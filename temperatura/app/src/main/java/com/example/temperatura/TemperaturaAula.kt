package com.example.temperatura

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class TemperaturaAula : AppCompatActivity() {

    private var username: String? = null
    private var ruta: String? = null
    private var selectedAula: String? = "A01"
    private var temperatura: Double = 0.0
    private lateinit var spinner: Spinner

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_temperatura_aula)

        username = intent.getStringExtra("username")
        ruta = intent.getStringExtra("ruta")
        selectedAula = intent.getStringExtra("aula")
        Log.d("PantallaInicio", selectedAula.toString())

        // Establecer el nombre de la aula
        val nombreAula = findViewById<TextView>(R.id.nombreAula)
        nombreAula.text = selectedAula

        // Inicializar el Spinner y agregar el listener
        spinner = findViewById(R.id.spinner_menu)

        val aulas = listOf("A01", "A02", "A03", "A04", "ATECA") // La lista de aulas disponibles
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, aulas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Seleccionar el aula actual en el Spinner
        val position = aulas.indexOf(selectedAula)
        if (position >= 0) {
            spinner.setSelection(position)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedAula = parent.getItemAtPosition(position) as String
                nombreAula.text = selectedAula
                updateTemperature()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }

        updateTemperature()

        // Aplicar el borde de la pantalla completa
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.linearLayout)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateTemperature() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$ruta/aulas/nombre/$selectedAula/ultimafecha")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"

                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val response = bufferedReader.use { it.readText() }
                temperatura = response.toDouble()

                urlConnection.disconnect()

                withContext(Dispatchers.Main) {
                    val temperaturaTextView = findViewById<TextView>(R.id.temperatura)
                    temperaturaTextView.text = "$temperatura°C"
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toAtras(view: View) {
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
        onBackPressed()
    }

    fun toGrafico(view: View) {
        val intent = Intent(this, Graficas::class.java).apply { }
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
        intent.putExtra("aula", selectedAula)
        startActivity(intent)
    }
}
