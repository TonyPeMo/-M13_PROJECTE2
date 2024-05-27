package com.example.temperatura
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class PlanoPlanta : AppCompatActivity() {

    private var username: String? = null
    private var ruta: String? = null
    private var selectedAula: String? = "A01"


    // HashMap para almacenar las temperaturas por habitación
    private val tempAulas: HashMap<String, Double> = HashMap()
    // Colores predeterminados
    private var colorFrio = "#1C3AFF"
    private var colorOptimo = "#00FF00"
    private var colorCalor = "#FF0000"
    private var notFrio = 18.5
    private var notCalor = 23.5
    private var tFrio = 23.5
    private var tCalor = 23.5
    private val aulas = listOf("A03", "A04", "ATECA", "A02", "A01")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plano_planta)

        val notificaciones = findViewById<ImageView>(R.id.notificationIcon)
        notificaciones.setOnClickListener {
            showFilteredValues()
        }

        username = intent.getStringExtra("username")
        ruta = intent.getStringExtra("ruta")

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
        val aulaA01 = findViewById<ImageView>(R.id.A01)
        aulaA01.setOnClickListener {
            selectedAula = "A01"
            toAula()
        }
        val aulaA02 = findViewById<ImageView>(R.id.A02)
        aulaA02.setOnClickListener {
            selectedAula = "A02"
            toAula()
        }
        val aulaA03 = findViewById<ImageView>(R.id.A03)
        aulaA03.setOnClickListener {
            selectedAula = "A03"
            toAula()
        }
        val aulaA04 = findViewById<ImageView>(R.id.A04)
        aulaA04.setOnClickListener {
            selectedAula = "A04"
            toAula()
        }
        val aulaATECA = findViewById<ImageView>(R.id.ATECA)
        aulaATECA.setOnClickListener {
            selectedAula = "ATECA"
            toAula()
        }



    }

    private fun consultarTemperaturasPorAula() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val urlBase = "$ruta/aulas/nombre/"

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
                tFrio = configJsonObject.getDouble("notFrio").toFloat().toDouble()
                tCalor = configJsonObject.getDouble("notCalor").toFloat().toDouble()

                // Log para verificar los parámetros recibidos
                Log.d("PantallaInicio", "tFrio: $tFrio, tCalor: $tCalor (${tCalor::class.simpleName})")


                // Cerrar la conexión de configuración
                configUrlConnection.disconnect()

                // Obtener los valores de temperatura de las aulas
                val filteredAulas = mutableListOf<String>()

                for (aula in aulas) {
                    val url = URL("$ruta/aulas/nombre/$aula/ultimafecha")
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
        AlertDialog.Builder(this@PlanoPlanta)
            .setTitle("Aulas fuera de la temperatura establecida:")
            .setItems(items) { dialog, which ->
                // Acción opcional al hacer clic en un elemento
            }
            .setNegativeButton("Cerrar") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    fun toAtras(view: View) {
        onBackPressed()
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
    }

    fun toGrafico(view: View) {
        val intent = Intent(this, Graficas::class.java).apply{}
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
        intent.putExtra("aula", selectedAula)
        startActivity(intent);
    }

    fun toAula() {
        val intent = Intent(this, TemperaturaAula::class.java).apply{}
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
        intent.putExtra("aula", selectedAula)
        startActivity(intent);
    }

    private fun obtenerConfiguracionColores() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$ruta/configuracion/nombre/$username")
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
                tFrio = jsonObject.getDouble("tFrio")
                tCalor = jsonObject.getDouble("tCalor")
                urlConnection.disconnect()
                Log.d("ConfiguracionColores", "ColorFrio: $colorFrio, ColorOptimo: $colorOptimo, ColorCalor: $colorCalor")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getColorFromTemperature(temperature: Double): Int {
        return when {
            temperature < tFrio -> Color.parseColor(colorTransparente(colorFrio))
            temperature in tFrio..tCalor -> Color.parseColor(colorTransparente(colorOptimo))
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