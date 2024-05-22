package com.example.temperatura

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class PantallaInicio : AppCompatActivity() {

    private val aulas = listOf("A03", "A04", "ATECA", "A02", "A01")
    private var tFrio: Float = 0f
    private var tCalor: Float = 0f
    private var username: String? = null
    private var ruta: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_inicio)
        username = intent.getStringExtra("username")
        ruta = intent.getStringExtra("ruta")

        val notificaciones = findViewById<ImageView>(R.id.notificaciones)
        notificaciones.setOnClickListener {
            showFilteredValues()
        }
    }

    fun toLogin(view: View) {
        val intent = Intent(this, Login::class.java).apply{}
        startActivity(intent);
    }

    fun toGrafico(view: View) {
        val intent = Intent(this, Graficas::class.java).apply{}
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
        startActivity(intent);
    }

    fun toPlanta(view: View) {
        Log.d("PantallaInicio", "Se hizo clic en toPlanta()")
        val intent = Intent(this, PlanoPlanta::class.java)
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
        startActivity(intent)
    }

    fun toConfiguracion(view: View) {
        val intent = Intent(this, Configuracion::class.java).apply {}
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
        startActivity(intent);
    }

    private fun showFilteredValues() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Obtener la configuración de temperatura
                val configUrl = URL("http://192.168.18.11:8081/configuracion/nombre/admin")
                val configUrlConnection = configUrl.openConnection() as HttpURLConnection
                configUrlConnection.requestMethod = "GET"

                val configBufferedReader = BufferedReader(InputStreamReader(configUrlConnection.inputStream))
                val configResponse = StringBuilder()
                var configLine: String?
                while (configBufferedReader.readLine().also { configLine = it } != null) {
                    configResponse.append(configLine)
                }

                // Procesar el JSON de respuesta de la configuración
                val configJsonObject = JSONObject(configResponse.toString())
                Log.d("PantallaInicio", "Respuesta del servidor: ${configResponse.toString()}")
                tFrio = configJsonObject.getDouble("notFrio").toFloat()
                tCalor = configJsonObject.getDouble("notCalor").toFloat()

                // Log para verificar los parámetros recibidos
                Log.d("PantallaInicio", "tFrio: $tFrio, tCalor: $tCalor (${tCalor::class.simpleName})")


                // Cerrar la conexión de configuración
                configUrlConnection.disconnect()

                // Obtener los valores de temperatura de las aulas
                val filteredAulas = mutableListOf<String>()

                for (aula in aulas) {
                    val url = URL("http://192.168.18.11:8081/aulas/nombre/$aula/ultimafecha")
                    val urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "GET"

                    Log.d("PantallaInicio", "1")

                    val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (bufferedReader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    Log.d("PantallaInicio", "2")

                    // Procesar el JSON de respuesta de temperatura del aula
                    val temperatura = response.toString().toDouble()

                    // Agregar el aula a la lista si está por encima de tCalor o por debajo de tFrio
                    if (temperatura > tCalor || temperatura < tFrio) {
                        filteredAulas.add(aula)
                        Log.d("PantallaInicio", "Aula agregada a filteredAulas: $aula")
                    }

                    // Cerrar la conexión del aula
                    urlConnection.disconnect()
                }

                runOnUiThread {
                    showAulas(filteredAulas)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showAulas(aulas: List<String>) {
        val items = aulas.toTypedArray()

        // Crear y mostrar el AlertDialog
        AlertDialog.Builder(this@PantallaInicio)
            .setTitle("Aulas fuera de la temperatura establecida:")
            .setItems(items) { dialog, which ->
                // Acción opcional al hacer clic en un elemento
            }
            .setNegativeButton("Cerrar") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}
