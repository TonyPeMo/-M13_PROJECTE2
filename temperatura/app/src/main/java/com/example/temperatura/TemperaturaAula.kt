package com.example.temperatura

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TemperaturaAula : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_temperatura_aula)

        val temperaturaActual = obtenerTemperaturaActual()
        val temperaturaMinima = obtenerTemperaturaMinima()
        val temperaturaMaxima = obtenerTemperaturaMaxima()

        val textColorTemperaturaActual = Color.parseColor("#FFA51E")
        val textColorTemperaturaMinima = Color.parseColor("#0500FF")
        val textColorTemperaturaMaxima = Color.parseColor("#FF0000")

        val textoTemperaturaActual = "• Temperatura actual: ${temperaturaActual}"
        val textoTemperaturaMinima = "• Temperatura mínima: ${temperaturaMinima}"
        val textoTemperaturaMaxima = "• Temperatura máxima: ${temperaturaMaxima}"

        val textViewTemperaturaActual = findViewById<TextView>(R.id.textViewTemperaturaActual)
        val textViewTemperaturaMinima = findViewById<TextView>(R.id.textViewTemperaturaMinima)
        val textViewTemperaturaMaxima = findViewById<TextView>(R.id.textViewTemperaturaMaxima)

        textViewTemperaturaActual.text = applyColorToString(textoTemperaturaActual, textColorTemperaturaActual)
        textViewTemperaturaMinima.text = applyColorToString(textoTemperaturaMinima, textColorTemperaturaMinima)
        textViewTemperaturaMaxima.text = applyColorToString(textoTemperaturaMaxima, textColorTemperaturaMaxima)



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
        return "23º" // Simulado, debes reemplazar con tu lógica real
    }

    private fun obtenerTemperaturaMinima(): String {
        return "17º" // Simulado, debes reemplazar con tu lógica real
    }

    private fun obtenerTemperaturaMaxima(): String {
        return "27º" // Simulado, debes reemplazar con tu lógica real
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
}
