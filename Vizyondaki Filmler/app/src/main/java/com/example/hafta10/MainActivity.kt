package com.example.hafta10

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listFilmler)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        val filmler = arrayOf(
            arrayOf("Zootropolis 2", "2025-11-28", "Rich Moore", "Judy Hopps", R.drawable.zootropolis_2),
            arrayOf("Nasipse Olur 2", "2025-11-28", "Selahattin Sancaklı", "Burak Sevinç", R.drawable.nasipse_olur_2_),
            arrayOf("Yan Yana", "2025-11-14", "Mert Baykal", "Feyyaz Yiğit", R.drawable.yan_yana),
            arrayOf("Sihirbazlar Çetesi", "2025-11-14", "Ruben Fleischer", "Jesse Eisenberg", R.drawable.sihrbazlar__etesi)
        )

        val adapter = object : BaseAdapter() {
            override fun getCount(): Int = filmler.size
            override fun getItem(position: Int): Any = filmler[position]
            override fun getItemId(position: Int): Long = position.toLong()

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = layoutInflater.inflate(R.layout.row_film, parent, false)

                val film = filmler[position]

                val imgPoster = view.findViewById<ImageView>(R.id.imgPoster)
                val txtAd = view.findViewById<TextView>(R.id.txtAd)
                val txtVizyon = view.findViewById<TextView>(R.id.txtVizyon)
                val txtYonetmen = view.findViewById<TextView>(R.id.txtYonetmen)
                val txtBasrol = view.findViewById<TextView>(R.id.txtBasrol)

                txtAd.text = film[0].toString()
                txtVizyon.text = "Vizyon: " + film[1].toString()
                txtYonetmen.text = "Yönetmen: " + film[2].toString()
                txtBasrol.text = "Başrol: " + film[3].toString()
                imgPoster.setImageResource(film[4] as Int)

                return view
            }
        }

        listView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val txtBaslik = findViewById<TextView>(R.id.txtBaslik)

        when (item.itemId) {

            R.id.mnuTurkiye -> {
                txtBaslik.text = "Türkiye'de Vizyondaki Filmler"
                filmleriYukle("turkiye")
                return true
            }

            R.id.mnuIstanbul -> {
                txtBaslik.text = "İstanbul’da Vizyondaki Filmler"
                filmleriYukle("istanbul")
                return true
            }

            R.id.mnuMalatya -> {
                txtBaslik.text = "Malatya’da Vizyondaki Filmler"
                filmleriYukle("malatya")
                return true
            }

            R.id.mnuAnkara -> {
                txtBaslik.text = "Ankara’da Vizyondaki Filmler"
                filmleriYukle("ankara")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun filmleriYukle(sehir: String) {

        val filmler = when (sehir) {

            "turkiye" -> arrayOf(
                arrayOf("Zootropolis 2", "2025-11-28", "Rich Moore", "Judy Hopps", R.drawable.zootropolis_2),
                arrayOf("Nasipse Olur 2", "2025-11-28", "Selahattin Sancaklı", "Burak Sevinç", R.drawable.nasipse_olur_2_),
                arrayOf("Yan Yana", "2025-11-14", "Mert Baykal", "Feyyaz Yiğit", R.drawable.yan_yana),
                arrayOf("Sihirbazlar Çetesi", "2025-11-14", "Ruben Fleischer", "Jesse Eisenberg", R.drawable.sihrbazlar__etesi)
            )

            "istanbul" -> arrayOf(
                arrayOf("Nasipse Olur 2", "2025-11-28", "Selahattin Sancaklı", "Burak Sevinç", R.drawable.nasipse_olur_2_),
                arrayOf("Yan Yana", "2025-11-14", "Mert Baykal", "Feyyaz Yiğit", R.drawable.yan_yana),
                arrayOf("Sihirbazlar Çetesi", "2025-11-14", "Ruben Fleischer", "Jesse Eisenberg", R.drawable.sihrbazlar__etesi)
            )

            "malatya" -> arrayOf(
                arrayOf("Zootropolis 2", "2025-11-28", "Rich Moore", "Judy Hopps", R.drawable.zootropolis_2),
                arrayOf("Yan Yana", "2025-11-14", "Mert Baykal", "Feyyaz Yiğit", R.drawable.yan_yana),
                arrayOf("Sihirbazlar Çetesi", "2025-11-14", "Ruben Fleischer", "Jesse Eisenberg", R.drawable.sihrbazlar__etesi)
            )

            "ankara" -> arrayOf(
                arrayOf("Zootropolis 2", "2025-11-28", "Rich Moore", "Judy Hopps", R.drawable.zootropolis_2),
                arrayOf("Nasipse Olur 2", "2025-11-28", "Selahattin Sancaklı", "Burak Sevinç", R.drawable.nasipse_olur_2_),
                arrayOf("Yan Yana", "2025-11-14", "Mert Baykal", "Feyyaz Yiğit", R.drawable.yan_yana)
            )

            else -> emptyArray()
        }

        // ListView'i yeniden dolduruyoruz
        val adapter = object : BaseAdapter() {
            override fun getCount(): Int = filmler.size
            override fun getItem(position: Int): Any = filmler[position]
            override fun getItemId(position: Int): Long = position.toLong()

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = layoutInflater.inflate(R.layout.row_film, parent, false)

                val film = filmler[position]

                view.findViewById<TextView>(R.id.txtAd).text = film[0].toString()
                view.findViewById<TextView>(R.id.txtBasrol).text = "Başrol: ${film[3]}"
                view.findViewById<TextView>(R.id.txtYonetmen).text = "Yönetmen: ${film[2]}"
                view.findViewById<TextView>(R.id.txtVizyon).text = "Vizyon: ${film[1]}"
                view.findViewById<ImageView>(R.id.imgPoster).setImageResource(film[4] as Int)

                return view
            }
        }

        listView.adapter = adapter
    }

}


