package com.example.temperatura

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class TemperaturaAula : AppCompatActivity() {

    private var username: String? = null
    private var ruta: String? = null
    private var selectedAula: String? = "A01"
    private var temperatura: Double = 0.0
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_temperatura_aula)

        username = intent.getStringExtra("username")
        ruta = intent.getStringExtra("ruta")
        selectedAula = intent.getStringExtra("aula")


        // Establecer el nombre de la aula
        val nombreAula = findViewById<TextView>(R.id.nombreAula)
        nombreAula.text = selectedAula

        val temperaturaActual = obtenerTemperaturaActual()

        val temperatura = findViewById<TextView>(R.id.temperatura)
        temperatura.text = temperaturaActual






        // Aplicar el borde de la pantalla completa
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.linearLayout)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    // Métodos para obtener temperaturas simuladas (debes reemplazar estos métodos con tus propios métodos para obtener las temperaturas reales)
    private fun obtenerTemperaturaActual(): String {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$ruta/aulas/nombre/$selectedAula/ultimafecha")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"

                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                // Procesar el JSON de respuesta de temperatura del aula
                temperatura = response.toString().toDouble()

                // Cerrar la conexión del aula
                urlConnection.disconnect()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        while (temperatura == 0.0) {
            // Esperar a que la temperatura se actualice
            Thread.sleep(100)
        }
        return temperatura.toString() + "°C"
    }



    private fun applyColorToString(text: String, color: Int): SpannableString {
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            ForegroundColorSpan(color),
            text.indexOf(':') + 2,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    fun toAtras(view: View) {
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
        onBackPressed()
    }

    fun toGrafico(view: View) {
        val intent = Intent(this, Graficas::class.java).apply{}
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
        intent.putExtra("aula", selectedAula)
        startActivity(intent);
    }

}
