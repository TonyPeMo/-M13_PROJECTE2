package com.example.temperatura

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Locale

class ValoresDatabase : AppCompatActivity() {
    private lateinit var baseDatos: BaseDatosAPP
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar la base de datos
        baseDatos = BaseDatosAPP(this, "temperaturaDB", null, 1)

        // Ejemplo de uso de las funciones para establecer datos en la base de datos
        baseDatos.setUser("admin2", "1234")
        // Puedes llamar a las otras funciones de la base de datos de la misma manera
        baseDatos.setAula("A01", 0)
        baseDatos.setAula("A02", 0)
        baseDatos.setAula("A03", 0)
        baseDatos.setAula("A04", 0)
        baseDatos.setAula("ATECA", 0)

        baseDatos.setRegistro("A01", 19.5f, 1, "08-04-2024 10:33:00")
        baseDatos.setRegistro("A01", 20.0f, 2, "08-04-2024 10:30:00")

        baseDatos.setRegistro("A01", 18.5f, 1, "08-04-2024 09:33:00")
        baseDatos.setRegistro("A01", 19.0f, 2, "08-04-2024 09:31:00")

        baseDatos.setRegistro("A01", 22.5f, 1, "08-04-2024 12:33:00")
        baseDatos.setRegistro("A01", 22.0f, 2, "08-04-2024 12:29:00")

        baseDatos.setRegistro("A01", 23.5f, 1, "08-04-2024 16:33:00")
        baseDatos.setRegistro("A01", 29.0f, 2, "08-04-2024 16:33:00")


        baseDatos.setRegistro("A02", 19.0f, 1, "08-04-2024 10:35:00")
        baseDatos.setRegistro("A02", 20.5f, 2, "08-04-2024 10:40:00")

        baseDatos.setRegistro("A02", 18.8f, 1, "08-04-2024 09:35:00")
        baseDatos.setRegistro("A02", 19.2f, 2, "08-04-2024 09:38:00")

        baseDatos.setRegistro("A02", 21.5f, 1, "08-04-2024 12:35:00")
        baseDatos.setRegistro("A02", 21.0f, 2, "08-04-2024 12:38:00")

        baseDatos.setRegistro("A02", 24.0f, 1, "08-04-2024 16:35:00")
        baseDatos.setRegistro("A02", 28.0f, 2, "08-04-2024 16:38:00")

        baseDatos.setRegistro("A03", 19.2f, 1, "08-04-2024 10:37:00")
        baseDatos.setRegistro("A03", 20.8f, 2, "08-04-2024 10:42:00")

        baseDatos.setRegistro("A03", 18.6f, 1, "08-04-2024 09:37:00")
        baseDatos.setRegistro("A03", 19.4f, 2, "08-04-2024 09:40:00")

        baseDatos.setRegistro("A03", 21.0f, 1, "08-04-2024 12:37:00")
        baseDatos.setRegistro("A03", 20.5f, 2, "08-04-2024 12:42:00")

        baseDatos.setRegistro("A03", 23.0f, 1, "08-04-2024 16:37:00")
        baseDatos.setRegistro("A03", 27.0f, 2, "08-04-2024 16:42:00")

        baseDatos.setRegistro("A04", 19.3f, 1, "08-04-2024 10:45:00")
        baseDatos.setRegistro("A04", 20.3f, 2, "08-04-2024 10:50:00")

        baseDatos.setRegistro("A04", 18.8f, 1, "08-04-2024 09:45:00")
        baseDatos.setRegistro("A04", 19.6f, 2, "08-04-2024 09:48:00")

        baseDatos.setRegistro("A04", 21.2f, 1, "08-04-2024 12:45:00")
        baseDatos.setRegistro("A04", 20.8f, 2, "08-04-2024 12:48:00")

        baseDatos.setRegistro("A04", 23.2f, 1, "08-04-2024 16:45:00")
        baseDatos.setRegistro("A04", 27.5f, 2, "08-04-2024 16:48:00")

        baseDatos.setRegistro("ATECA", 19.8f, 1, "08-04-2024 10:55:00")
        baseDatos.setRegistro("ATECA", 20.7f, 2, "08-04-2024 11:00:00")

        baseDatos.setRegistro("ATECA", 18.7f, 1, "08-04-2024 09:55:00")
        baseDatos.setRegistro("ATECA", 19.9f, 2, "08-04-2024 09:58:00")

        baseDatos.setRegistro("ATECA", 21.8f, 1, "08-04-2024 12:55:00")
        baseDatos.setRegistro("ATECA", 21.5f, 2, "08-04-2024 13:00:00")

        baseDatos.setRegistro("ATECA", 23.8f, 1, "08-04-2024 16:55:00")
        baseDatos.setRegistro("ATECA", 29.2f, 2, "08-04-2024 17:00:00")





    }



}