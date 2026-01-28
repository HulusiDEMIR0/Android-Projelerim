package com.example.vizyondakifilmlervol2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.graphics.Typeface
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.TextView

class GirisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_giris)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtSayac = findViewById<TextView>(R.id.txtSayac)

        object : CountDownTimer(3000, 1000) {

            override fun onTick(kalanMS: Long) {
                val kalanSaniye = kalanMS / 1000 + 1

                val metin = "$kalanSaniye saniye sonra ana ekrana yönlendirileceksiniz"

                val spannable = SpannableString(metin)

                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    kalanSaniye.toString().length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                txtSayac.text = spannable
            }

            override fun onFinish() {
                startActivity(Intent(this@GirisActivity, MainActivity::class.java))
                finish()
            }

        }.start()
    }
}

