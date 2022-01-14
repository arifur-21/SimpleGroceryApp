package com.example.groceryapp.model

import java.io.Serializable

data class ViewAllModel(
    var name: String = "",
    var description: String ="",
    var rating: String = "",
    var type: String = "",
    var img_url: String ="",
    var price: Long = 0
): Serializable {
}