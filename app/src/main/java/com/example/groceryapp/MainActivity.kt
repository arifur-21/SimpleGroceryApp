package com.example.groceryapp

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapp.adapter.ExploreAdapter
import com.example.groceryapp.adapter.PopularAdapter
import com.example.groceryapp.adapter.RecommendedAdapter
import com.example.groceryapp.adapter.ViewAllProductAdapter
import com.example.groceryapp.customer.MessageAdapter
import com.example.groceryapp.customer.UserMessage
import com.example.groceryapp.customercare.CustomerCareActivity
import com.example.groceryapp.databinding.ActivityMainBinding
import com.example.groceryapp.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
    //populor product
    private lateinit var popularAdapter: PopularAdapter
    private lateinit var popularList: List<PopularModel>
    ///category list
    private lateinit var homeCateAdapter: ExploreAdapter
    protected lateinit var cateList: List<ExploreModel>
    ///recommended producl
    private lateinit var recommendedAdapter: RecommendedAdapter
    private lateinit var recommendedList: List<RecommendetModel>

    ////search View
    private lateinit var viewAllList: List<ViewAllModel>
    private lateinit var viewAllProductAdapter: ViewAllProductAdapter

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

  private lateinit var messageAdapter : MessageAdapter
  private lateinit var msgList: ArrayList<UserMessage>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference

        binding.srcollViewId.visibility = View.GONE
        binding.mainProgressBarId.visibility = View.VISIBLE



        val nav_name = findViewById<TextView>(R.id.nav_headerUserNameId)
            val nav_email = findViewById<TextView>(R.id.nav_headerTvEmailId)
            val nav_image = findViewById<ImageView>(R.id.nav_headerProImageId)


        /*userRef = FirebaseDatabase.getInstance().reference.child("users")
        userRef.child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               if (snapshot.exists()){
                   val name = snapshot.child("name").getValue().toString()
                   val email = snapshot.child("email").getValue().toString()
                   val image = snapshot.child("image").getValue().toString()
                   nav_name.text = name
                   nav_email.text = email
                   Picasso.get().load(image).placeholder(R.drawable.profile).into(nav_image)
               }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })*/

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener{
            messageDialog()
        }

        val drawerToggle : ActionBarDrawerToggle = object : ActionBarDrawerToggle(
                this,
                binding.drawerLayoutId,
                binding.toolbarId,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ){
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                setTitle("Drawer Closeed")
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                setTitle("Drawer Opened")
            }
        }
        drawerToggle.isDrawerIndicatorEnabled = true
        binding.drawerLayoutId.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

       binding.navViewId.setNavigationItemSelectedListener { item: MenuItem ->
           when(item.itemId){

               R.id.honeMenuId -> {
                   val intent4 = Intent(this@MainActivity, MainActivity::class.java)
                   startActivity(intent4)
               }
               R.id.profileMenuId -> {
                   val intent4 = Intent(this@MainActivity, ProfileActivity::class.java)
                   startActivity(intent4)
               }
               R.id.categoryMenuId -> {
                   val intent4 = Intent(this@MainActivity, CategoryActivity::class.java)
                   startActivity(intent4)
               }
               R.id.cartMenuId -> {
                   val intent5 = Intent(this@MainActivity, CartsActivity::class.java)
                   startActivity(intent5)
               }
               R.id.logOutId -> {
                   FirebaseAuth.getInstance().signOut()
                   val intent5 = Intent(this@MainActivity, LoginActivity::class.java)
                   startActivity(intent5)
               }
               R.id.orderMenuId -> {
                   val intent6 = Intent(this@MainActivity, CartsActivity::class.java)
                   startActivity(intent6)
               }

               R.id.customerCareId -> {
                   val intent7 = Intent(this@MainActivity, CustomerCareActivity::class.java)
                   startActivity(intent7)
               }
           }
           true
       }



        //popular product
        binding.popRecycleViewId.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        popularList = arrayListOf()
        popularAdapter = PopularAdapter(this, popularList as ArrayList<PopularModel>)
        binding.popRecycleViewId.adapter = popularAdapter

        //Explore caterogy
        binding.exploreRecycleViewId.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        cateList = arrayListOf()
        homeCateAdapter = ExploreAdapter(this, cateList as ArrayList<ExploreModel>)
        binding.exploreRecycleViewId.adapter = homeCateAdapter

        //recommended product
        binding.recommendedRecId.layoutManager = GridLayoutManager(this, 2)
        recommendedList = arrayListOf()
        recommendedAdapter = RecommendedAdapter(this, recommendedList)
        binding.recommendedRecId.adapter = recommendedAdapter

        db = FirebaseFirestore.getInstance()
        ///get popular product from firebase firestore
        db.collection("popularProduct").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {

                        (popularList as ArrayList<PopularModel>).add(dc.document.toObject(PopularModel::class.java))

                        binding.mainProgressBarId.visibility = View.GONE
                        binding.srcollViewId.visibility = View.VISIBLE


                    }
                }
            }

        })

        //Explore category product
        db.collection("explore product").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        (cateList as ArrayList<ExploreModel>).add(dc.document.toObject(ExploreModel::class.java))
                    }
                }
            }

        })

        //get recommended product
        db.collection("recommended product").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        (recommendedList as ArrayList<RecommendetModel>).add(dc.document.toObject(RecommendetModel::class.java))
                    }
                }
            }

        })

        ///// search view
        binding.searchRecycleViewId.layoutManager = LinearLayoutManager(this)
        viewAllList = arrayListOf()
        viewAllProductAdapter = ViewAllProductAdapter(this, viewAllList)
        binding.recommendedRecId.setHasFixedSize(true)
        binding.searchRecycleViewId.adapter = viewAllProductAdapter

        binding.searchBoxEtId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                binding.searchRecycleViewId.visibility = View.VISIBLE
                binding.recommendedRecId.visibility = View.GONE
                binding.popRecycleViewId.visibility = View.GONE
                binding.exploreRecycleViewId.visibility = View.GONE
                if (s.toString().isEmpty()) {
                    (viewAllList as ArrayList<ViewAllModel>).clear()
                    viewAllProductAdapter.notifyDataSetChanged()
                } else {
                    searchProduct(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })



    }

    private fun messageDialog() {
        val builder =  AlertDialog.Builder(this)
        builder.setTitle("Message")
        val itemVeiw = LayoutInflater.from(this).inflate(R.layout.message_dialog_layout, null)

        val send = itemVeiw.findViewById<ImageView>(R.id.sentBtnId)
        val etMessage = itemVeiw.findViewById<EditText>(R.id.messageBox)
        val recyclerView = itemVeiw.findViewById<RecyclerView>(R.id.chattRecycleViewId)

        msgList = arrayListOf()
        messageAdapter = MessageAdapter(msgList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter


        val senderUid = auth.currentUser!!.uid
        val ricever = senderUid + System.currentTimeMillis()


        dbRef.child("chat").child(senderUid!!).child("message").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                msgList.clear()

                for (messageSn in snapshot.children){
                    val msg = messageSn.getValue(UserMessage::class.java)
                    msgList.add(msg!!)
                    Log.e("msg","message${msg!!.message}")
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        send.setOnClickListener {

            val messgae = etMessage.text.toString()
            val objMessage = UserMessage(messgae, senderUid)



           dbRef.child("chats").child(senderUid).child("message").push()
                   .setValue(objMessage).addOnCompleteListener {
                       dbRef.child("chats").child(ricever).child("message").push()
                               .setValue(objMessage)
                       Toast.makeText(this,"Send", Toast.LENGTH_SHORT).show()
                   }
            etMessage.setText("")

        }
        builder.setView(itemVeiw)

        val dialog = builder.create()
        dialog.show()



    }

    ///search

    private fun searchProduct(type: String) {

        if (!type.isEmpty()) {

db.collection("viewAllProducts").whereEqualTo("type", "drink").addSnapshotListener(object : EventListener<QuerySnapshot> {
    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            Log.e("error", error.message.toString())
            return
        }
        for (dc: DocumentChange in value?.documentChanges!!) {

            if (dc.type == DocumentChange.Type.ADDED) {
                (viewAllList as ArrayList<ViewAllModel>).add(dc.document.toObject(ViewAllModel::class.java))
            }
        }
        viewAllProductAdapter.notifyDataSetChanged()
    }

})

db.collection("viewAllProducts").whereEqualTo("type", "vegetable").addSnapshotListener(object : EventListener<QuerySnapshot> {
    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            Log.e("error", error.message.toString())
            return
        }
        for (dc: DocumentChange in value?.documentChanges!!) {

            if (dc.type == DocumentChange.Type.ADDED) {
                (viewAllList as ArrayList<ViewAllModel>).add(dc.document.toObject(ViewAllModel::class.java))
            }
        }
        viewAllProductAdapter.notifyDataSetChanged()
    }

})

