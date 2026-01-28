package com.example.vizyondakifilmlervol2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FilmDatabase(context: Context) :
    SQLiteOpenHelper(context, "filmler.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL( // film tablosunu burada tanımladık her bir satırın kendi pk'sı var
            """
        CREATE TABLE filmler (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            ad TEXT,
            vizyon TEXT,
            yonetmen TEXT,
            basrol TEXT,
            resim INTEGER,
            sehir TEXT
        )
        """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS filmler")
        onCreate(db)
    } // tabloyu tamamen silme işlemi

    fun getAllFilms(): ArrayList<Film> { // ana sayfa açıldığında tüm filmleri göstereeği için burayı kullanıyoruz
        val list = ArrayList<Film>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM filmler", null)

        if (cursor.moveToFirst()) {
            do {
                val film = Film(
                    id = cursor.getInt(0),
                    ad = cursor.getString(1),
                    vizyon = cursor.getString(2),
                    yonetmen = cursor.getString(3),
                    basrol = cursor.getString(4),
                    resim = cursor.getInt(5),
                    sehir = cursor.getString(6)
                )
                list.add(film)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list
    }

    fun getFilmsByCity(sehir: String): ArrayList<Film> { // menuden sehre göre bir film secme yaparsa bu fonksiyon Lıke ile çalışır
        val list = ArrayList<Film>() // ve bir çok şehir de o film vizyonda olsa bile biz burada onu seçtiğimiz şehre göre gösteriyoruz
        val db = this.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM filmler WHERE sehir LIKE ?",
            arrayOf("%$sehir%")
        )

        if (cursor.moveToFirst()) {
            do {
                val film = Film(
                    id = cursor.getInt(0),
                    ad = cursor.getString(1),
                    vizyon = cursor.getString(2),
                    yonetmen = cursor.getString(3),
                    basrol = cursor.getString(4),
                    resim = cursor.getInt(5),
                    sehir = cursor.getString(6)
                )
                list.add(film)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list
    }

    fun insertFilm(film: Film) { // film eklemek için bu kod çalışıyor saece ilk basta veri tabanı bo işse bur komut çalışır ve yazmış olduğumuz fimler eklenir
        val db = this.writableDatabase
        val sql = "INSERT INTO filmler (ad, vizyon, yonetmen, basrol, resim, sehir) VALUES (?, ?, ?, ?, ?, ?)"
        val stmt = db.compileStatement(sql)

        stmt.bindString(1, film.ad)
        stmt.bindString(2, film.vizyon)
        stmt.bindString(3, film.yonetmen)
        stmt.bindString(4, film.basrol)
        stmt.bindLong(5, film.resim.toLong())
        stmt.bindString(6, film.sehir)

        stmt.executeInsert()
        db.close()
    }


}

