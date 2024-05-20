package com.example.temperatura

import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.temperatura.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import yuku.ambilwarna.AmbilWarnaDialog


class Configuracion : AppCompatActivity() {

    private lateinit var username: String
    private lateinit var ruta: String
    private var defaultColor: Int = 0
    private lateinit var imagenFrio: ImageView
    private lateinit var imagenOptimo: ImageView
    private lateinit var imagenCalor: ImageView
    private var colorFrio: Int = 0
    private var colorOptimo: Int = 0
    private var colorCalor: Int = 0
    private var isUpdatingText = false
    private var isUpdatingText2 = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        // Obtener datos de la intención
        username = intent.getStringExtra("username") ?: ""
        ruta = intent.getStringExtra("ruta") ?: ""

        // Inicializar vistas y establecer colores por defecto
        // Inicializa tus vistas
        imagenFrio = findViewById(R.id.lapiz_color_frio)
        imagenOptimo = findViewById(R.id.lapiz_color_optimo)
        imagenCalor = findViewById(R.id.lapiz_color_calor)

        // Realizar la solicitud HTTP y mostrar los datos
        obtenerDatosConfiguracion()


        // Establecer listeners para las imágenes
        imagenFrio.setOnClickListener {
            defaultColor = colorFrio
            openColorPicker(imagenFrio)
        }
        imagenOptimo.setOnClickListener {
            defaultColor = colorOptimo
            openColorPicker(imagenOptimo)
        }
        imagenCalor.setOnClickListener {
            defaultColor = colorCalor
            openColorPicker(imagenCalor)
        }

        // Obtén referencias a los TextInputEditTexts dentro de los TextInputLayouts
        val tminOptimoTextInputLayout = findViewById<TextInputLayout>(R.id.TminNotificacionesOptimo_mapa_de_calor)
        val tminOptimoEditText = tminOptimoTextInputLayout.editText
        val tmaxFrioTextInputLayout = findViewById<TextInputLayout>(R.id.TmaxNotificacionesFrio_mapa_de_calor)
        val tmaxFrioEditText = tmaxFrioTextInputLayout.editText
        val tmaxCalorTextInputLayout = findViewById<TextInputLayout>(R.id.TmaxNotificacionesCalor_mapa_de_calor)
        val tmaxCalorEditText = tmaxCalorTextInputLayout.editText
        val tmaxOptimoTextInputLayout = findViewById<TextInputLayout>(R.id.TmaxNotificacionesOptimo_mapa_de_calor)
        val tmaxOptimoEditText = tmaxOptimoTextInputLayout.editText


