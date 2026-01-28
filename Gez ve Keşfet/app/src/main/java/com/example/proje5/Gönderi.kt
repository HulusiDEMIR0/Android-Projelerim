package com.example.proje5

data class Gönderi(
    val postId: String = "",

    // Kullanıcı
    val userId: String = "",
    val userName: String = "",

    // Ortak
    val location: String = "",
    val placeType: String = "",
    val rating: Double = 0.0,
    val createdAt: Long = 0L,

    // Opsiyonel
    val comment: String? = null,
    val imageUrl: String? = null,

    // Restoran
    val menuRating: Double? = null,
    val priceLevel: String? = null,

    // Müze
    val ticketType: String? = null,
    val ticketPrice: Double? = null,

    // Park
    val cleanliness: Double? = null,
    val greenery: Double? = null,
    val air: Double? = null
)
