package com.example.groceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapp.adapter.MyCartAdapter
import com.example.groceryapp.model.MyCartModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class OrdersActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var myCartAdapter: MyCartAdapter
    private lateinit var cartList: ArrayList<MyCartModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        recyclerView = findViewById(R.id.userOrderRecycleViewId)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        recyclerView.layoutManager = LinearLayoutManager(this)
        cartList = arrayListOf()
        myCartAdapter = MyCartAdapter(this, cartList)
       recyclerView.adapter = myCartAdapter

        ///get cart added user product
        db.collection("currentUser").document(auth.currentUser!!.uid)
                .collection("MyOrder").addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null) {
                            Log.e("error", error.message.toString())
                            return
                        }
                        for (dc: DocumentChange in value?.documentChanges!!) {
                            ///delete item
                            val documentId =  dc.document.id
                            val cartModel = dc.document.toObject(MyCartModel::class.java)
                            cartModel.documentId = documentId

                            if (dc.type == DocumentChange.Type.ADDED) {
                                (cartList as ArrayList<MyCartModel>).add(dc.document.toObject(MyCartModel::class.java))


                            }
                        }
                        myCartAdapter.notifyDataSetChanged()
                    }
                })

    }
}