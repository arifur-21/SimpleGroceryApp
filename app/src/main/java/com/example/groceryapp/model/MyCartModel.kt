package com.example.groceryapp.model

import java.io.Serializable

class MyCartModel(
        var productName: String ="",
        var currentDate: String ="",
        var currentTime: String ="",
        var documentId: String ="",
        var productPrice: Long = 0,
        var totalProduct: Long = 0,
        var totalPrice: Long = 0

): Serializable {
}