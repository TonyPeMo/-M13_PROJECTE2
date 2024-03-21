package com.example.temperatura

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class Graficas : AppCompatActivity() {

    private lateinit var selectedDateTextView_inicio: TextView
    private lateinit var selectedTimeTextView_inicio: TextView
    private lateinit var selectedDateTextView_final: TextView
    private lateinit var selectedTimeTextView_final: TextView

    private lateinit var selectedDateInicio: Date
    private lateinit var selectedTimeInicio: Date

    private lateinit var selectedDateFinal: Date
    private lateinit var selectedTimeFinal: Date

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graficas)

        // Inicio
        selectedDateTextView_inicio = findViewById(R.id.textViewDatePicker_inicio)
        selectedTimeTextView_inicio = findViewById(R.id.textViewTimePicker_inicio)

        // Final
        selectedDateTextView_final = findViewById(R.id.textViewDatePicker_final)
        selectedTimeTextView_final = findViewById(R.id.textViewTimePicker_final)

        // Establecer la fecha y hora por defecto para el inicio y el final
        val currentDate = Calendar.getInstance().time
        selectedDateTextView_inicio.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(currentDate)
        selectedTimeTextView_inicio.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentDate)
        selectedDateTextView_final.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(currentDate)
        selectedTimeTextView_final.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentDate)

        selectedDateInicio = currentDate
        selectedTimeInicio = currentDate
        selectedDateFinal = currentDate
        selectedTimeFinal = currentDate
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
}
