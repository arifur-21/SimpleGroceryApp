package com.example.groceryapp.model

import android.util.Log
import java.io.Serializable

data class PopularModel(
    var name: String ="",
    var description: String ="",
    var rating: String="",
    var discount: Long = 0,
    var price: Long = 0,
    var type: String ="",
    var img_url: String ="",
):Serializable {
}