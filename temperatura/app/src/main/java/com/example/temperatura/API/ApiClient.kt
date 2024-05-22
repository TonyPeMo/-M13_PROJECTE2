import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ApiClient {
    fun fetchData(urlString: String?): String? {
        var urlConnection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        var jsonResult: String? = null
        try {
            val url = URL(urlString)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"

            // Leer la respuesta
            val inputStream = urlConnection!!.inputStream
            val buffer = StringBuilder()
            if (inputStream == null) {
                // Manejar el caso donde no hay datos
                return null
            }
            reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                buffer.append(line).append("\n")
            }
            if (buffer.length == 0) {
                // Manejar el caso donde el buffer está vacío
                return null
            }
            jsonResult = buffer.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            // Manejar la excepción apropiadamente
        } finally {
            urlConnection?.disconnect()
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    // Manejar la excepción apropiadamente
                }
            }
        }
        return jsonResult
    }
}