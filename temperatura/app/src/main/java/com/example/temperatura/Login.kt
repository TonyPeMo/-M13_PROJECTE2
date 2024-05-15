package com.example.temperatura

import MySQLDatabase
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.sql.SQLException

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun toInicio(view: View) {
        val intent = Intent(this, PantallaInicio::class.java)

        // Datos de conexión
        val url = "jdbc:mysql://192.168.19.73:3307/temperatura"
        val username = "root"
        val password = "root"

        try {
            // Crear conexión y operaciones en la base de datos
            val db = MySQLDatabase(url, username, password)
            db.connect()
            db.createTables()
            db.insertDefaultValues()
            db.disconnect()

            // Iniciar la siguiente actividad
            startActivity(intent)
        } catch (e: SQLException) {
            // Manejar cualquier error de conexión a la base de datos
            Log.e("Error de conexión", e.message ?: "Error desconocido al conectar a la base de datos")
            // Aquí puedes mostrar un mensaje de error al usuario si lo deseas
        }
    }

}