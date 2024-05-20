package com.example.temperatura

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Click : AppCompatActivity(){

    fun toLogin(view: View) {
        val intent = Intent(this, Login::class.java).apply{}
        startActivity(intent);
    }

    fun toPlanta(view: View) {
        val intent = Intent(this, PlanoPlanta::class.java).apply{}
        startActivity(intent);
    }

    fun toGrafico(view: View) {
        val intent = Intent(this, Graficas::class.java).apply{}
        startActivity(intent);
    }

//    fun toConfiguracion(view: View) {
//        val intent = Intent(this, Configuracion::class.java).apply {}
//        startActivity(intent);
//    }

    //fun toNotificacion(view: View) {
    //    val intent = Intent(this, Notificacion::class.java).apply {}
    //    startActivity(intent);
    //}

    fun toInicio(view: View) {
        val intent = Intent(this, PantallaInicio::class.java).apply {}
        startActivity(intent);
    }


    fun toAtras(view: View) {
        onBackPressed()
    }

}