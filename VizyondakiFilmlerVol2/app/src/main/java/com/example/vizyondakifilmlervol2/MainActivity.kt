package com.example.vizyondakifilmlervol2

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var db: FilmDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FilmAdapter
    private lateinit var txtBaslik: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        txtBaslik = findViewById(R.id.txtBaslik)

        db = FilmDatabase(this)

        if (db.getAllFilms().isEmpty()) {

            db.insertFilm(
                Film(0, "Zootropolis 2", "2025-11-28", "Rich Moore",
                    "Judy Hopps", R.drawable.zootropolis_2, "istanbul,ankara")
            )

            db.insertFilm(
                Film(0, "Nasipse Olur 2", "2025-11-28", "Selahattin Sancaklı",
                    "Burak Sevinç", R.drawable.nasipse_olur_2_, "istanbul,ankara,malatya")
            )

            db.insertFilm(
                Film(0, "Yan Yana", "2025-11-14", "Mert Baykal",
                    "Feyyaz Yiğit", R.drawable.yan_yana, "istanbul,malatya")
            )

            db.insertFilm(
                Film(0, "Sihirbazlar Çetesi", "2025-11-14", "Ruben Fleischer",
                    "Jesse Eisenberg", R.drawable.sihrbazlar__etesi, "ankara,malatya")
            )
        }

        recyclerView = findViewById(R.id.rvFilmler) // RecyclerView'i burada tanımladık
        recyclerView.layoutManager = LinearLayoutManager(this)

        val tumFilmler = db.getAllFilms() // sql ile var olan tüm filmleri burada cekiyoruz

        adapter = FilmAdapter(tumFilmler) // göstermesi için de adeptere bağlıyoruz
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var liste: List<Film> = emptyList()

        when (item.itemId) {

            R.id.mnuTurkiye -> {
                txtBaslik.text = "Türkiye'de Vizyondaki Filmler"
                liste = db.getAllFilms()
            }

            R.id.mnuIstanbul -> {
                txtBaslik.text = "İstanbul’da Vizyondaki Filmler"
                liste = db.getFilmsByCity("istanbul")
            }

            R.id.mnuMalatya -> {
                txtBaslik.text = "Malatya’da Vizyondaki Filmler"
                liste = db.getFilmsByCity("malatya")
            }

            R.id.mnuAnkara -> {
                txtBaslik.text = "Ankara’da Vizyondaki Filmler"
                liste = db.getFilmsByCity("ankara")
            }
        }

        adapter.updateList(liste) // gösterilecek fimler değiştiği için burada yeni filmleri gösteriyoruz

        return true
    }
}
