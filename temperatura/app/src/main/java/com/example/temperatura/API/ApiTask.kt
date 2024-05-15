package com.example.temperatura.API

import ApiClient.fetchData
import android.os.AsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ApiTask(private val listener: ApiListener) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg urls: String): String? {
        if (urls.isEmpty()) {
            return null
        }

        val urlString = urls[0]
        return fetchData(urlString)
    }

    override fun onPostExecute(result: String?) {
        if (result != null) {
            listener.onApiResponse(result)
        } else {
            listener.onApiError(Exception("No se pudieron obtener datos de la API"))
        }
    }

    private fun fetchData(urlString: String): String? {
        var urlConnection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        var jsonResult: String? = null

        try {
            val url = URL(urlString)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"

            // Leer la respuesta
            val inputStream: InputStream = urlConnection.inputStream
            val buffer = StringBuffer()
            if (inputStream == null) {
                // Manejar el caso donde no hay datos
                return null
            }
            reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                buffer.append(line).append("\n")
            }
            if (buffer.isEmpty()) {
                // Manejar el caso donde el buffer está vacío
                return null
            }
            jsonResult = buffer.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            // Manejar la excepción apropiadamente
        } finally {
            urlConnection?.disconnect()
            try {
                reader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
                // Manejar la excepción apropiadamente
            }
        }
        return jsonResult
    }
}
