package com.example.proje5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class Adapter(
    private val postList: List<Gönderi>
) : RecyclerView.Adapter<Adapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlaceType: TextView = itemView.findViewById(R.id.tvPlaceType)
        val tvMainInfo: TextView = itemView.findViewById(R.id.tvMainInfo)
        val ivPostImage: ImageView = itemView.findViewById(R.id.ivPostImage)
        val tvComment: TextView = itemView.findViewById(R.id.tvComment)
        val tvExtraLabel1: TextView = itemView.findViewById(R.id.tvExtraLabel1)
        val tvExtraValue1: TextView = itemView.findViewById(R.id.tvExtraValue1)
        val tvExtraLabel2: TextView = itemView.findViewById(R.id.tvExtraLabel2)
        val tvExtraValue2: TextView = itemView.findViewById(R.id.tvExtraValue2)
        val tvExtraLabel3: TextView = itemView.findViewById(R.id.tvExtraLabel3)
        val tvExtraValue3: TextView = itemView.findViewById(R.id.tvExtraValue3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]

        /* --------- ORTAK ALANLAR --------- */

        holder.tvPlaceType.text = post.placeType
        val userName = if (post.userName.isNotEmpty()) {
            post.userName
        } else {
            "Anonim"
        }
        holder.tvMainInfo.text =
            "${userName} • ${post.location} • ⭐ ${post.rating}"

        // Yorum
        if (!post.comment.isNullOrEmpty()) {
            holder.tvComment.visibility = View.VISIBLE
            holder.tvComment.text = post.comment
        } else {
            holder.tvComment.visibility = View.GONE
        }

        // Fotoğraf
        if (!post.imageUrl.isNullOrEmpty()) {
            holder.ivPostImage.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(post.imageUrl)
                .into(holder.ivPostImage)
        } else {
            holder.ivPostImage.visibility = View.GONE
        }

        /* --------- EK ALANLARI BAŞTA KAPAT --------- */

        holder.tvExtraLabel1.visibility = View.GONE
        holder.tvExtraValue1.visibility = View.GONE
        holder.tvExtraLabel2.visibility = View.GONE
        holder.tvExtraValue2.visibility = View.GONE
        holder.tvExtraLabel3.visibility = View.GONE
        holder.tvExtraValue3.visibility = View.GONE

        /* --------- YER TİPİNE GÖRE AÇ --------- */

        when (post.placeType) {

            "Park - Ören Yeri" -> {

                post.cleanliness?.let {
                    holder.tvExtraLabel1.visibility = View.VISIBLE
                    holder.tvExtraValue1.visibility = View.VISIBLE
                    holder.tvExtraLabel1.text = "Temizlik"
                    holder.tvExtraValue1.text = ": $it"
                }

                post.greenery?.let {
                    holder.tvExtraLabel2.visibility = View.VISIBLE
                    holder.tvExtraValue2.visibility = View.VISIBLE
                    holder.tvExtraLabel2.text = "Yeşillik"
                    holder.tvExtraValue2.text = ": $it"
                }

                post.air?.let {
                    holder.tvExtraLabel3.visibility = View.VISIBLE
                    holder.tvExtraValue3.visibility = View.VISIBLE
                    holder.tvExtraLabel3.text = "Hava"
                    holder.tvExtraValue3.text = ": $it"
                }
            }

            "Restoran / Kafe" -> {

                post.menuRating?.let {
                    holder.tvExtraLabel1.visibility = View.VISIBLE
                    holder.tvExtraValue1.visibility = View.VISIBLE
                    holder.tvExtraLabel1.text = "Menü"
                    holder.tvExtraValue1.text = ": $it"
                }

                post.priceLevel?.let {
                    holder.tvExtraLabel2.visibility = View.VISIBLE
                    holder.tvExtraValue2.visibility = View.VISIBLE
                    holder.tvExtraLabel2.text = "Fiyat"
                    holder.tvExtraValue2.text = ": $it"
                }
            }

            "Müze" -> {

                post.ticketType?.let {
                    holder.tvExtraLabel1.visibility = View.VISIBLE
                    holder.tvExtraValue1.visibility = View.VISIBLE
                    holder.tvExtraLabel1.text = "Bilet"
                    holder.tvExtraValue1.text = ": $it"
                }

                post.ticketPrice?.let {
                    holder.tvExtraLabel2.visibility = View.VISIBLE
                    holder.tvExtraValue2.visibility = View.VISIBLE
                    holder.tvExtraLabel2.text = "Fiyat"
                    holder.tvExtraValue2.text = ": $it"
                }
            }
        }
    }


    override fun getItemCount(): Int = postList.size
}
