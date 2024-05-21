package com.example.temperatura

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.echo.holographlibrary.Line
import com.echo.holographlibrary.LineGraph
import com.echo.holographlibrary.LinePoint
import com.example.temperatura.data.RegistroLine
import com.example.temperatura.data.Registro
import com.example.temperatura.databinding.ActivityGraficasBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import kotlin.collections.ArrayList

class Graficas : AppCompatActivity() {

    private lateinit var selectedDateTextView_inicio: TextView
    private lateinit var selectedTimeTextView_inicio: TextView
    private lateinit var selectedDateTextView_final: TextView
    private lateinit var selectedTimeTextView_final: TextView

    private lateinit var selectedDateInicio: Date
    private lateinit var selectedTimeInicio: Date
    private lateinit var selectedDateFinal: Date
    private lateinit var selectedTimeFinal: Date

    //Graficas View
    private var cantidadPuntos: Int = 10
    private lateinit var binding: ActivityGraficasBinding
    private lateinit var lineGrafica: LineGraph
    private var listaRegistrosLine: ArrayList<RegistroLine> = ArrayList()

    private var notFrio = 15.0
    private var notCalor = 23.5

    //Prueba json api
    val registros = listOf(
        Registro(registro = 91, temperatura = 19.5f, fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-04-08 08:33:00"), aulaId = 1),
        Registro(registro = 92, temperatura = 20.0f, fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-04-08 08:30:00"), aulaId = 1),
        Registro(registro = 93, temperatura = 18.5f, fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-04-08 07:33:00"), aulaId = 1),
        Registro(registro = 94, temperatura = 19.0f, fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-04-08 07:31:00"), aulaId = 1)
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraficasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        obtenerConfiguracionColores()
        binding.tvPuntos.text = "Días\n"

        // Genera puntos para la línea
        var linea = Line()
        for (i in 1..cantidadPuntos) {
            var ejeX = 15.0 + i
            val ejeY = String.format("%.1f", Random.nextDouble(15.5, 24.8)).toDouble()
            linea = datosGrafica(linea, ejeX, ejeY, false)
        }
        linea.color = Color.parseColor("#FFBB33")

        graficarL(linea)

        // Genera puntos para la línea API basados en los registros
        var lineaAPI = Line()
        var ejeX = 15.0
        for (registro in registros) {
            val ejeY = registro.temperatura.toDouble()
            lineaAPI = datosGrafica(lineaAPI, ejeX, ejeY, true)
            ejeX += 1.0
        }
        lineaAPI.color = Color.parseColor("#FF0000")

        graficarL(lineaAPI)

        // Segunda línea
        var segundaLinea = Line()
        for (i in 1..cantidadPuntos) {
            var ejeX = 15.0 + i
            val ejeY = notFrio
            segundaLinea = datosGrafica(segundaLinea, ejeX, ejeY, false)
        }
        segundaLinea.color = Color.parseColor("#1C3AFF")
        graficarL(segundaLinea)

        // Tercera línea
        var terceraLinea = Line()
        for (i in 1..cantidadPuntos) {
            var ejeX = 15.0 + i
            val ejeY = notCalor
            terceraLinea = datosGrafica(terceraLinea, ejeX, ejeY, false)
        }
        terceraLinea.color = Color.parseColor("#ff0000")
        graficarL(terceraLinea)

        binding.graphLine.setOnPointClickedListener { lineIndex, pointIndex ->
            Toast.makeText(
                this@Graficas,
                "Linea: $lineIndex, Punto: $pointIndex",
                Toast.LENGTH_LONG
            ).show()
        }

        // Fecha Inicio
        selectedDateTextView_inicio = findViewById(R.id.textViewDatePicker_inicio)
        selectedTimeTextView_inicio = findViewById(R.id.textViewTimePicker_inicio)

        // Fecha Final
        selectedDateTextView_final = findViewById(R.id.textViewDatePicker_final)
        selectedTimeTextView_final = findViewById(R.id.textViewTimePicker_final)

        // Establecer la fecha y hora por defecto para el inicio y el final
        val currentDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        // Solo establecer la fecha y la hora por defecto en la fecha de inicio
        selectedDateTextView_inicio.text = "Select date"
        selectedTimeTextView_inicio.text = "08:00"

        // Establecer la fecha y la hora por defecto en la fecha final
        selectedDateTextView_final.text = dateFormat.format(currentDate.time)
        selectedTimeTextView_final.text = timeFormat.format(currentDate.time)

        selectedDateInicio = Date()
        selectedTimeInicio = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 8); set(Calendar.MINUTE, 0) }.time

        selectedDateFinal = currentDate.time
        selectedTimeFinal = currentDate.time
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

                // Procesar el JSON de respuesta
                val jsonObject = JSONObject(response.toString())
                notFrio = jsonObject.getDouble("notFrio")
                notCalor = jsonObject.getDouble("notCalor")

                // Cerrar la conexión
                urlConnection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Función para agregar puntos a la línea
    private fun datosGrafica(linea: Line, ejeX: Double, ejeY: Double, texto: Boolean): Line {
        val punto = LinePoint()
        punto.setX(ejeX)
        punto.setY(ejeY)
        linea.addPoint(punto)

        if (texto) {
            binding.tvPuntos.text = "${binding.tvPuntos.text}\n15/05/2024 - Temperatura: $ejeY º"
        }
        return linea
    }

    fun graficarL(linea: Line) {
        binding.graphLine.addLine(linea)
        binding.graphLine.setRangeX(15f, 25f)
        binding.graphLine.setRangeY(14.5f, 26f)
    }

    fun showDatePicker(view: View) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val selectedDateInMillis = it
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selectedDateInMillis
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)
            if (view.id == R.id.textViewDatePicker_inicio) {
                selectedDateTextView_inicio.text = formattedDate
                selectedDateInicio = calendar.time
            } else {
                selectedDateTextView_final.text = formattedDate
                selectedDateFinal = calendar.time
            }
        }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun showTimePicker(view: View) {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Select time")
                .build()

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val selectedTime = timeFormat.parse(String.format("%02d:%02d", hour, minute))!!
            if (view.id == R.id.textViewTimePicker_inicio) {
                selectedTimeTextView_inicio.text = String.format("%02d:%02d", hour, minute)
                selectedTimeInicio = selectedTime
            } else {
                selectedTimeTextView_final.text = String.format("%02d:%02d", hour, minute)
                selectedTimeFinal = selectedTime
            }
        }
        timePicker.show(supportFragmentManager, "timePicker")
    }

    fun consultarRegistros(fechaInicio: Date, fechaFin: Date, callback: (List<Registro>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formattedFechaInicio = dateFormat.format(fechaInicio)
                val formattedFechaFin = dateFormat.format(fechaFin)

                val url = URL("http://192.168.18.11:8081/aulas/nombre/A01/registros/fecha?fechaInicio=$formattedFechaInicio&fechaFin=$formattedFechaFin")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"

                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                // Procesar el JSON de respuesta
                val jsonArray = JSONArray(response.toString())
                val registros = mutableListOf<Registro>()
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val registro = jsonObject.getInt("idRegistro")
                    val temperatura = jsonObject.getDouble("temperatura")
                    val fechaStr = jsonObject.getString("fecha")
                    val fecha = dateFormat.parse(fechaStr)
                    val aulaId = jsonObject.getInt("aulaId")
                    registros.add(Registro(registro, temperatura.toFloat(), fecha, aulaId))
                }

                // Llamar al callback con los registros obtenidos
                callback(registros)

                // Cerrar la conexión
                urlConnection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                // Manejar errores aquí, por ejemplo, notificar al usuario
            }
        }
    }

    fun toAtras(view: View) {
        onBackPressed()
    }
}
