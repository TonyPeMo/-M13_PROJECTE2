package com.example.temperatura


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var baseDatos: BaseDatosAPP
    private lateinit var valores: ValoresDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_land)
        valores = ValoresDatabase()

        baseDatos = BaseDatosAPP(this, "temperaturaDB", null, 1)

        val btnConfiguracion: Button = findViewById(R.id.btnConfiguracion)

        btnConfiguracion.setOnClickListener {
            val intent = Intent(this, Configuracion::class.java)
            startActivity(intent)
        }

        val btnConfiguracion2: Button = findViewById(R.id.login)

        btnConfiguracion2.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        val btnConfiguracion3: Button = findViewById(R.id.pantallainicio)

        btnConfiguracion3.setOnClickListener {
            val intent = Intent(this, PantallaInicio::class.java)
            startActivity(intent)
        }

        val btnPlanta: Button = findViewById(R.id.btn_planta)

        btnPlanta.setOnClickListener {
            val intent = Intent(this, PlanoPlanta::class.java)
            startActivity(intent)
        }

        val btnConfiguracion4: Button = findViewById(R.id.graficas)

        btnConfiguracion4.setOnClickListener {
            val intent = Intent(this, Graficas::class.java)
            startActivity(intent)
        }

        val btnConfiguracion5: Button = findViewById(R.id.temperaturaAula)

        btnConfiguracion5.setOnClickListener {
            val intent = Intent(this, TemperaturaAula::class.java)
            startActivity(intent)
        }
    }
}
