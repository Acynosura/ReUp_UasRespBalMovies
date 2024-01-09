package com.example.respbalmovies.data

//INISIASI VARIABEL UNTUK DATABASE DataFilm DI FIREBASE NANTINYA
data class DataFilm(
    var id: String = "",
    var image_url: String = "",
    var tittle: String = "",
    var director: String = "",
    var duration: String = "",
    var description: String = "",
    var genre: String = "",
    var price: String = ""
)
