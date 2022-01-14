package com.example.groceryapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryapp.adapter.MyCartAdapter
import com.example.groceryapp.databinding.ActivityCartsBinding
import com.example.groceryapp.model.CustomerModel
import com.example.groceryapp.model.MyCartModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class CartsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartsBinding
    private lateinit var myCartAdapter: MyCartAdapter
    private lateinit var cartList: ArrayList<MyCartModel>
    private lateinit var db: FirebaseFirestore
    private lateinit var textView: TextView
    private lateinit var auth: FirebaseAuth
    private var paymentMethod: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        textView = findViewById(R.id.totalProductPriceId)

        binding.cartRecycleViewId.layoutManager = LinearLayoutManager(this)
        cartList = arrayListOf()
        myCartAdapter = MyCartAdapter(this, cartList)
        binding.cartRecycleViewId.adapter = myCartAdapter




        val amount = intent.getIntArrayExtra("totalAmount")
        Log.e("amount", amount.toString())

        LocalBroadcastManager.getInstance(applicationContext)
            .registerReceiver(mMessae, IntentFilter("MyTotalAmount"))


        db = FirebaseFirestore.getInstance()
        ///get cart added user product
        db.collection("currentUser").document(auth.currentUser!!.uid)
                .collection("AddToCart").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {

                    if (dc.type == DocumentChange.Type.ADDED) {
                     val cart: MyCartModel =  dc.document.toObject(MyCartModel::class.java)
                        ///delete item
                        val id =  dc.document.id
                        cart.documentId = id
                        cartList.add(cart)

                    }
                }
                myCartAdapter.notifyDataSetChanged()
            }
        })


        binding.buyNow.setOnClickListener{
            alertDialog()
        }
    }

    private fun alertDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Customser Address")

        val view = LayoutInflater.from(applicationContext).inflate(R.layout.customer_details_layout, null)
        val address = view.findViewById<EditText>(R.id.customerAddressEtId)
        val phone = view.findViewById<EditText>(R.id.customerPhoneEtId)
        val userMap = view.findViewById<ImageView>(R.id.mapImageBtnId)
        val radioGroup = view.findViewById<RadioGroup>(R.id.customerRadioGroupId)
        val cashPaymet = view.findViewById<RadioButton>(R.id.cashPaymentId)
        val cartPayment = view.findViewById<RadioButton>(R.id.cartPaymentId)
        val saveBtn = view.findViewById<Button>(R.id.customerInfoSaveBtnId)
        builder.setView(view)

        userMap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }


        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            cartPayment.setOnClickListener {
                paymentMethod = "Cart Payment"
                val intent = Intent(this, CartPaymentActivity::class.java)
                startActivity(intent)
            }
            if (checkedId == R.id.cashPaymentId){
                paymentMethod = "Cash Payement"
            }
        }

        val customerAddress  = intent.getStringExtra("main")
        address.setText(customerAddress)

        saveBtn.setOnClickListener {

            val cPhone = phone.text.toString()
            val getAddress = address.text.toString()
            val payment = CustomerModel(getAddress,cPhone, paymentMethod)

            db.collection("CustomerInfo").document(auth.currentUser!!.uid).collection("payment").document().set(payment).addOnCompleteListener { task->
                if (task.isSuccessful){
                    Toast.makeText(this,"save customer information", Toast.LENGTH_SHORT).show()
                }
                else{
                    Log.e("main","customer info : ${task.exception.toString()}")
                }
            }
        }
        builder.create()
        builder.show()

    }

    var mMessae: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val totalBill = intent.getIntExtra("totalamount", 0)
         //   binding.tvTotalAmountId.text = "Total Amount ${totalBill}"
            Log.e("price", "Amount ${totalBill}")

        }
    }

}



