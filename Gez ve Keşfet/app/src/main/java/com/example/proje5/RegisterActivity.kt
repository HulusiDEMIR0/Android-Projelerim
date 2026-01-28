package com.example.proje5

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proje5.databinding.ActivityRegisterBinding
import com.example.proje5.databinding.GirisSayasiBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private  lateinit var auth: FirebaseAuth

    private lateinit var db : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth // auth değişkeni tanımlandı bağlatnı sağlamak için

        db = Firebase.firestore // db değişkeni tanımlandı bağlatnı sağlamak için

        binding.tvBackToLogin.paintFlags = binding.tvBackToLogin.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.tvBackToLogin.setOnClickListener { // giriş sayfasına yeniden yönlendiriyor
            var intent = Intent(this, Girissayfasi::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener { // kayıt ol butonu gerekli işlemlier yapılyor

            var email = binding.etEmail.text.toString().trim()
            var password = binding.etPassword.text.toString().trim()
            var passwordAgain = binding.etPasswordAgain.text.toString().trim()
            var username = binding.etUsername.text.toString().trim()
            var age = binding.etAge.text.toString().toIntOrNull()
            var phone = binding.etPhone.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() ||
                passwordAgain.isEmpty() || username.isEmpty() || phone.isEmpty()){
                Toast.makeText(this,"Tüm alanları doldurunuz",Toast.LENGTH_SHORT).show()
            }
            if (password != passwordAgain) {
                Toast.makeText(this, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (age == null || age < 13) {
                Toast.makeText(this, "yaşınız 13'den büyük olmalıdır", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password==passwordAgain){
                // 🔹 FIREBASE AUTH
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult -> // auth basatılı olması duumunda ypıalcaklar

                        val uid = authResult.user!!.uid

                        // 🔹 FIRESTORE'A KAYIT
                        val userMap = hashMapOf(
                            "username" to username,
                            "email" to email,
                            "phone" to phone,
                            "age" to age,
                            "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp() // kullanıcının kayıt oldğu zaman
                        )

                        db.collection("users")
                            .document(uid)
                            .set(userMap)
                            .addOnSuccessListener {

                                Toast.makeText(this, "Kayıt başarılı", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, Girissayfasi::class.java)) // fıre store kayıt yapar basarıı ise ekrana basarılı
                                // yazar ondan sonra giris sayfasına yonlendirir
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Kullanıcı bilgileri kaydedilemedi", Toast.LENGTH_SHORT).show()
                                // firesotre ver kayıt basarısz ise bunu yazar auth ile alakası yoktur
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                        // auth basarısız ise bunu yazar email ve sifre doğru olmadığında
                    }
            }


        }


    }
}