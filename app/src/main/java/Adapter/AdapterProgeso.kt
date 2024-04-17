package Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.saddwy.R
import org.json.JSONArray

class AdapterProgeso(private val context: Context?, private val dataList: JSONArray) :
    RecyclerView.Adapter<AdapterProgeso.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.ImagenProgreso)
        var textView: TextView = itemView.findViewById(R.id.TextoLenguaje)
        var progressBar: ProgressBar = itemView.findViewById(R.id.progress_Explicacion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cardprogreso, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dataList.getJSONObject(position).getString("lenguajeNombre")
        holder.progressBar.progress = dataList.getJSONObject(position).getInt("progresoLenguaje")
        context?.let {
            Glide.with(it)
                .load(dataList.getJSONObject(position).getString("lenguajeLogo"))
                .into(holder.imageView)
        }

        // Aquí puedes manejar la lógica para el ProgressBar si es necesario
    }

    override fun getItemCount(): Int {
        return dataList.length()
    }
}
