import android.annotation.SuppressLint
import com.example.temperatura.data.ConfiguracionData
import com.example.temperatura.data.Registro
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import org.mindrot.jbcrypt.BCrypt
import java.sql.ResultSet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Ejemplo de uso
//fun main() {
//    val url = "jdbc:mysql://localhost:3306/nombre_base_de_datos"
//    val username = "usuario"
//    val password = "contraseña"
//
//    val db = MySQLDatabase(url, username, password)
//    db.connect()
//    createTables()
//    db.insertDefaultValues()
//    db.disconnect()
//}



class MySQLDatabase(private val url: String, private val username: String, private val password: String) {

    private var connection: Connection? = null

    fun connect() {
        try {
            connection = DriverManager.getConnection(url, username, password)
            println("Conexión establecida con la base de datos.")
        } catch (e: SQLException) {
            println("Error al conectar a la base de datos: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            connection?.close()
            println("Conexión cerrada correctamente.")
        } catch (e: SQLException) {
            println("Error al cerrar la conexión: ${e.message}")
        }
    }

    fun createTables() {
        val statements = arrayOf(
            "CREATE TABLE IF NOT EXIST USUARIO (ID_USER INT AUTO_INCREMENT PRIMARY KEY," +
                    " USERNAME VARCHAR(255) UNIQUE," +
                    " PASSWORD TEXT NOT NULL)",

            "CREATE TABLE IF NOT EXIST CONFIGURACION (ID_CONFIG INT AUTO_INCREMENT PRIMARY KEY," +
                    " ID_USER INT NOT NULL," +
                    " COLOR_FRIO VARCHAR(7) DEFAULT '#1C3AFF'," +
                    " COLOR_OPTIMO VARCHAR(7) DEFAULT '#00FF00'," +
                    " COLOR_CALOR VARCHAR(7) DEFAULT '#FF0000'," +
                    " NOT_FRIO FLOAT DEFAULT 18.5," +
                    " NOT_CALOR FLOAT DEFAULT 23.5," +
                    " T_FRIO FLOAT DEFAULT 18.5," +
                    " T_OPTIMA_MIN FLOAT DEFAULT 18.5," +
                    " T_OPTIMA_MAX FLOAT DEFAULT 23.5," +
                    " T_CALOR FLOAT DEFAULT 23.5," +
                    " FOREIGN KEY (ID_USER) REFERENCES USUARIO(ID_USER))",

            "CREATE TABLE IF NOT EXIST AULAS (ID_AULA INT AUTO_INCREMENT PRIMARY KEY," +
                    " NOM_AULA VARCHAR(255) UNIQUE," +
                    " NUM_PLANTA INT NOT NULL)",

            "CREATE TABLE IF NOT EXIST REGISTROS (ID_REGISTRO INT AUTO_INCREMENT PRIMARY KEY," +
                    " ID_AULA INT NOT NULL, TEMPERATURA FLOAT NOT NULL," +
                    " TERMOMETRO INT NOT NULL," +
                    " FECHA DATETIME NOT NULL," +
                    " FOREIGN KEY (ID_AULA) REFERENCES AULAS(ID_AULA))"
        )

        try {
            connection?.createStatement()?.use { statement ->
                for (sql in statements) {
                    statement.executeUpdate(sql)
                }
                println("Tablas creadas correctamente.")
            }
        } catch (e: SQLException) {
            println("Error al crear las tablas: ${e.message}")
        }
    }

    fun insertDefaultValues() {
        val defaultInsertStatements = arrayOf(
            "INSERT INTO AULAS (NOM_AULA, NUM_PLANTA) VALUES ('A01', 0)",
            "INSERT INTO AULAS (NOM_AULA, NUM_PLANTA) VALUES ('A02', 0)",
            "INSERT INTO AULAS (NOM_AULA, NUM_PLANTA) VALUES ('A03', 0)",
            "INSERT INTO AULAS (NOM_AULA, NUM_PLANTA) VALUES ('A04', 0)",
            "INSERT INTO AULAS (NOM_AULA, NUM_PLANTA) VALUES ('ATECA', 0)",

            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A01', 19.5, 1, '08-04-2024 10:33:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A01', 20.0, 2, '08-04-2024 10:30:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A01', 18.5, 1, '08-04-2024 09:33:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A01', 19.0, 2, '08-04-2024 09:31:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A01', 22.5, 1, '08-04-2024 12:33:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A01', 22.0, 2, '08-04-2024 12:29:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A01', 23.5, 1, '08-04-2024 16:33:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A01', 29.0, 2, '08-04-2024 16:33:00')",

            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A02', 19.0, 1, '08-04-2024 10:35:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A02', 20.5, 2, '08-04-2024 10:40:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A02', 18.8, 1, '08-04-2024 09:35:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A02', 19.2, 2, '08-04-2024 09:38:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A02', 21.5, 1, '08-04-2024 12:35:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A02', 21.0, 2, '08-04-2024 12:38:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A02', 24.0, 1, '08-04-2024 16:35:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A02', 28.0, 2, '08-04-2024 16:38:00')",

            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A03', 19.2, 1, '08-04-2024 10:37:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A03', 20.8, 2, '08-04-2024 10:42:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A03', 18.6, 1, '08-04-2024 09:37:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A03', 19.4, 2, '08-04-2024 09:40:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A03', 21.0, 1, '08-04-2024 12:37:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A03', 20.5, 2, '08-04-2024 12:42:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A03', 23.0, 1, '08-04-2024 16:37:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A03', 27.0, 2, '08-04-2024 16:42:00')",

            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A04', 19.3, 1, '08-04-2024 10:45:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A04', 20.3, 2, '08-04-2024 10:50:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A04', 18.8, 1, '08-04-2024 09:45:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A04', 19.6, 2, '08-04-2024 09:48:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A04', 21.2, 1, '08-04-2024 12:45:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A04', 20.8, 2, '08-04-2024 12:48:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A04', 23.2, 1, '08-04-2024 16:45:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('A04', 27.5, 2, '08-04-2024 16:48:00')",

            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('ATECA', 19.8, 1, '08-04-2024 10:55:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('ATECA', 20.7, 2, '08-04-2024 11:00:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('ATECA', 18.7, 1, '08-04-2024 09:55:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('ATECA', 19.9, 2, '08-04-2024 09:58:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('ATECA', 21.8, 1, '08-04-2024 12:55:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('ATECA', 21.5, 2, '08-04-2024 13:00:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('ATECA', 23.8, 1, '08-04-2024 16:55:00')",
            "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES ('ATECA', 29.2, 2, '08-04-2024 17:00:00')"
        )

        try {
            connection?.createStatement()?.use { statement ->
                for (sql in defaultInsertStatements) {
                    val result = statement.executeUpdate(sql)
                    if (result > 0) {
                        println("Insert ejecutado correctamente: $sql")
                    } else {
                        println("No se ejecutó el insert: $sql (ya existía)")
                    }
                }
            }
        } catch (e: SQLException) {
            println("Error al ejecutar los inserts: ${e.message}")
        }
    }

    fun setUser(username: String, password: String): Boolean {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val query = "INSERT INTO USUARIO (USERNAME, PASSWORD) VALUES (?, ?)"
        return try {
            connection?.prepareStatement(query)?.use { statement ->
                statement.setString(1, username)
                statement.setString(2, hashedPassword)
                statement.executeUpdate() > 0
            } ?: false
        } catch (e: SQLException) {
            println("Error al crear el usuario: ${e.message}")
            false
        }
    }
    @SuppressLint("Range")
    fun getIdUsuarioPorNombre(nombreUsuario: String): Int? {
        var idUsuario: Int? = null
        val query = "SELECT ID_USER FROM USUARIO WHERE USERNAME = ?"

        try {
            val preparedStatement: PreparedStatement = connection!!.prepareStatement(query)
            preparedStatement.setString(1, nombreUsuario)
            val resultSet: ResultSet = preparedStatement.executeQuery()

            if (resultSet.next()) {
                idUsuario = resultSet.getInt("ID_USER")
            }

            resultSet.close()
            preparedStatement.close()
        } catch (e: SQLException) {
            println("Error al ejecutar la consulta: ${e.message}")
        }

        return idUsuario
    }

    fun setConfiguracion(userId: Int): Boolean {
        val query = "INSERT INTO CONFIGURACION (ID_USER) VALUES (?)"
        return try {
            connection?.prepareStatement(query)?.use { statement ->
                statement.setInt(1, userId)
                statement.executeUpdate() > 0
            } ?: false
        } catch (e: SQLException) {
            println("Error al crear la configuración: ${e.message}")
            false
        }
    }

    fun setAula(nombreAula: String, numPlanta: Int) {
        val query = "INSERT INTO AULAS (NOM_AULA, NUM_PLANTA) VALUES (?, ?)"
        executeInsert(query, nombreAula, numPlanta)
    }

    fun setRegistro(idAula: Int, temperatura: Float, termometro: Int, fecha: Date) {
        val query = "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES (?, ?, ?, ?)"
        executeInsert(query, idAula, temperatura, termometro, fecha)
    }

    fun setRegistro(nombreAula: String, temperatura: Float, termometro: Int, fecha: Date) {
        val idAula = getIdAulaPorNombre(nombreAula)
        val query = "INSERT INTO REGISTROS (ID_AULA, TEMPERATURA, TERMOMETRO, FECHA) VALUES (?, ?, ?, ?)"
        executeInsert(query, idAula!!, temperatura, termometro, fecha)
    }

    fun setRegistro(nombreAula: String, temperatura: Float, termometro: Int, fechaString: String) {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val fecha = sdf.parse(fechaString)
        setRegistro(nombreAula, temperatura, termometro, fecha)
    }

    fun setRegistro(idAula: Int, temperatura: Float, termometro: Int, fechaString: String) {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val fecha = sdf.parse(fechaString)
        setRegistro(idAula, temperatura, termometro, fecha)
    }

    private fun executeInsert(query: String, vararg params: Any) {
        try {
            val preparedStatement: PreparedStatement = connection!!.prepareStatement(query)
            for (i in params.indices) {
                preparedStatement.setObject(i + 1, params[i])
            }
            preparedStatement.executeUpdate()
            preparedStatement.close()
        } catch (e: SQLException) {
            println("Error al ejecutar la consulta: ${e.message}")
        }
    }
    fun setConfiguracion(idUsuario: Int, colorFrio: String, colorOptimo: String, colorCalor: String,
                         notFrio: Float, notCalor: Float, tFrio: Float, tOptimaMin: Float, tOptimaMax: Float, tCalor: Float) {
        val query = "INSERT INTO CONFIGURACION (ID_USER, COLOR_FRIO, COLOR_OPTIMO, COLOR_CALOR, NOT_FRIO, NOT_CALOR, T_FRIO, T_OPTIMA_MIN, T_OPTIMA_MAX, T_CALOR) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"

        try {
            val preparedStatement: PreparedStatement = connection!!.prepareStatement(query)
            preparedStatement.setInt(1, idUsuario)
            preparedStatement.setString(2, colorFrio)
            preparedStatement.setString(3, colorOptimo)
            preparedStatement.setString(4, colorCalor)
            preparedStatement.setFloat(5, notFrio)
            preparedStatement.setFloat(6, notCalor)
            preparedStatement.setFloat(7, tFrio)
            preparedStatement.setFloat(8, tOptimaMin)
            preparedStatement.setFloat(9, tOptimaMax)
            preparedStatement.setFloat(10, tCalor)
            preparedStatement.executeUpdate()
            preparedStatement.close()
        } catch (e: SQLException) {
            println("Error al ejecutar la consulta: ${e.message}")
        }
    }

    fun updateConfiguracion(idUsuario: Int, colorFrio: String, colorOptimo: String, colorCalor: String,
                            notFrio: Float, notCalor: Float, tFrio: Float, tOptimaMin: Float, tOptimaMax: Float, tCalor: Float) {
        val query = "UPDATE CONFIGURACION SET COLOR_FRIO = ?, COLOR_OPTIMO = ?, COLOR_CALOR = ?, NOT_FRIO = ?, NOT_CALOR = ?, T_FRIO = ?, T_OPTIMA_MIN = ?, T_OPTIMA_MAX = ?, T_CALOR = ? WHERE ID_USER = ?"

        try {
            val preparedStatement: PreparedStatement = connection!!.prepareStatement(query)
            preparedStatement.setString(1, colorFrio)
            preparedStatement.setString(2, colorOptimo)
            preparedStatement.setString(3, colorCalor)
            preparedStatement.setFloat(4, notFrio)
            preparedStatement.setFloat(5, notCalor)
            preparedStatement.setFloat(6, tFrio)
            preparedStatement.setFloat(7, tOptimaMin)
            preparedStatement.setFloat(8, tOptimaMax)
            preparedStatement.setFloat(9, tCalor)
            preparedStatement.setInt(10, idUsuario)
            preparedStatement.executeUpdate()
            preparedStatement.close()
        } catch (e: SQLException) {
            println("Error al ejecutar la consulta: ${e.message}")
        }
    }
    @SuppressLint("Range")
    fun getIdAulaPorNombre(nombreAula: String): Int? {
        var idAula: Int? = null
        val query = "SELECT ID_AULA FROM AULAS WHERE NOM_AULA = ?"

        try {
            val preparedStatement: PreparedStatement = connection!!.prepareStatement(query)
            preparedStatement.setString(1, nombreAula)
            val resultSet: ResultSet = preparedStatement.executeQuery()

            if (resultSet.next()) {
                idAula = resultSet.getInt("ID_AULA")
            }

            resultSet.close()
            preparedStatement.close()
        } catch (e: SQLException) {
            println("Error al ejecutar la consulta: ${e.message}")
        }

        return idAula
    }

    @SuppressLint("Range")
    fun comparePassword(username: String, password: String): Boolean {
        var result = false
        val query = "SELECT PASSWORD FROM USUARIO WHERE USERNAME = ?"

        try {
            val preparedStatement: PreparedStatement = connection!!.prepareStatement(query)
            preparedStatement.setString(1, username)
            val resultSet: ResultSet = preparedStatement.executeQuery()

            if (resultSet.next()) {
                val storedHashedPassword = resultSet.getString("PASSWORD")
                // Comparar la contraseña proporcionada con la almacenada en la base de datos
                result = BCrypt.checkpw(password, storedHashedPassword)
            }

            resultSet.close()
            preparedStatement.close()
        } catch (e: SQLException) {
            println("Error al ejecutar la consulta: ${e.message}")
        }

        return result
    }

    private val GET_REGISTROS_BY_AULA_ID = "SELECT * FROM REGISTROS WHERE ID_AULA = ?"

    @SuppressLint("Range")
    fun getRegistrosPorAulaId(idAula: Int): List<Registro> {
        val registros = mutableListOf<Registro>()
        val query = GET_REGISTROS_BY_AULA_ID

        try {
            val preparedStatement: PreparedStatement = connection!!.prepareStatement(query)
            preparedStatement.setInt(1, idAula)
            val resultSet: ResultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                val idRegistro = resultSet.getInt("ID_REGISTRO")
                val temperatura = resultSet.getFloat("TEMPERATURA")
                val termometro = resultSet.getInt("TERMOMETRO")
                val fechaLong = resultSet.getLong("FECHA")
                val fecha = Date(fechaLong)
                val registro = Registro(idRegistro, idAula, temperatura, termometro, fecha)
                registros.add(registro)
            }

            resultSet.close()
            preparedStatement.close()
        } catch (e: SQLException) {
            println("Error al ejecutar la consulta: ${e.message}")
        }

        return registros
    }

    private val GET_REGISTROS_BY_FECHA = "SELECT * FROM REGISTROS WHERE FECHA BETWEEN ? AND ?"

    @SuppressLint("Range")
    fun getRegistrosPorFecha(fechaInicio: Date, fechaFin: Date): List<Registro> {
        val registros = mutableListOf<Registro>()
        val query = GET_REGISTROS_BY_FECHA

        try {
            val preparedStatement: PreparedStatement = connection!!.prepareStatement(query)
            preparedStatement.setString(1, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(fechaInicio))
            preparedStatement.setString(2, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(fechaFin))
            val resultSet: ResultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                val idRegistro = resultSet.getInt("ID_REGISTRO")
                val idAula = resultSet.getInt("ID_AULA")
                val temperatura = resultSet.getFloat("TEMPERATURA")
                val termometro = resultSet.getInt("TERMOMETRO")
                val fechaLong = resultSet.getLong("FECHA")
                val fecha = Date(fechaLong)
                val registro = Registro(idRegistro, idAula, temperatura, termometro, fecha)
                registros.add(registro)
            }

            resultSet.close()
            preparedStatement.close()
        } catch (e: SQLException) {
            println("Error al ejecutar la consulta: ${e.message}")
        }

        return registros
    }
    private val GET_REGISTROS_BY_FECHA_ID_AULA = "SELECT * FROM REGISTROS WHERE ID_AULA = ? AND FECHA BETWEEN ? AND ?"

    private fun getFechaMasRecienteAula(idAula: Int): Date? {
        val connection: Connection = DriverManager.getConnection(url, username, password)
        val query = "SELECT MAX(FECHA) FROM REGISTROS WHERE ID_AULA = ?"
        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, idAula)
        val resultSet: ResultSet = preparedStatement.executeQuery()
        var fechaMasReciente: Date? = null

        if (resultSet.next()) {
            val fechaLong = resultSet.getLong(1)
            fechaMasReciente = Date(fechaLong)
        }

        resultSet.close()
        preparedStatement.close()
        connection.close()
        return fechaMasReciente
    }

    fun getRegistrosUltimos5Minutos(idAula: Int): List<Registro> {
        val registros = mutableListOf<Registro>()
        val connection: Connection = DriverManager.getConnection(url, username, password)

        // Obtener la fecha más reciente
        val fechaMasReciente = getFechaMasRecienteAula(idAula)
        if (fechaMasReciente != null) {
            // Calcular la fecha de inicio (hace 5 minutos)
            val fechaInicio = Date(System.currentTimeMillis() - 5 * 60 * 1000)

            // Crear el formato de fecha
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val fechaInicioStr = sdf.format(fechaInicio)
            val fechaFinStr = sdf.format(fechaMasReciente)

            // Ejecutar la consulta SQL
            val preparedStatement: PreparedStatement = connection.prepareStatement(GET_REGISTROS_BY_FECHA_ID_AULA)
            preparedStatement.setInt(1, idAula)
            preparedStatement.setString(2, fechaInicioStr)
            preparedStatement.setString(3, fechaFinStr)
            val resultSet: ResultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                val idRegistro = resultSet.getInt("ID_REGISTRO")
                val temperatura = resultSet.getFloat("TEMPERATURA")
                val termometro = resultSet.getInt("TERMOMETRO")
                val fechaLong = resultSet.getLong("FECHA")
                val fecha = Date(fechaLong)
                val registro = Registro(idRegistro, idAula, temperatura, termometro, fecha)
                registros.add(registro)
            }

            resultSet.close()
            preparedStatement.close()
        }

        connection.close()
        return registros
    }

