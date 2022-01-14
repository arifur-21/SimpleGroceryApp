package com.example.groceryapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.groceryapp.model.MyCartModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PlacedOrderActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placed_order)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val list = intent.getSerializableExtra("itemList") as ArrayList<MyCartModel>

        if (list != null && list.size > 0){

            for (model in list){
                val cartMap: HashMap<String, Any> = HashMap()

                cartMap["productName"] = model.productName
                cartMap["productPrice"] = model.productPrice
                cartMap["currentDate"] = model.currentDate
                cartMap["currentTime"] = model.currentTime
                cartMap["totalProduct"] = model.totalProduct
                cartMap["totalPrice"] = model.totalPrice

                db.collection("currentUser").document(auth.currentUser!!.uid).collection("MyOrder").add(cartMap).addOnCompleteListener{task ->
                    Toast.makeText(this,"place order added", Toast.LENGTH_SHORT).show()
                    finish()
                }

            }

        }

      
    }
}