package com.example.temperatura
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class PlanoPlanta : AppCompatActivity() {

    // HashMap para almacenar las temperaturas por habitación
    private val tempAulas: HashMap<String, Double> = HashMap()
    // Colores predeterminados
    private var colorFrio = "#1C3AFF"
    private var colorOptimo = "#00FF00"
    private var colorCalor = "#FF0000"
    private var notFrio = 18.5
    private var notCalor = 23.5


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

        // Llamar a la función para consultar la configuración de colores
        obtenerConfiguracionColores()

        // Llamar a la función para consultar la API cuando se crea la actividad
        actualizarInterfaz()
    }

    private fun consultarTemperaturasPorAula() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val urlBase = "http://192.168.18.11:8081/aulas/nombre/"

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

    fun toAula(view: View) {
        val intent = Intent(this, TemperaturaAula::class.java).apply{}
        startActivity(intent);
    }

    private fun obtenerConfiguracionColores() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL("http://192.168.18.11:8081/configuracion/nombre/admin")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                val jsonObject = JSONObject(response.toString())
                colorFrio = jsonObject.getString("colorFrio")
                colorOptimo = jsonObject.getString("colorOptimo")
                colorCalor = jsonObject.getString("colorCalor")
                notFrio = jsonObject.getDouble("notFrio")
                notCalor = jsonObject.getDouble("notCalor")
                urlConnection.disconnect()
                Log.d("ConfiguracionColores", "ColorFrio: $colorFrio, ColorOptimo: $colorOptimo, ColorCalor: $colorCalor")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getColorFromTemperature(temperature: Double): Int {
        return when {
            temperature < 20 -> Color.parseColor(colorTransparente(colorFrio))
            temperature in 20.0..25.70 -> Color.parseColor(colorTransparente(colorOptimo))
            else -> Color.parseColor(colorTransparente(colorCalor))
        }
    }

    fun colorTransparente(color: String, transparency: String = "75"): String {
        // Verificar si el color ya incluye transparencia (#AARRGGBB)
        if (color.length == 9 && color.startsWith("#")) {
            Log.d("ColorTransparente", "Color ya incluye transparencia: $color")
            return color
        }
        // Verificar que el color recibido es un string de 7 caracteres (ej: #RRGGBB)
        if (color.length != 7 || !color.startsWith("#")) {
            Log.e("ColorError", "Color inválido: $color")
            throw IllegalArgumentException("Color debe ser un string en formato #RRGGBB")
        }
        return "#$transparency${color.substring(1)}"
    }
}
