package com.example.proje5

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proje5.databinding.ActivityAnasayfaBinding
import com.example.proje5.databinding.GirisSayasiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Anasayfa : AppCompatActivity() {
    private  lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var binding: ActivityAnasayfaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnasayfaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.akıs_feed -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, FeedFragment()) // akıs
                        .commit()
                    true
                }

                R.id.post_feed -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, CreatePostFragment()) // post paylasım
                        .commit()
                    true
                }
                R.id.cikis -> {
                    showLogoutDialog()
                    false
                }
                else -> false
            }
        }


    }

    private fun showLogoutDialog() {

        AlertDialog.Builder(this)
            .setTitle("Çıkış Yap")
            .setMessage("Çıkış yapmak istediğinizden emin misiniz?")
            .setPositiveButton("Evet") { _, _ ->
                logout()
            }
            .setNegativeButton("Hayır") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun logout() {

        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this, Girissayfasi::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }



}