db.collection("viewAllProducts").whereEqualTo("type", "fish").addSnapshotListener(object : EventListener<QuerySnapshot> {
    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            Log.e("error", error.message.toString())
            return
        }
        for (dc: DocumentChange in value?.documentChanges!!) {

            if (dc.type == DocumentChange.Type.ADDED) {
                (viewAllList as ArrayList<ViewAllModel>).add(dc.document.toObject(ViewAllModel::class.java))
            }
        }
        viewAllProductAdapter.notifyDataSetChanged()
    }

})

db.collection("viewAllProducts").whereEqualTo("type", "rice").addSnapshotListener(object : EventListener<QuerySnapshot> {
    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            Log.e("error", error.message.toString())
            return
        }
        for (dc: DocumentChange in value?.documentChanges!!) {

            if (dc.type == DocumentChange.Type.ADDED) {
                (viewAllList as ArrayList<ViewAllModel>).add(dc.document.toObject(ViewAllModel::class.java))
            }
        }
        viewAllProductAdapter.notifyDataSetChanged()
    }

})

db.collection("viewAllProducts").whereEqualTo("type", "fruits").addSnapshotListener(object : EventListener<QuerySnapshot> {
    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            Log.e("error", error.message.toString())
            return
        }
        for (dc: DocumentChange in value?.documentChanges!!) {

            if (dc.type == DocumentChange.Type.ADDED) {
                (viewAllList as ArrayList<ViewAllModel>).add(dc.document.toObject(ViewAllModel::class.java))
            }
        }
        viewAllProductAdapter.notifyDataSetChanged()
    }

})

}
}
}