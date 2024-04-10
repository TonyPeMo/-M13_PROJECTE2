package com.example.temperatura

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.temperatura.data.ConfiguracionData
import com.example.temperatura.data.Registro
import org.mindrot.jbcrypt.BCrypt
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale


class BaseDatosAPP(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {

    // Tabla USUARIO
    private val CREATE_TABLE_USUARIO = "CREATE TABLE USUARIO (" +
            "ID_USER INTEGER PRIMARY KEY AUTOINCREMENT," +
            "USERNAME TEXT UNIQUE," +
            "PASSWORD TEXT NOT NULL)"

    // Tabla CONFIGURACION
    private val CREATE_TABLE_CONFIGURACION = "CREATE TABLE CONFIGURACION (" +
            "ID_CONFIG INTEGER PRIMARY KEY AUTOINCREMENT," +
            "ID_USER INTEGER NOT NULL," +
            "COLOR_FRIO TEXT DEFAULT '#1C3AFF'," +
            "COLOR_OPTIMO TEXT DEFAULT '#00FF00'," +
            "COLOR_CALOR TEXT DEFAULT '#FF0000'," +
            "NOT_FRIO REAL DEFAULT 18.5," +
            "NOT_CALOR REAL DEFAULT 23.5," +
            "T_FRIO REAL DEFAULT 18.5," +
            "T_OPTIMA_MIN REAL DEFAULT 18.5," +
            "T_OPTIMA_MAX REAL DEFAULT 23.5," +
            "T_CALOR REAL DEFAULT 23.5," +
            "FOREIGN KEY (ID_USER) REFERENCES USUARIO (ID_USER))"

    // Tabla AULAS
    private val CREATE_TABLE_AULAS = "CREATE TABLE AULAS (" +
            "ID_AULA INTEGER PRIMARY KEY AUTOINCREMENT," +
            "NOM_AULA TEXT UNIQUE," +
            "NUM_PLANTA INTEGER NOT NULL)"

    // Tabla REGISTROS
    private val CREATE_TABLE_REGISTROS = "CREATE TABLE REGISTROS (" +
            "ID_REGISTRO INTEGER PRIMARY KEY AUTOINCREMENT," +
            "ID_AULA INTEGER NOT NULL," +
            "TEMPERATURA REAL NOT NULL," +
            "TERMOMETRO INTEGER NOT NULL," +
            "FECHA DATE NOT NULL," +
            "FOREIGN KEY (ID_AULA) REFERENCES AULAS (ID_AULA))"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_USUARIO)
        db?.execSQL(CREATE_TABLE_CONFIGURACION)
        db?.execSQL(CREATE_TABLE_AULAS)
        db?.execSQL(CREATE_TABLE_REGISTROS)

        // Contraseña del usuario
        val password = "admin"
        // Hashear la contraseña
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        // Insertar el usuario en la tabla USUARIO
        val insertQuery = "INSERT INTO USUARIO (USERNAME, PASSWORD) VALUES ('admin', '$hashedPassword')"
        db?.execSQL(insertQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Manejar actualizaciones de la base de datos si es necesario
        // Aún no implementado
    }
    fun setUser(username: String, password: String) {
        val db = this.writableDatabase
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val values = ContentValues().apply {
            put("USERNAME", username)
            put("PASSWORD", hashedPassword)
        }
        db.insert("USUARIO", null, values)
        db.close()
    }

    fun setConfiguracion(
        idUsuario: Int, colorFrio: String, colorOptimo: String, colorCalor: String, notFrio: Float, notCalor: Float, tFrio: Float, tOptimaMin: Float, tOptimaMax: Float, tCalor: Float) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("ID_USER", idUsuario)
            put("COLOR_FRIO", colorFrio)
            put("COLOR_OPTIMO", colorOptimo)
            put("COLOR_CALOR", colorCalor)
            put("NOT_FRIO", notFrio)
            put("NOT_CALOR", notCalor)
            put("T_FRIO", tFrio)
            put("T_OPTIMA_MIN", tOptimaMin)
            put("T_OPTIMA_MAX", tOptimaMax)
            put("T_CALOR", tCalor)
        }
        db.insert("CONFIGURACION", null, values)
        db.close()
    }

    fun setAula(nombreAula: String, numPlanta: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("NOM_AULA", nombreAula)
            put("NUM_PLANTA", numPlanta)
        }
        db.insert("AULAS", null, values)
        db.close()
    }

    fun setRegistro(idAula: Int, temperatura: Float, termometro: Int, fecha: Date) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("ID_AULA", idAula)
            put("TEMPERATURA", temperatura)
            put("TERMOMETRO", termometro)
            put("FECHA", fecha.time) // Convertir la fecha a milisegundos
        }
        db.insert("REGISTROS", null, values)
        db.close()
    }
    fun setRegistro(nombreAula: String, temperatura: Float, termometro: Int, fecha: Date) {
        val db = this.writableDatabase
        var idAula = getIdAulaPorNombre(nombreAula);
        val values = ContentValues().apply {
            put("ID_AULA", idAula)
            put("TEMPERATURA", temperatura)
            put("TERMOMETRO", termometro)
            put("FECHA", fecha.time) // Convertir la fecha a milisegundos
        }
        db.insert("REGISTROS", null, values)
        db.close()
    }
    fun setRegistro(nombreAula: String, temperatura: Float, termometro: Int, fechaString: String) {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val fecha = sdf.parse(fechaString)
        val db = this.writableDatabase
        var idAula = getIdAulaPorNombre(nombreAula);
        val values = ContentValues().apply {
            put("ID_AULA", idAula)
            put("TEMPERATURA", temperatura)
            put("TERMOMETRO", termometro)
            put("FECHA", fecha.time) // Convertir la fecha a milisegundos
        }
        db.insert("REGISTROS", null, values)
        db.close()
    }


    @SuppressLint("Range")
    fun getIdAulaPorNombre(nombreAula: String): Int? {
        var idAula: Int? = null
        val db = this.readableDatabase
        val query = "SELECT ID_AULA FROM AULAS WHERE NOM_AULA = ?"
        val cursor: Cursor? = db.rawQuery(query, arrayOf(nombreAula))

        cursor?.use {
            if (cursor.moveToFirst()) {
                idAula = cursor.getInt(cursor.getColumnIndex("ID_AULA"))
            }
        }

        cursor?.close()
        db.close()
        return idAula
    }

    @SuppressLint("Range")
    fun comparePassword(username: String, password: String): Boolean {
        val db = this.readableDatabase
        var result = false

        val query = "SELECT PASSWORD FROM USUARIO WHERE USERNAME = ?"
        val cursor: Cursor? = db.rawQuery(query, arrayOf(username))

        cursor?.use {
            if (cursor.moveToFirst()) {
                val storedHashedPassword = cursor.getString(cursor.getColumnIndex("PASSWORD"))
                // Comparar la contraseña proporcionada con la almacenada en la base de datos
                result = BCrypt.checkpw(password, storedHashedPassword)
            }
        }

        cursor?.close()
        db.close()
        return result
    }



    private val GET_REGISTROS_BY_AULA_ID = "SELECT * FROM REGISTROS WHERE ID_AULA = ?"

    @SuppressLint("Range")
    fun getRegistrosPorAulaId(idAula: Int): List<Registro> {
        val registros = mutableListOf<Registro>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(GET_REGISTROS_BY_AULA_ID, arrayOf(idAula.toString()))

        cursor?.use {
            while (cursor.moveToNext()) {
                val idRegistro = cursor.getInt(cursor.getColumnIndex("ID_REGISTRO"))
                val idAula = cursor.getInt(cursor.getColumnIndex("ID_AULA"))
                val temperatura = cursor.getFloat(cursor.getColumnIndex("TEMPERATURA"))
                val termometro = cursor.getInt(cursor.getColumnIndex("TERMOMETRO"))
                val fechaLong = cursor.getLong(cursor.getColumnIndex("FECHA"))
                val fecha = Date(fechaLong)
                val registro = Registro(idRegistro, idAula, temperatura, termometro, fecha)
                registros.add(registro)
            }
        }

        db.close()
        return registros
    }


    private val GET_REGISTROS_BY_FECHA = "SELECT * FROM REGISTROS WHERE FECHA BETWEEN ? AND ?"

    @SuppressLint("Range")
    fun getRegistrosPorFecha(fechaInicio: Date, fechaFin: Date): List<Registro> {
        val registros = mutableListOf<Registro>()
        val db = this.readableDatabase
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val fechaInicioStr = sdf.format(fechaInicio)
        val fechaFinStr = sdf.format(fechaFin)
        val cursor: Cursor? = db.rawQuery(GET_REGISTROS_BY_FECHA, arrayOf(fechaInicioStr, fechaFinStr))

        cursor?.use {
            while (cursor.moveToNext()) {
                val idRegistro = cursor.getInt(cursor.getColumnIndex("ID_REGISTRO"))
                val idAula = cursor.getInt(cursor.getColumnIndex("ID_AULA"))
                val temperatura = cursor.getFloat(cursor.getColumnIndex("TEMPERATURA"))
                val termometro = cursor.getInt(cursor.getColumnIndex("TERMOMETRO"))
                val fechaLong = cursor.getLong(cursor.getColumnIndex("FECHA"))
                val fecha = Date(fechaLong)
                val registro = Registro(idRegistro, idAula, temperatura, termometro, fecha)
                registros.add(registro)
            }
        }

        db.close()
        return registros
    }

    private val GET_REGISTROS_BY_FECHA_ID_AULA = "SELECT * FROM REGISTROS WHERE ID_AULA = ? AND FECHA BETWEEN ? AND ?"

    private fun getFechaMasRecienteAula(idAula: Int): Date? {
        val db = this.readableDatabase
        val query = "SELECT MAX(FECHA) FROM REGISTROS WHERE ID_AULA = ?"
        val cursor: Cursor? = db.rawQuery(query, arrayOf(idAula.toString()))
        var fechaMasReciente: Date? = null

        cursor?.use {
            if (cursor.moveToFirst()) {
                val fechaLong = cursor.getLong(0)
                fechaMasReciente = Date(fechaLong)
            }
        }

        cursor?.close()
        db.close()
        return fechaMasReciente
    }

    // Función para obtener registros por fecha
    @SuppressLint("SimpleDateFormat", "Range")
    fun getRegistrosUltimos5Minutos(idAula: Int): List<Registro> {
        val registros = mutableListOf<Registro>()
        val db = this.readableDatabase

        // Obtener la fecha más reciente
        val fechaMasReciente = getFechaMasRecienteAula(idAula)
        if (fechaMasReciente != null) {
            // Calcular la fecha de inicio (hace 5 minutos)
            val fechaInicio = Date(System.currentTimeMillis() - 5 * 60 * 1000)

            // Crear el formato de fecha
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

            // Obtener las fechas como cadenas de texto
            val fechaInicioStr = sdf.format(fechaInicio)
            val fechaFinStr = sdf.format(fechaMasReciente)

            // Ejecutar la consulta SQL
            val cursor: Cursor? = db.rawQuery(GET_REGISTROS_BY_FECHA_ID_AULA, arrayOf(idAula.toString(), fechaInicioStr, fechaFinStr))

            cursor?.use {
                while (cursor.moveToNext()) {
                    val idRegistro = cursor.getInt(cursor.getColumnIndex("ID_REGISTRO"))
                    val idAula = cursor.getInt(cursor.getColumnIndex("ID_AULA"))
                    val temperatura = cursor.getFloat(cursor.getColumnIndex("TEMPERATURA"))
                    val termometro = cursor.getInt(cursor.getColumnIndex("TERMOMETRO"))
                    val fechaLong = cursor.getLong(cursor.getColumnIndex("FECHA"))
                    val fecha = Date(fechaLong)
                    val registro = Registro(idRegistro, idAula, temperatura, termometro, fecha)
                    registros.add(registro)
                }
            }

            cursor?.close()
        }

        db.close()
        return registros
    }

    private val GET_CONFIGURACION_BY_ID_USER = "SELECT * FROM CONFIGURACION WHERE ID_USER = ?"

    @SuppressLint("Range")
    fun getConfiguracionPorIdUsuario(idUsuario: Int): ConfiguracionData? {
        var configuracion: ConfiguracionData? = null
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(GET_CONFIGURACION_BY_ID_USER, arrayOf(idUsuario.toString()))

        cursor?.use {
            if (cursor.moveToFirst()) {
                val idConfiguracion = cursor.getInt(cursor.getColumnIndex("ID_CONFIG"))
                val idUser = cursor.getInt(cursor.getColumnIndex("ID_USER"))
                val colorFrio = cursor.getString(cursor.getColumnIndex("COLOR_FRIO"))
                val colorOptimo = cursor.getString(cursor.getColumnIndex("COLOR_OPTIMO"))
                val colorCalor = cursor.getString(cursor.getColumnIndex("COLOR_CALOR"))
                val notFrio = cursor.getFloat(cursor.getColumnIndex("NOT_FRIO"))
                val notCalor = cursor.getFloat(cursor.getColumnIndex("NOT_CALOR"))
                val tFrio = cursor.getFloat(cursor.getColumnIndex("T_FRIO"))
                val tOptimaMin = cursor.getFloat(cursor.getColumnIndex("T_OPTIMA_MIN"))
                val tOptimaMax = cursor.getFloat(cursor.getColumnIndex("T_OPTIMA_MAX"))
                val tCalor = cursor.getFloat(cursor.getColumnIndex("T_CALOR"))
                configuracion = ConfiguracionData(idConfiguracion, idUser, colorFrio, colorOptimo, colorCalor,notFrio,
                    notCalor, tFrio, tOptimaMin, tOptimaMax, tCalor)
            }
        }

        cursor?.close()
        db.close()
        return configuracion
    }

}
