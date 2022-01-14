package com.example.groceryapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapp.R
import com.example.groceryapp.model.SubCategoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class SubCategoryAdapter (var context: Context, var subCateList: List<SubCategoryModel>): RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {


    private var totalProduct: Long = 1
    private var totalPrice: Long = 0
    private  var saveCurrentDate: String =""
    private var saveCurrentTime: String = ""
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       var view = LayoutInflater.from(context).inflate(R.layout.sub_cate_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        auth = FirebaseAuth.getInstance()
        val subList = subCateList[position]

        holder.name.text = subList.name
        holder.price.text = subList.price.toString()
        Picasso.get().load(subList.img_url).placeholder(R.drawable.imag3).into(holder.image)

        holder.addItem.setOnClickListener{
            if (totalProduct < 10){
                totalProduct++
                holder.itemNumber.text = totalProduct.toString()
                totalPrice = subList.price * totalProduct
            }
        }
        holder.removeItem.setOnClickListener{
            if ( totalProduct > 0){
                totalProduct--
                holder.itemNumber.text = totalProduct.toString()
                totalPrice = subList.price * totalProduct
            }
        }

        holder.buyNow.setOnClickListener{

            val calForDate = Calendar.getInstance()
            val currentDate = SimpleDateFormat("MM dd, yyyy")
            saveCurrentDate = currentDate.format(calForDate.time)

            val currentTime = SimpleDateFormat("HH:mm:ss a")
            saveCurrentTime = currentTime.format(calForDate.time)

            val cartMap: HashMap<String, Any> = HashMap()

            cartMap.put("productName", subList.name)
            cartMap.put("productPrice", subList.price)
            cartMap.put("currentDate", saveCurrentDate)
            cartMap.put("currentTime", saveCurrentTime)
            cartMap.put("totalProduct", totalProduct)
            cartMap.put("totalPrice", totalPrice)

            db = FirebaseFirestore.getInstance()
            db.collection("currentUser").document(auth.currentUser!!.uid)
                    .collection("AddToCart").add(cartMap).addOnCompleteListener { task ->
                        Toast.makeText(context, "added a cart", Toast.LENGTH_SHORT).show()
                    }

        }

    }

    override fun getItemCount(): Int {
      return subCateList.size
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var name = itemView.findViewById<TextView>(R.id.sub_cate_nameId)
        var price = itemView.findViewById<TextView>(R.id.sub_cateProductPriceId)
        var itemNumber = itemView.findViewById<TextView>(R.id.sub_itemNumberId)
        var image = itemView.findViewById<ImageView>(R.id.sub_cate_img)
        var addItem = itemView.findViewById<ImageView>(R.id.sub_addItemImgId)
        var removeItem = itemView.findViewById<ImageView>(R.id.sub_removeItemImgd)
        var buyNow = itemView.findViewById<Button>(R.id.sub_AddCartBtnId)

    }
}