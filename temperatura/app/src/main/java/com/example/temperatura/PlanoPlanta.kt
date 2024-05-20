package com.example.temperatura
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class PlanoPlanta : AppCompatActivity() {

    private var username: String? = null
    private var ruta: String? = null

    // HashMap para almacenar las temperaturas por habitación
    private val tempAulas: HashMap<String, Double> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plano_planta)
        username = intent.getStringExtra("username")
        ruta = intent.getStringExtra("ruta")
        // Agrega las temperaturas para cada aula
        tempAulas["aula03"] = 23.2
        tempAulas["aula04"] = 25.1
        tempAulas["aulaAteca"] = 28.8
        tempAulas["aula2"] = 18.6
        tempAulas["aula1"] = 22.0

        // Asigna los colores a las habitaciones basados en la temperatura
        for ((aula, temperatura) in tempAulas) {
            val imageViewHabitacion = findViewById<ImageView>(resources.getIdentifier(aula, "id", packageName))
            imageViewHabitacion.setBackgroundColor(getColorFromTemperature(temperatura))

            val temperatureTextView = findViewById<TextView>(resources.getIdentifier("temperatura$aula", "id", packageName))
            temperatureTextView.text = "${temperatura.toString()} ºC"

        }
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
        startActivity(intent);
    }


    // Método para determinar el color en función de la temperatura
    private fun getColorFromTemperature(temperature: Double): Int {
        return when {
            temperature < 20 -> Color.parseColor("#751C3AFF") // Azul
            temperature in 20.0..24.0 -> Color.parseColor("#7500FF00") // Verde
            else -> Color.parseColor("#75ff0000") // Rojo
        }
    }
}
