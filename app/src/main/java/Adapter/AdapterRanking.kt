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
import org.json.JSONObject

class AdapterRanking(var context:Context?, var listaRanking: JSONObject):RecyclerView.Adapter<AdapterRanking.MyHolder>() {

    inner class MyHolder(item: View) : RecyclerView.ViewHolder(item) {
        val puesto: TextView = item.findViewById(R.id.Posicion)
        val nombre: TextView = item.findViewById(R.id.Nombre_Ranking)
        val puntos: TextView = item.findViewById(R.id.Puntaje_Ranking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_ranking, parent, false)
        return MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaRanking.length()
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val rankingObject = listaRanking.getJSONObject((position + 1).toString())
        var suma = 0
        for (i in 0 until position + 1) {
            suma += listaRanking.getJSONObject((i + 1).toString()).getInt("puntos")
        }
        holder.puesto.text = (position + 1).toString()
        holder.nombre.text = rankingObject.getString("nombre")
        holder.puntos.text = suma.toString()
    }

}