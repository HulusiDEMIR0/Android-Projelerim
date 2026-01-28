package com.example.proje5

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proje5.databinding.GirisSayasiBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Girissayfasi : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: GirisSayasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GirisSayasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.tvRegister.paintFlags =
            binding.tvRegister.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val sifre = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || sifre.isEmpty()) {
                Toast.makeText(this, "Email ve şifre boş olamaz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, sifre)
                .addOnSuccessListener {
                    Toast.makeText(this, "Giriş başarılı", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Anasayfa::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Kullanıcı daha önce giriş yapmış
            startActivity(Intent(this, Anasayfa::class.java))
            finish()
        }
    }
}
