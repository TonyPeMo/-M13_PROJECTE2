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

    private lateinit var binding: ActivityGraficasBinding
    private lateinit var lineGrafica: LineGraph
    private var listaRegistrosLine: ArrayList<RegistroLine> = ArrayList()

    private var username: String? = null
    private var ruta: String? = null

    private var notFrio = 15.0
    private var notCalor = 23.5

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_graficas)
        username = intent.getStringExtra("username")
        ruta = intent.getStringExtra("ruta")
        //Graficos
        binding = ActivityGraficasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        obtenerConfiguracionColores()

        binding.tvPuntos.text = "Días\n"

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

        // Configurar el botón para generar las líneas
        binding.btnGenerar.setOnClickListener {
            consultarRegistros(selectedDateInicio, selectedDateFinal) { registros ->
                runOnUiThread {
                    // Limpiar las líneas anteriores
                    binding.graphLine.removeAllLines()
                    binding.tvPuntos.text = "Días\n"

                    generarLineas(registros)

                    // Configurar el listener para los puntos después de graficar las líneas
                    binding.graphLine.setOnPointClickedListener { lineIndex, pointIndex ->
                        Toast.makeText(
                            this@Graficas,
                            "Linea: $lineIndex, Punto: $pointIndex",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
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

    private fun datosGrafica(linea: Line, fecha: Date? = null, ejeY: Double, texto: Boolean): Line {
        val punto = LinePoint()
        punto.setX(linea.points.size.toDouble()) //
        punto.setY(ejeY)
        linea.addPoint(punto)

        // Si se proporciona la fecha, formatea la fecha y temperatura en texto y agrega al textView
        if (fecha != null && texto) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            val formattedDate = dateFormat.format(fecha)
            val texto = "$formattedDate , ${timeFormat.format(fecha)}h - $ejeY ºC\n"

            binding.tvPuntos.append(texto)
        }

        return linea
    }



    private fun graficarL(linea: Line, rangeXStart: Float, rangeXEnd: Float) {
        binding.graphLine.addLine(linea)
        binding.graphLine.setRangeX(rangeXStart, rangeXEnd)
        binding.graphLine.setRangeY(13f, 29f)
    }

    private fun generarLineas(registros: List<Registro>) {
        val cantidadPuntos = registros.size

        // Genera puntos para la línea API basados en los registros
        var lineaAPI = Line()
        for (registro in registros) {
            val ejeY = registro.temperatura.toDouble()
            lineaAPI = datosGrafica(lineaAPI, registro.fecha, ejeY, true)
        }
        lineaAPI.color = Color.parseColor("#FFFFC107")
        graficarL(lineaAPI, 0f, (cantidadPuntos).toFloat())

        // Segunda línea
        var segundaLinea = Line()
        for (i in 1..cantidadPuntos) {
            val ejeX = 15.0 + i
            val ejeY = notFrio
            segundaLinea = datosGrafica(segundaLinea, null, ejeY, false)
        }
        segundaLinea.color = Color.parseColor("#1C3AFF")
        graficarL(segundaLinea, 0f, (cantidadPuntos).toFloat())

        // Tercera línea
        var terceraLinea = Line()
        for (i in 1..cantidadPuntos) {
            val ejeX = 15.0 + i
            val ejeY = notCalor
            terceraLinea = datosGrafica(terceraLinea, null, ejeY, false)
        }
        terceraLinea.color = Color.parseColor("#ff0000")
        graficarL(terceraLinea, 0f, (cantidadPuntos).toFloat())
    }


    fun showDatePicker(view: View) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()

        datePicker.addOnPositiveButtonClickListener {
            // Handle positive button click and get selected date
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
            // Do something with the selected date
        }

        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun showTimePicker(view: View) {
        val timePicker = MaterialTimePicker.Builder()
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
            // Do something with the selected time
        }

        timePicker.show(supportFragmentManager, "timePicker")
    }

    fun consultarRegistros(fechaInicio: Date, fechaFin: Date, callback: (List<Registro>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formattedFechaInicio = dateFormat.format(fechaInicio)
                val formattedFechaFin = dateFormat.format(fechaFin)

                val url = URL("$ruta/aulas/nombre/A01/registros/fecha?fechaInicio=$formattedFechaInicio&fechaFin=$formattedFechaFin")
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
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
    }
}

//http://192.168.18.11:8081/aulas/nombre/A01/registros/fecha?fechaInicio=2024-04-07%2010:33:00&fechaFin=2024-04-08%2010:33:00
//http://192.168.18.11:8081/configuracion/nombre/admin
//http://192.168.18.11:8081/aulas/nombre/A01/ultimafecha"