package com.example.groceryapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.groceryapp.databinding.ActivityDetailsBinding
import com.example.groceryapp.model.PopularModel
import com.example.groceryapp.model.RecommendetModel
import com.example.groceryapp.model.ViewAllModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DetailsActivity : AppCompatActivity() {
    private lateinit var bindin: ActivityDetailsBinding
    private  var viewAllModel: ViewAllModel? = null
    private var recommendetModel: RecommendetModel? = null


    private var totalProduct: Long = 1
    private var totalPrice: Long = 0

    private  var saveCurrentDate: String =""
    private var saveCurrentTime: String = ""
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindin = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(bindin.root)

        setSupportActionBar(bindin.toolbarDetailsId)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val `object`: Any? = intent.getSerializableExtra("detail")
        if (`object` is ViewAllModel) {
            viewAllModel = `object`
        }

        val Recobject: Any? = intent.getSerializableExtra("item")
        if (Recobject is RecommendetModel){
            recommendetModel = Recobject
        }

        if (viewAllModel != null){
            bindin.detailsDescriptionId.text = viewAllModel!!.description
            bindin.detailsPriceId.text = viewAllModel!!.price.toString()
            bindin.detailsRatingId.text = viewAllModel!!.rating
            Picasso.get().load(viewAllModel!!.img_url).placeholder(R.drawable.imag3).into(bindin.detailsImgId)

            totalPrice = viewAllModel!!.price * totalProduct

            ////add cart
            bindin.detaislAddCartBtnId.setOnClickListener{

                addedViewToCart()
            }

            bindin.addItemBtnId.setOnClickListener{
                if (totalProduct < 10){
                    totalProduct++
                    bindin.itemNumberId.text = totalProduct.toString()
                    totalPrice = viewAllModel!!.price * totalProduct
                }
            }
            bindin.removeItemBtnId.setOnClickListener{
                if (totalProduct > 1){
                    totalProduct--
                    bindin.itemNumberId.text = totalProduct.toString()
                    totalPrice = viewAllModel!!.price * totalProduct
                }
            }

            if (viewAllModel!!.type.equals("egg")) {
                bindin.detailsPriceId.text = ("Price : $" + viewAllModel!!.price.toString() + "/dozen")
                totalPrice = viewAllModel!!.price * totalProduct
            }

            if (viewAllModel!!.type.equals("milk")) {
                bindin.detailsPriceId.text = ("Price : $" + viewAllModel!!.price.toString() + "/litre")
                totalPrice = viewAllModel!!.price * totalProduct
            }

        }

        if (recommendetModel != null){
            bindin.detailsDescriptionId.text = recommendetModel!!.description
            bindin.detailsPriceId.text = recommendetModel!!.price.toString()
            bindin.detailsRatingId.text = recommendetModel!!.rating
            Picasso.get().load(recommendetModel!!.img_url).placeholder(R.drawable.imag3).into(bindin.detailsImgId)

         totalPrice = recommendetModel!!.price * totalProduct

            ////add cart
            bindin.detaislAddCartBtnId.setOnClickListener{
                addedToCart()
            }
            bindin.addItemBtnId.setOnClickListener{
                if (totalProduct < 10){
                    totalProduct++
                    bindin.itemNumberId.text = totalProduct.toString()
                    totalPrice = recommendetModel!!.price * totalProduct
                }
            }
            bindin.removeItemBtnId.setOnClickListener{
                if (totalProduct > 1){
                    totalProduct--
                    bindin.itemNumberId.text = totalProduct.toString()
                    totalPrice = recommendetModel!!.price * totalProduct
                }
            }


        }
    }

    private fun addedToCart() {

     val calForDate = Calendar.getInstance()
      val currentDate = SimpleDateFormat("MM dd, yyyy")
        saveCurrentDate = currentDate.format(calForDate.time)
        val currentTime = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentTime.format(calForDate.time)


        val addCart: HashMap<String, Any> = HashMap()
        addCart.put("productName", recommendetModel!!.name)
        addCart.put("productPrice", recommendetModel!!.price)
        addCart.put("currentDate", saveCurrentDate)
        addCart.put("currentTime", saveCurrentTime)
        addCart.put("totalProduct", totalProduct)
        addCart.put("totalPrice", totalPrice)


        db.collection("currentUser").document(auth.currentUser!!.uid)
                .collection("AddToCart").add(addCart).addOnCompleteListener { task ->
                    Toast.makeText(this, "added a cart", Toast.LENGTH_SHORT).show()
                }
    }


    private fun addedViewToCart() {

        val calForDate = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MM dd, yyyy")
        saveCurrentDate = currentDate.format(calForDate.time)
        val currentTime = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentTime.format(calForDate.time)

        val cartMap: HashMap<String, Any> = HashMap()
        cartMap.put("productName", viewAllModel!!.name)
        cartMap.put("productPrice", viewAllModel!!.price)
        cartMap.put("currentDate", saveCurrentDate)
        cartMap.put("currentTime", saveCurrentTime)
        cartMap.put("totalProduct", totalProduct)
        cartMap.put("totalPrice", totalPrice)


        db.collection("currentUser").document(auth.currentUser!!.uid)
                .collection("AddToCart").add(cartMap).addOnCompleteListener { task ->
                    Toast.makeText(this, "added a cart", Toast.LENGTH_SHORT).show()
                    finish()
                }
    }
}