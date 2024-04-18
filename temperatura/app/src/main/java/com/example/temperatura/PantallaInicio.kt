package com.example.temperatura

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class PantallaInicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_inicio)
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
}