package com.example.temperatura

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog

class PantallaInicio : AppCompatActivity() {

    private val randomValues = IntArray(6) { (0..10).random() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_inicio)

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
        startActivity(intent);
    }

    fun toPlanta(view: View) {
        Log.d("PantallaInicio", "Se hizo clic en toPlanta()")
        val intent = Intent(this, PlanoPlanta::class.java)
        startActivity(intent)
    }


    fun toConfiguracion(view: View) {
        val intent = Intent(this, Configuracion::class.java).apply {}
        startActivity(intent);
    }

    private fun showFilteredValues() {
        // Filtrar valores mayores a 5
        val filteredValues = randomValues.filter { it > 5 }

        // Convertir a array de strings para mostrar en el AlertDialog
        val items = filteredValues.map { it.toString() }.toTypedArray()

        // Crear y mostrar el AlertDialog
        AlertDialog.Builder(this)
            .setTitle("Valores mayores a 5")
            .setItems(items) { dialog, which ->
                // Acción opcional al hacer clic en un elemento
            }
            .setNegativeButton("Cerrar") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}