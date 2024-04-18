package Config

import android.content.Context
import android.content.SharedPreferences
import android.media.session.MediaSession.Token
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Timer
import java.util.TimerTask

class Config {
    var urlBase = "http://192.168.137.1:8000/api/v01/"

    private val timer = Timer()
    private var primeroInicio = true

    companion object{
        var acceso = ""
        var actualizar = ""
        var foto = ""
        lateinit var prefs: SharedPreferences
    }

    fun startTokenRefreshTimer(context: Context) {
        val task = object: TimerTask() {
            override fun run() {
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        if (!primeroInicio) {
                            refreshToken(context)
                        } else {
                            primeroInicio = false
                        }
                    } catch (error:Exception) {
                        println(error)
                    }
                }
            }
        }
        timer.schedule(task, 0, 1 * 60 * 1000) // 10 minutos = 10 * 60 * 1000 milisegundos
    }

    fun endTokenRefresh() {
        acceso = ""
        actualizar = ""
        timer.cancel()
        timer.purge()
    }

    private suspend fun refreshToken(context: Context) {
        var queue = Volley.newRequestQueue(context)
        var datos = JSONObject().apply {
            put("refresh", actualizar)
        }
        var request = JsonObjectRequest(
            Request.Method.POST, "${Config().urlBase}refresh/", datos,
            {response ->

                acceso = response.getString("access")
                actualizar = response.getString("refresh")
                val editor = prefs.edit()
                editor.putString("token", actualizar)
                editor.apply()
            },
            {error ->
                println(error)
            }
        )
        queue.add(request)
    }

}