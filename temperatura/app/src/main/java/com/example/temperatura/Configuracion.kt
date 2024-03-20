package com.example.temperatura

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import yuku.ambilwarna.AmbilWarnaDialog

class Configuracion : AppCompatActivity() {
    private var defaultColor: Int = 0
    private lateinit var imagenFrio: ImageView
    private lateinit var imagenOptimo: ImageView
    private lateinit var imagenCalor: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        // Inicializa tus vistas
        imagenFrio = findViewById(R.id.lapiz_color_frio)
        imagenOptimo = findViewById(R.id.lapiz_color_optimo)
        imagenCalor = findViewById(R.id.lapiz_color_calor)

        // Define un color por defecto
        defaultColor = ContextCompat.getColor(this, R.color.azul)

        // Establece listeners para las imágenes
        imagenFrio.setOnClickListener { openColorPicker(imagenFrio) }
        imagenOptimo.setOnClickListener { openColorPicker(imagenOptimo) }
        imagenCalor.setOnClickListener { openColorPicker(imagenCalor) }
    }

    private fun openColorPicker(imagen: ImageView) {
        val ambilWarnaDialog = AmbilWarnaDialog(
            this,
            defaultColor,
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog?) {
                    // No es necesario hacer nada si se cancela
                }

                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    defaultColor = color
                    // Cambia el color de fondo del óvalo
                    imagen.background.setColorFilter(defaultColor, PorterDuff.Mode.SRC_ATOP)
                }
            })
        ambilWarnaDialog.show()
    }

    fun toAtras(view: View) {
        onBackPressed()
    }
}