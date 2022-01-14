package com.example.groceryapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapp.CartsActivity
import com.example.groceryapp.R
import com.example.groceryapp.SubCategoryActivity
import com.example.groceryapp.model.MyCartModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyCartAdapter(var context: Context, var cartList: ArrayList<MyCartModel>): RecyclerView.Adapter<MyCartAdapter.ViewHolder>() {

    var totalProductPrice: Long = 0
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.my_cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        var cart = cartList[position]

        holder.name.text = cart.productName
        holder.date.text = cart.currentDate
        holder.time.text = cart.currentTime
        holder.price.text = cart.productPrice.toString()
        holder.totalProduct.text = cart.totalProduct.toString()
        holder.totalPrice.text = cart.totalPrice.toString()

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        holder.delete.setOnClickListener{

            val builder = AlertDialog.Builder(context)
                    builder.setTitle("Alert")
                    builder.setMessage("Do you want to delete this item")
                    builder.setPositiveButton("yes"){dialogInterface, i->
                        db.collection("currentUser").document(auth.currentUser!!.uid).collection("AddToCart")
                                .document(cartList[position].documentId).delete().addOnCompleteListener{task->
                                    if (task.isSuccessful){
                                        cartList.removeAt(position)
                                        Toast.makeText(context,"delete successfull", Toast.LENGTH_SHORT).show()

                                    }
                                    else{
                                        Log.e("tag","Error : ${task.exception.toString()}")
                                    }
                                }
                        notifyDataSetChanged()
                        dialogInterface.dismiss()
                    }
                    builder.setNegativeButton("Cancel"){dialogInterface, i ->
                        dialogInterface.dismiss()
                    }

            builder.create()
            builder.show()

        }

        ///// pass total amount to my cart activity
        totalProductPrice = totalProductPrice + cart.productPrice
            Log.e("value", "Value : ${totalProductPrice}")

        val intent = Intent(Intent(context, CartsActivity::class.java))
        intent.putExtra("totalAmount",totalProductPrice ) ///cartList[position].totalPrice)
        context.startActivity(intent)

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

    }

    override fun getItemCount(): Int {
       return cartList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var name = itemView.findViewById<TextView>(R.id.product_name)
        var price = itemView.findViewById<TextView>(R.id.product_price)
        var date = itemView.findViewById<TextView>(R.id.currentDateId)
        var time = itemView.findViewById<TextView>(R.id.current_timeId)
        var totalProduct = itemView.findViewById<TextView>(R.id.total_productId)
        var totalPrice = itemView.findViewById<TextView>(R.id.total_priceId)
        var delete = itemView.findViewById<ImageView>(R.id.deleteBtnId)

    }

}