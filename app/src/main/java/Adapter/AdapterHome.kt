package Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.parser.ColorParser
import com.bumptech.glide.Glide
import com.example.saddwy.Cuestionario
import com.example.saddwy.Principal
import com.example.saddwy.R
import com.example.saddwy.Recuperar_Correo
import org.json.JSONArray
import org.json.JSONObject

class AdapterHome(private val context: Context, private val jsonObject: JSONArray) :
    RecyclerView.Adapter<AdapterHome.MyHolder>() {

    inner class MyHolder(item: View) : RecyclerView.ViewHolder(item) {
        val imagen: ImageView = itemView.findViewById(R.id.imagen_start)
        val titulo: TextView = itemView.findViewById(R.id.titulo_start)
        val btnNivel1: Button = itemView.findViewById(R.id.btn_card1)
        val btnNivel2: Button = itemView.findViewById(R.id.btn_card2)
        val btnNivel3: Button = itemView.findViewById(R.id.btn_card3)
        val btnDocumentacion: Button = itemView.findViewById(R.id.btn_documentacion)
        var id_linear: LinearLayout = itemView.findViewById(R.id.id_Linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHome.MyHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false)
        return MyHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterHome.MyHolder, position: Int) {
        val lenguaje = jsonObject.getJSONObject(position)



            val color = lenguaje.getJSONObject("color")
            val fondo = color.getString("componente")
            try {
                val colorInt = Color.parseColor(fondo)
                holder.id_linear.setBackgroundColor(colorInt)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace() // Manejar la excepción si el color no está en un formato válido
            }



        // Cargar la imagen utilizando Glide
        Glide.with(context)
            .load(lenguaje.getString("logo"))
            .into(holder.imagen)



        holder.titulo.text = lenguaje.getString("nombre")

        // Configurar el evento click para el botón de documentación
        holder.btnDocumentacion.setOnClickListener {
            val urlDocumentacion = lenguaje.getString("urlDocumentation")
            // Abrir la URL de documentación en un navegador
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlDocumentacion))
            context.startActivity(intent)
        }

        // Configurar los eventos click para los botones de niveles
        val niveles = lenguaje.getJSONArray("niveles")
        for (i in 0 until niveles.length()) {
            val nivel = niveles.getJSONObject(i)
            val btnIndex = i % 3  // Calcula el índice del botón dentro del ciclo (0, 1 o 2)
            val btnNivel = when (btnIndex) {
                0 -> holder.btnNivel1
                1 -> holder.btnNivel2
                2 -> holder.btnNivel3
                else -> null
            }
            btnNivel?.apply {
                text = "${nivel.optString("nombre")}: ${nivel.getString("explanation")}"
                setOnClickListener {
                    val intent = Intent(context, Cuestionario::class.java)
                    intent.putExtra("nivel_id", nivel.getInt("id"))
                    context.startActivity(intent)
                    Principal().finish()
                }
            }
        }
    }


    override fun getItemCount(): Int {
        // El número de elementos es igual a la cantidad de claves en el objeto JSON
        return jsonObject.length()
    }
}