    private val GET_CONFIGURACION_BY_ID_USER = "SELECT * FROM CONFIGURACION WHERE ID_USER = ?"

    fun getConfiguracionPorIdUsuario(idUsuario: Int): ConfiguracionData? {
        var configuracion: ConfiguracionData? = null
        val connection: Connection = DriverManager.getConnection(url, username, password)
        val preparedStatement: PreparedStatement = connection.prepareStatement(GET_CONFIGURACION_BY_ID_USER)
        preparedStatement.setInt(1, idUsuario)
        val resultSet: ResultSet = preparedStatement.executeQuery()

        if (resultSet.next()) {
            val idConfiguracion = resultSet.getInt("ID_CONFIG")
            val idUser = resultSet.getInt("ID_USER")
            val colorFrio = resultSet.getString("COLOR_FRIO")
            val colorOptimo = resultSet.getString("COLOR_OPTIMO")
            val colorCalor = resultSet.getString("COLOR_CALOR")
            val notFrio = resultSet.getFloat("NOT_FRIO")
            val notCalor = resultSet.getFloat("NOT_CALOR")
            val tFrio = resultSet.getFloat("T_FRIO")
            val tOptimaMin = resultSet.getFloat("T_OPTIMA_MIN")
            val tOptimaMax = resultSet.getFloat("T_OPTIMA_MAX")
            val tCalor = resultSet.getFloat("T_CALOR")
            configuracion = ConfiguracionData(idConfiguracion, idUser, colorFrio, colorOptimo, colorCalor, notFrio,
                notCalor, tFrio, tOptimaMin, tOptimaMax, tCalor)
        }

        resultSet.close()
        preparedStatement.close()
        connection.close()
        return configuracion
    }





}
