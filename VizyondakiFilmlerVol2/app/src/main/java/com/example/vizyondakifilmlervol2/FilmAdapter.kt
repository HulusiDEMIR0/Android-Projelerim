package com.example.vizyondakifilmlervol2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FilmAdapter(private var filmList: List<Film>) :
    RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

        // genel olarak burası row_film ile bağladır her bir filmin nasıl gösteileçeğini burdan row_filme aktarıypruz

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { // bu yüzden ilk once nesneler tanımlandı
        val imgPoster: ImageView = itemView.findViewById(R.id.imgPoster)
        val txtAd: TextView = itemView.findViewById(R.id.txtAd)
        val txtVizyon: TextView = itemView.findViewById(R.id.txtVizyon)
        val txtYonetmen: TextView = itemView.findViewById(R.id.txtYonetmen)
        val txtBasrol: TextView = itemView.findViewById(R.id.txtBasrol)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_film, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) { // burada text ekranları dolduruluyor
        val film = filmList[position]

        holder.imgPoster.setImageResource(film.resim)
        holder.txtAd.text = film.ad
        holder.txtVizyon.text = "Vizyon: ${film.vizyon}"
        holder.txtYonetmen.text = "Yönetmen: ${film.yonetmen}"
        holder.txtBasrol.text = "Başrol: ${film.basrol}"
    }

    override fun getItemCount(): Int = filmList.size // liste boyutu

    fun updateList(newList: List<Film>) { // her bir filtreleme işleiminden sonra liste değişmeli ki o listeyi ekranda gösterebilelim burda onu yapıyoruz
        filmList = newList
        notifyDataSetChanged()
    }
}
