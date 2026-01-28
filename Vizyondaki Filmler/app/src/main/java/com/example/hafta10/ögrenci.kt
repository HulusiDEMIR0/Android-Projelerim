package com.example.hafta10

class ögrenci {

    var numara = 10
    var adı = "ahmet"
    var soyadı = "demir"

    fun fonk(){
        println("fonksiyon çalıştı")
        println("ögrenci adı : $adı")
        println("ögrenci soyadı : $soyadı")
    }

    fun deneme(isim : String,soyisim : String) {
        println("deneme fonksiyonu çalıştı")
        println("isim : $isim")
        println("soyisim : $soyisim")

    }


}

class ogrenci2(var no : Int,var ad : String,var soyad : String){

    fun fonk(){
        println("fonksiyon çalıştı")
        println("ögrenci adı : $ad")
        println("ögrenci soyadı : $soyad")
    }


}

abstract  class soyut (var no : Int,var ad : String,var soyad : String){
    fun fonk(){
        println("soyut fonk çalıştı")
        println("ögrenci adı : $ad")
        println("ögrenci soyadı : $soyad")
    }
}