package com.example.proje5

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proje5.databinding.FragmentCreatePostBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage
import java.util.UUID


class CreatePostFragment : Fragment(R.layout.fragment_create_post) {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    private val auth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCreatePostBinding.bind(view)

        binding.layoutImageUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1001)
        }

        setupPlaceTypeSpinner()
        setupMuseumTicketSpinner()
        setupSaveButton()
    }

    /**
     * Yer tipi spinner'ı ve seçime göre alanları aç/kapa
     */
    private fun setupPlaceTypeSpinner() {

        val placeTypes = listOf(
            "Yer Tipi Seç",
            "Restoran / Kafe",
            "Müze",
            "Park / Ören Yeri"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            placeTypes
        )

        binding.spinnerPlaceType.adapter = adapter

        binding.spinnerPlaceType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // önce hepsini kapat
                    binding.layoutRestaurant.visibility = View.GONE
                    binding.layoutMuseum.visibility = View.GONE
                    binding.layoutPark.visibility = View.GONE

                    when (parent.getItemAtPosition(position).toString()) {
                        "Restoran / Kafe" -> {
                            binding.layoutRestaurant.visibility = View.VISIBLE
                        }
                        "Müze" -> {
                            binding.layoutMuseum.visibility = View.VISIBLE
                        }
                        "Park / Ören Yeri" -> {
                            binding.layoutPark.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    /**
     * Kaydet butonu – zorunlu alan kontrolleri
     */
    private fun setupSaveButton() {

        binding.btnSavePost.setOnClickListener {

            val location = binding.etLocation.text.toString().trim()
            val ratingStr = binding.etRating.text.toString().trim()
            val placeTypeIndex = binding.spinnerPlaceType.selectedItemPosition
            val comment = binding.etComment.text.toString().trim()

            val currentUser = auth.currentUser
            if (currentUser == null) {
                toast("Kullanıcı oturumu bulunamadı")
                return@setOnClickListener
            }

            if (location.isEmpty()) {
                toast("Konum zorunludur")
                return@setOnClickListener
            }

            if (ratingStr.isEmpty()) {
                toast("Puan giriniz")
                return@setOnClickListener
            }

            val rating = ratingStr.toDoubleOrNull()
            if (rating == null || rating < 0 || rating > 10) {
                toast("Puan 0 - 10 arasında olmalıdır")
                return@setOnClickListener
            }

            if (placeTypeIndex == 0) {
                toast("Yer tipi seçiniz")
                return@setOnClickListener
            }

            if (placeTypeIndex == 1) {

                val menuRatingStr = binding.etMenuRating.text.toString().trim()
                val priceText = binding.etPriceLevel.text.toString().trim()

                if (menuRatingStr.isNotEmpty()) {
                    val menuRating = menuRatingStr.toDoubleOrNull()
                    if (menuRating == null || menuRating < 0 || menuRating > 10) {
                        toast("Menü puanı 0 - 10 arasında olmalıdır")
                        return@setOnClickListener
                    }
                }
            }

            if (placeTypeIndex == 2) {

                val ticketTypeIndex = binding.spinnerTicketType.selectedItemPosition
                if (ticketTypeIndex != 0) {
                    val ticketPriceStr = binding.etTicketPrice.text.toString().trim()
                    if (ticketPriceStr.isEmpty()) {
                        toast("Bilet türü seçildiğinde fiyat girilmelidir")
                        return@setOnClickListener
                    }
                }
            }

            if (placeTypeIndex == 3) {

                val cleanlinessStr = binding.etCleanliness.text.toString().trim()
                val greeneryStr = binding.etGreenery.text.toString().trim()
                val airStr = binding.etAir.text.toString().trim()

                if (cleanlinessStr.isNotEmpty()) {
                    val cleanliness = cleanlinessStr.toDoubleOrNull()
                    if (cleanliness == null || cleanliness < 0 || cleanliness > 10) {
                        toast("Temizlik puanı 0 - 10 arasında olmalıdır")
                        return@setOnClickListener
                    }
                }

                if (greeneryStr.isNotEmpty()) {
                    val greenery = greeneryStr.toDoubleOrNull()
                    if (greenery == null || greenery < 0 || greenery > 10) {
                        toast("Yeşillik puanı 0 - 10 arasında olmalıdır")
                        return@setOnClickListener
                    }
                }

                if (airStr.isNotEmpty()) {
                    val air = airStr.toDoubleOrNull()
                    if (air == null || air < 0 || air > 10) {
                        toast("Hava puanı 0 - 10 arasında olmalıdır")
                        return@setOnClickListener
                    }
                }
            }

            Firebase.firestore
                .collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->

                    val userName = document.getString("username") ?: "Bilinmeyen Kullanıcı"

                    val postData = hashMapOf<String, Any>(
                        "location" to location,
                        "rating" to rating,
                        "placeType" to binding.spinnerPlaceType.selectedItem.toString(),
                        "createdAt" to System.currentTimeMillis(),
                        "userId" to currentUser.uid,
                        "userName" to userName
                    )

                    if (comment.isNotEmpty()) {
                        postData["comment"] = comment
                    }

                    when (placeTypeIndex) {

                        1 -> {
                            binding.etMenuRating.text.toString().toDoubleOrNull()
                                ?.let { postData["menuRating"] = it }

                            binding.etPriceLevel.text.toString().trim()
                                .takeIf { it.isNotEmpty() }
                                ?.let { postData["priceLevel"] = it }
                        }

                        2 -> {
                            val ticketTypeIndex = binding.spinnerTicketType.selectedItemPosition
                            if (ticketTypeIndex != 0) {
                                postData["ticketType"] =
                                    binding.spinnerTicketType.selectedItem.toString()

                                binding.etTicketPrice.text.toString().toDoubleOrNull()
                                    ?.let { postData["ticketPrice"] = it }
                            }
                        }

                        3 -> {
                            binding.etCleanliness.text.toString().toDoubleOrNull()
                                ?.let { postData["cleanliness"] = it }

                            binding.etGreenery.text.toString().toDoubleOrNull()
                                ?.let { postData["greenery"] = it }

                            binding.etAir.text.toString().toDoubleOrNull()
                                ?.let { postData["air"] = it }
                        }
                    }

                    if (selectedImageUri != null) {
                        uploadImageAndSavePost(postData)
                    } else {
                        savePostToFirestore(postData)
                    }
                }
                .addOnFailureListener {
                    toast("Kullanıcı bilgileri alınamadı")
                }
        }
    }


    private fun uploadImageAndSavePost(postData: HashMap<String, Any>) {

        val imageRef = Firebase.storage.reference
            .child("post_images/${UUID.randomUUID()}.jpg")

        imageRef.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                toast("Fotoğraf yüklendi")
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    postData["imageUrl"] = uri.toString()
                    savePostToFirestore(postData)
                }
            }
            .addOnFailureListener {
                toast("Fotoğraf yüklenemedi")
            }
    }


    private fun savePostToFirestore(postData: HashMap<String, Any>) {

        Firebase.firestore
            .collection("posts")
            .add(postData)
            .addOnSuccessListener {
                toast("Gönderi başarıyla kaydedildi")
                clearForm()
            }
            .addOnFailureListener {
                toast("Kayıt başarısız: ${it.localizedMessage}")
            }
    }

    private fun clearForm() {

        // Ortak alanlar
        binding.etLocation.text?.clear()
        binding.etRating.text?.clear()
        binding.spinnerPlaceType.setSelection(0)

        // Yorum
        binding.etComment.text?.clear()

        // Restoran / Kafe
        binding.etMenuRating.text?.clear()
        binding.etPriceLevel.text?.clear()
        binding.layoutRestaurant.visibility = View.GONE

        // Müze
        binding.spinnerTicketType.setSelection(0)
        binding.etTicketPrice.text?.clear()
        binding.layoutMuseum.visibility = View.GONE

        // Park / Ören yeri
        binding.etCleanliness.text?.clear()
        binding.etGreenery.text?.clear()
        binding.etAir.text?.clear()
        binding.layoutPark.visibility = View.GONE

        // Fotoğraf
        selectedImageUri = null
        binding.ivPreview.setImageDrawable(null)
        binding.ivPreview.visibility = View.GONE
        binding.layoutPlaceholder.visibility = View.VISIBLE
    }



    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == AppCompatActivity.RESULT_OK) {
            selectedImageUri = data?.data

            if (selectedImageUri != null) {

                // ✅ Önizleme göster
                binding.ivPreview.setImageURI(selectedImageUri)
                binding.ivPreview.visibility = View.VISIBLE
                binding.tvUploadText.visibility = View.GONE

                // ✅ Anlık bilgi
                toast("Fotoğraf seçildi")
            }
        }
    }



    private fun setupMuseumTicketSpinner() {

        val ticketTypes = listOf(
            "Bilet Türü Seç",
            "Tam",
            "Öğrenci",
            "Kampanya",
            "0-7 Yaş Ücretsiz",
            "Ücretsiz"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            ticketTypes
        )

        binding.spinnerTicketType.adapter = adapter

        binding.spinnerTicketType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selected = parent.getItemAtPosition(position).toString()

                    // Ücretsiz durumlar → fiyat alanı gizlenir
                    if (
                        selected == "Ücretsiz" ||
                        selected == "0-7 Yaş Ücretsiz"
                    ) {
                        binding.etTicketPrice.visibility = View.GONE
                    } else {
                        binding.etTicketPrice.visibility = View.VISIBLE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