        // Establece listeners de texto para los campos
        tminOptimoEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdatingText) {
                    isUpdatingText = true
                    // Si se edita el campo de TminNotificacionesOptimo_mapa_de_calor, actualiza TmaxNotificacionesFrio_mapa_de_calor
                    tmaxFrioEditText?.setText(s.toString())
                    isUpdatingText = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita implementar
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se necesita implementar
            }
        })

        tmaxFrioEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdatingText) {
                    isUpdatingText = true
                    // Si se edita el campo de TmaxNotificacionesFrio_mapa_de_calor, actualiza TminNotificacionesOptimo_mapa_de_calor
                    tminOptimoEditText?.setText(s.toString())
                    isUpdatingText = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita implementar
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se necesita implementar
            }
        })

        // Establece listeners de texto para los campos
        tmaxCalorEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdatingText2) {
                    isUpdatingText2 = true
                    // Si se edita el campo de TminNotificacionesOptimo_mapa_de_calor, actualiza TmaxNotificacionesFrio_mapa_de_calor
                    tmaxOptimoEditText?.setText(s.toString())
                    isUpdatingText2 = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita implementar
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se necesita implementar
            }
        })

        tmaxOptimoEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdatingText2) {
                    isUpdatingText2 = true
                    // Si se edita el campo de TmaxNotificacionesFrio_mapa_de_calor, actualiza TminNotificacionesOptimo_mapa_de_calor
                    tmaxCalorEditText?.setText(s.toString())
                    isUpdatingText2 = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita implementar
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se necesita implementar
            }
        })

    }


    private fun obtenerDatosConfiguracion() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val url = "$ruta/configuracion/nombre/$username"
                val request = Request.Builder()
                    .url(url)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val responseData = response.body?.string()
                    responseData?.let { mostrarDatos(JSONObject(it)) }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
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
                    // Cambia el color de fondo del óvalo
                    imagen.setBackgroundColor(color)
                    // Actualiza el color en la variable global según la imagen
                    when (imagen) {
                        imagenFrio -> colorFrio = color
                        imagenOptimo -> colorOptimo = color
                        imagenCalor -> colorCalor = color
                    }
                }
            })
        ambilWarnaDialog.show()
    }

    private fun mostrarDatos(json: JSONObject) {
        colorFrio = Color.parseColor(json.getString("colorFrio"))
        colorOptimo = Color.parseColor(json.getString("colorOptimo"))
        colorCalor = Color.parseColor(json.getString("colorCalor"))
        val notFrio = json.getDouble("notFrio")
        val notCalor = json.getDouble("notCalor")
        val tFrio = json.getDouble("tFrio")
        val tOptimaMin = json.getDouble("tOptimaMin")
        val tOptimaMax = json.getDouble("tOptimaMax")
        val tCalor = json.getDouble("tCalor")

        // Actualizar las vistas con los datos obtenidos
        Log.d("Configuracion", "Datos: $colorFrio, $colorOptimo, $colorCalor, $notFrio, $notCalor, $tFrio, $tOptimaMin, $tOptimaMax, $tCalor")


        // Actualizar las vistas con los datos obtenidos
        runOnUiThread {
            // Actualizar el valor de TmaxNotificacionesFrio_mapa_de_calor
            val textInputLayoutTmaxNotificacionesFrioMapaDeCalor = findViewById<TextInputLayout>(R.id.TmaxNotificacionesFrio_mapa_de_calor)
            val textInputTmaxNotificacionesFrioMapaDeCalor = textInputLayoutTmaxNotificacionesFrioMapaDeCalor.editText
            textInputTmaxNotificacionesFrioMapaDeCalor?.setText(tFrio.toString())


            // Actualizar el valor de TmaxOptimoMapa
            val textInputLayoutTmaxNotificacionesOptimoMapaDeCalor = findViewById<TextInputLayout>(R.id.TmaxNotificacionesOptimo_mapa_de_calor)
            val textInputTmaxOptimoMapa = textInputLayoutTmaxNotificacionesOptimoMapaDeCalor.editText
            textInputTmaxOptimoMapa?.setText(tOptimaMax.toString())

            // Actualizar el valor de TminOptimoMapa
            val textInputLayoutTminNotificacionesOptimoMapaDeCalor = findViewById<TextInputLayout>(R.id.TminNotificacionesOptimo_mapa_de_calor)
            val textInputTminOptimoMapa = textInputLayoutTminNotificacionesOptimoMapaDeCalor.editText
            textInputTminOptimoMapa?.setText(tOptimaMin.toString())


            // Actualizar el valor de TmaxNotificacionesCalor_mapa_de_calor
            val textInputLayoutTmaxNotificacionesCalorMapaDeCalor = findViewById<TextInputLayout>(R.id.TmaxNotificacionesCalor_mapa_de_calor)
            val textInputTmaxNotificacionesCalorMapaDeCalor = textInputLayoutTmaxNotificacionesCalorMapaDeCalor.editText
            textInputTmaxNotificacionesCalorMapaDeCalor?.setText(tCalor.toString())

            // Actualizar el valor de TmaxNotificacionesFrio
            val LayouttextInputTmaxNotificacionesFrio = findViewById<TextInputLayout>(R.id.TmaxNotificacionesFrio)
            val textInputTmaxNotificacionesFrio = LayouttextInputTmaxNotificacionesFrio.editText
            textInputTmaxNotificacionesFrio?.setText(notFrio.toString())

            // Actualizar el valor de TmaxNotificacionesCalor
            val LayouttextInputTmaxNotificacionesCalor = findViewById<TextInputLayout>(R.id.TmaxNotificacionesCalor)
            val textInputTmaxNotificacionesCalor = LayouttextInputTmaxNotificacionesCalor.editText
            textInputTmaxNotificacionesCalor?.setText(notCalor.toString())


            imagenFrio.setBackgroundColor(colorFrio)
            imagenOptimo.setBackgroundColor(colorOptimo)
            imagenCalor.setBackgroundColor(colorCalor)

            val buttonGuardar = findViewById<Button>(R.id.buttonGuardar)
            buttonGuardar.setOnClickListener {
                putConfiguracion()
            }
        }
    }

    fun putConfiguracion() {
        val client = OkHttpClient()

        // Obtener los valores de las vistas
        val colorFrio = obtenerColorDesdeVista(R.id.lapiz_color_frio)
        val colorOptimo = obtenerColorDesdeVista(R.id.lapiz_color_optimo)
        val colorCalor = obtenerColorDesdeVista(R.id.lapiz_color_calor)
        val notFrio = obtenerValorDesdeVista(R.id.TmaxNotificacionesFrio)
        val notCalor = obtenerValorDesdeVista(R.id.TmaxNotificacionesCalor)
        val tFrio = obtenerValorDesdeVista(R.id.TmaxNotificacionesFrio_mapa_de_calor)
        val tOptimaMin = obtenerValorDesdeVista(R.id.TminNotificacionesOptimo_mapa_de_calor)
        val tOptimaMax = obtenerValorDesdeVista(R.id.TmaxNotificacionesOptimo_mapa_de_calor)
        val tCalor = obtenerValorDesdeVista(R.id.TmaxNotificacionesCalor_mapa_de_calor)

        // Crear el objeto JSON con los datos que deseas enviar
        val json = JSONObject().apply {
            put("colorFrio", colorFrio)
            put("colorOptimo", colorOptimo)
            put("colorCalor", colorCalor)
            put("notFrio", notFrio)
            put("notCalor", notCalor)
            put("tFrio", tFrio)
            put("tOptimaMin", tOptimaMin)
            put("tOptimaMax", tOptimaMax)
            put("tCalor", tCalor)
        }
        Log.d("Configuracion", "Datos: $json")

        // Crear el cuerpo de la solicitud con el objeto JSON
        // Creas el MediaType adecuado para JSON
        val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

        // Conviertes el objeto JSON a una cadena y luego a un RequestBody
        val requestBody = json.toString().toRequestBody(JSON)

        // Crear la solicitud PUT
        val request = Request.Builder()
            .url("$ruta/configuracion/nombre/$username")
            .put(requestBody)
            .build()

        // Ejecutar la solicitud en un hilo de fondo
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Manejar la falla de la solicitud
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                // Manejar la respuesta de la solicitud
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                } else {
                    val responseBody = response.body?.string()

                    // Mostrar el Toast indicando que se ha guardado la configuración en el hilo principal
                    runOnUiThread {
                        Toast.makeText(this@Configuracion, "Configuración guardada", Toast.LENGTH_SHORT).show()
                    }

                    println(responseBody)
                }
            }
        })
    }


    private fun obtenerValorDesdeVista(id: Int): Double {
        val textInputLayout = findViewById<TextInputLayout>(id)
        val textInputEditText = textInputLayout.editText
        return textInputEditText?.text.toString().toDoubleOrNull() ?: 0.0
    }

    private fun obtenerColorDesdeVista(id: Int): String {
        val imageView = findViewById<ImageView>(id)
        val colorDrawable = imageView.background
        return if (colorDrawable is ColorDrawable) {
            val color = colorDrawable.color
            String.format("#%06X", 0xFFFFFF and color)
        } else {
            // Si no se puede obtener el color, devuelve un color por defecto o maneja el caso según tu necesidad.
            "#FFFFFF"
        }
    }


    fun toAtras(view: View) {
        onBackPressed()
        intent.putExtra("username", username)
        intent.putExtra("ruta", ruta)
    }


}
