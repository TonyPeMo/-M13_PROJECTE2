package com.example.temperatura

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.sql.SQLException
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {
    val ruta = "http://192.168.1.149:8081"

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
        val intent = Intent(this@Login, PantallaInicio::class.java)
        intent.putExtra("username", "admin")
        intent.putExtra("ruta", ruta)
        startActivity(intent)
    }
    fun toInicioApi(view: View) {
        val usernameField = findViewById<TextInputEditText>(R.id.editTextUsuario)
        val passwordField = findViewById<TextInputEditText>(R.id.editTextPassword)

        val username = usernameField.text.toString()
        val password = passwordField.text.toString()

        GlobalScope.launch(Dispatchers.IO) {
            if (verifyUser(username, password)) {
                withContext(Dispatchers.Main) {
                    val intent = Intent(this@Login, PantallaInicio::class.java)
                    intent.putExtra("username", username)
                    intent.putExtra("ruta", ruta)
                    startActivity(intent)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Login, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun verifyUser(username: String, password: String): Boolean {
        try {
            val client = OkHttpClient()
            val url = "$ruta/usuario/verifyPassword?nombre=$username&password=$password"

            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string()
                if (responseBody == "true") {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            // Manejar cualquier excepción que ocurra durante la consulta
        }
        return false
    }
}