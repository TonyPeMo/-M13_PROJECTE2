package com.example.temperatura
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class PlanoPlanta : AppCompatActivity() {

    // HashMap para almacenar las temperaturas por habitación
    private val tempAulas: HashMap<String, Double> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plano_planta)

        // Agrega las temperaturas para cada aula
        tempAulas["A03"] = 0.0
        tempAulas["A04"] = 0.0
        tempAulas["ATECA"] = 0.0
        tempAulas["A02"] = 0.0
        tempAulas["A01"] = 0.0
        consultarTemperaturasPorAula()


/*
        // Asigna los colores a las habitaciones basados en la temperatura
        for ((aula, temperatura) in tempAulas) {
            val imageViewHabitacion = findViewById<ImageView>(resources.getIdentifier(aula, "id", packageName))
            imageViewHabitacion.setBackgroundColor(getColorFromTemperature(temperatura))


            val temperatureTextView = findViewById<TextView>(resources.getIdentifier("temperatura$aula", "id", packageName))
            temperatureTextView.text = "${temperatura.toString()} ºC"

        }
           */
        // Llamar a la función para consultar la API cuando se crea la actividad
        actualizarInterfaz()
    }



    private fun consultarTemperaturasPorAula() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val urlBase = "http://192.168.17.99:8081/aulas/nombre/"

                // Iterar sobre cada aula en el HashMap
                for ((aula, _) in tempAulas) {
                    val url = URL("$urlBase$aula/ultimafecha")

                    // Abrir una conexión HTTP
                    val urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "GET"

                    // Leer la respuesta
                    val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    var temperaturaString = bufferedReader.readLine()

                    // Convertir la cadena de temperatura a un valor Double
                    val temperatura = temperaturaString.toDouble()

                    // Actualizar el HashMap con la temperatura más reciente
                    tempAulas[aula] = temperatura

                    // Cerrar la conexión
                    urlConnection.disconnect()
                }

                // Actualizar la interfaz de usuario con las nuevas temperaturas
                runOnUiThread {
                    actualizarInterfaz()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Manejar cualquier excepción que ocurra durante la consulta
            }
        }
    }
    private fun actualizarInterfaz() {
        // Iterar sobre el HashMap y actualizar las temperaturas en la interfaz de usuario
        for ((aula, temperatura) in tempAulas) {
            val temperatureTextView = findViewById<TextView>(resources.getIdentifier("temperatura$aula", "id", packageName))
            temperatureTextView.text = "${temperatura.toString()} ºC"

            // Actualizar el color de fondo basado en la temperatura
            val imageViewHabitacion = findViewById<ImageView>(resources.getIdentifier(aula, "id", packageName))
            imageViewHabitacion.setBackgroundColor(getColorFromTemperature(temperatura))

        }
    }

    fun toAtras(view: View) {
        onBackPressed()
    }

    fun toGrafico(view: View) {
        val intent = Intent(this, Graficas::class.java).apply{}
        startActivity(intent);
    }


    // Método para determinar el color en función de la temperatura
    private fun getColorFromTemperature(temperature: Double): Int {
        return when {
            temperature < 20 -> Color.parseColor("#751C3AFF") // Azul
            temperature in 20.0..25.70 -> Color.parseColor("#7500FF00") // Verde
            else -> Color.parseColor("#75ff0000") // Rojo
        }
    }
}
