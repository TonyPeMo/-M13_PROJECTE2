package com.example.temperatura


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_land)

        val btnConfiguracion: Button = findViewById(R.id.btnConfiguracion)

        btnConfiguracion.setOnClickListener {
            val intent = Intent(this, Configuracion::class.java)
            startActivity(intent)
        }
    }
}
