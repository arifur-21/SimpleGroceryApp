package com.example.groceryapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapp.adapter.NavCategoryAdapter
import com.example.groceryapp.adapter.ViewAllProductAdapter
import com.example.groceryapp.model.NavCategoryModel
import com.example.groceryapp.model.ViewAllModel
import com.google.firebase.firestore.*

class ViewAllProduct : AppCompatActivity() {

    private lateinit var viewAllProductAdapter: ViewAllProductAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAllList: List<ViewAllModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_product)

        recyclerView = findViewById(R.id.viewAllProRecycleViewId)
        db = FirebaseFirestore.getInstance()

        ///ite's from popular adapter
        var type = intent.getStringExtra("type")

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)
        viewAllList = arrayListOf()
        viewAllProductAdapter = ViewAllProductAdapter(this, viewAllList)
        recyclerView.adapter = viewAllProductAdapter




        ///get fruits type of data
        if (type != null && type.contentEquals("fruits")) {
            db.collection("viewAllProducts").whereEqualTo("type","fruits").addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.e("error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            (viewAllList as ArrayList<ViewAllModel>) .add(dc.document.toObject(ViewAllModel::class.java))
                        }
                    }
                    viewAllProductAdapter.notifyDataSetChanged()
                }

            })
        }

        //get milk type of data
        if (type != null && type.contentEquals("milk")) {
            db.collection("viewAllProducts").whereEqualTo("type","milk").addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.e("error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            (viewAllList as ArrayList<ViewAllModel>) .add(dc.document.toObject(ViewAllModel::class.java))
                        }
                    }
                    viewAllProductAdapter.notifyDataSetChanged()
                }

            })
        }
        //get vegetable type of data
        if (type != null && type.contentEquals("vegetable")) {
            db.collection("viewAllProducts").whereEqualTo("type","vegetable").addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.e("error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            (viewAllList as ArrayList<ViewAllModel>) .add(dc.document.toObject(ViewAllModel::class.java))
                        }
                    }
                    viewAllProductAdapter.notifyDataSetChanged()
                }

            })
        }

        /// get fish type of data
        if (type != null && type.contentEquals("fish")) {
            db.collection("viewAllProducts").whereEqualTo("type","fish").addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.e("error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            (viewAllList as ArrayList<ViewAllModel>) .add(dc.document.toObject(ViewAllModel::class.java))
                        }
                    }
                    viewAllProductAdapter.notifyDataSetChanged()
                }

            })
        }

        /// get rice type of data
        if (type != null && type.contentEquals("rice")) {
            db.collection("viewAllProducts").whereEqualTo("type","rice").addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.e("error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            (viewAllList as ArrayList<ViewAllModel>) .add(dc.document.toObject(ViewAllModel::class.java))
                        }
                    }
                    viewAllProductAdapter.notifyDataSetChanged()
                }

            })
        }

        /// get drink type of data
        if (type != null && type.contentEquals("drink")) {
            db.collection("viewAllProducts").whereEqualTo("type","drink").addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.e("error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            (viewAllList as ArrayList<ViewAllModel>) .add(dc.document.toObject(ViewAllModel::class.java))
                        }
                    }
                    viewAllProductAdapter.notifyDataSetChanged()
                }

            })
        }

     }
    }
