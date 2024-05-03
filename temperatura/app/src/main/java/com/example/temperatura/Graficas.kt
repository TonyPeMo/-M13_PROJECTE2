package com.example.temperatura

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.echo.holographlibrary.Line
import com.echo.holographlibrary.LineGraph
import com.echo.holographlibrary.LinePoint
import com.echo.holographlibrary.PieGraph
import com.echo.holographlibrary.PieSlice
//import com.example.temperatura.data.RegistroLine
import com.example.temperatura.data.Registro
import com.example.temperatura.databinding.ActivityGraficasBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
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

    //Graficas View
    private  lateinit var binding : ActivityGraficasBinding
    private  lateinit var pieGrafica: PieGraph
    //private  lateinit var lineGrafica: LineGraph
    private var listaRegistrosPastel: ArrayList<Registro> = ArrayList()
    //private var listaRegistrosLine: ArrayList<RegistroLine> = ArrayList()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_graficas)

        // GRAFICAS PASTEL
        binding = ActivityGraficasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pieGrafica = findViewById(R.id.graphPie) as PieGraph

        binding.btnGuardar.setOnClickListener{

            listaRegistrosPastel.add(Registro("reg1", 18.50f, "#006400"))
            listaRegistrosPastel.add(Registro("reg2", 24.50f, "#228B22"))
            listaRegistrosPastel.add(Registro("reg3", 16.50f, "#808000"))
            listaRegistrosPastel.add(Registro("reg4", 20.50f, "#00FF00"))
            listaRegistrosPastel.add(Registro("reg5", 21.10f, "#7FFF00"))
            listaRegistrosPastel.add(Registro("reg6", 18.70f, "#00FF7F"))
        }

        binding.btnGenerar.setOnClickListener { graficarPie() }

/*
        // GRAFICAS LINEA
        binding = ActivityGraficasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lineGrafica = findViewById(R.id.graphLine) as LineGraph

        binding.btnGuardar.setOnClickListener{

            listaRegistrosLine.add(RegistroLine(20.50f,1))
            listaRegistrosLine.add(RegistroLine(22.50f,2))
            listaRegistrosLine.add(RegistroLine(21.50f,3))



        }

        binding.btnGenerar.setOnClickListener { graficarLine() }


        fun datosGrafica(linea: Line, ejeX: Double, ejeY: Double): Line {
            val punto = LinePoint()
            punto.setX(ejeX)
            punto.setY(ejeY)
            linea.addPoint(punto)

            binding.tvPuntos.text = "${binding.tvPuntos.text}\nX: $ejeX, Y:$ejeY"

            return linea
        }

        fun graficar(linea: Line) {
            binding.lineGrafica.addLine(linea)
            binding.lineGrafica.setRangeX(1f, 4f)
            binding.lineGrafica.setRangeY(0f, 10f)
            binding.lineGrafica.lineToFill = 0
        }
*/


        // Inicio
        selectedDateTextView_inicio = findViewById(R.id.textViewDatePicker_inicio)
        selectedTimeTextView_inicio = findViewById(R.id.textViewTimePicker_inicio)

        // Final
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


    fun graficarPie (){
        for (i in 0 until listaRegistrosPastel.size) {
            val rebanada = PieSlice()
            rebanada.color = Color.parseColor(listaRegistrosPastel[i].color)
            rebanada.value = listaRegistrosPastel[i].temperatura.toString().toFloat()
            pieGrafica.addSlice(rebanada)
        }
    }
    fun showDatePicker(view: View) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
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
            // Do something with the selected time
        }

        timePicker.show(supportFragmentManager, "timePicker")
    }

    fun toAtras(view: View) {
        onBackPressed()
    }
}
