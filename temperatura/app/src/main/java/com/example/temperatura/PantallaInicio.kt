package com.example.temperatura

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class PantallaInicio : AppCompatActivity() {
    private var username: String? = null
    private var ruta: String? = null
    private val CHANNEL_ID = "mi_canal_id"
    private val NOTIFICATION_ID = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_inicio)
        username = intent.getStringExtra("username")
        ruta = intent.getStringExtra("ruta")

        // Crea el canal de notificación (solo necesario en Android Oreo y superior)
        createNotificationChannel()

        // Configura el intent para la acción de clic en la notificación
        val intent = Intent(this, Configuracion::class.java) // Cambiar OtraActividad por la actividad que deseas abrir
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Crea la notificación
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_notificaciones)
            .setContentTitle("Título de la notificación")
            .setContentText("Contenido de la notificación")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Cierra la notificación cuando se hace clic en ella

        // Muestra la notificación
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun toLogin(view: View) {
        val intent = Intent(this, Login::class.java).apply{}
        startActivity(intent);
    }

    fun toGrafico(view: View) {
        val intent = Intent(this, Graficas::class.java).apply{}
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
        startActivity(intent);
    }

    fun toPlanta(view: View) {
        Log.d("PantallaInicio", "Se hizo clic en toPlanta()")
        val intent = Intent(this, PlanoPlanta::class.java)
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
        startActivity(intent)
    }


    fun toConfiguracion(view: View) {
        val intent = Intent(this, Configuracion::class.java).apply {}
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
        startActivity(intent);
    }
}

