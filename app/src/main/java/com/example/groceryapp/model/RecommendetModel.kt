package com.example.groceryapp.model

import java.io.Serializable

data class RecommendetModel(
    var name: String = "",
    var description: String = "",
    var rating: String ="",
    var img_url: String = "",
     var price: Int = 0
):Serializable {
}