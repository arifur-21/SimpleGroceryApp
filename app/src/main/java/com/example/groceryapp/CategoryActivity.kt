package com.example.groceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryapp.adapter.NavCategoryAdapter
import com.example.groceryapp.databinding.ActivityCategoryBinding
import com.example.groceryapp.model.NavCategoryModel
import com.google.firebase.firestore.*

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding
    private lateinit var navCateAdapter: NavCategoryAdapter
    private lateinit var navCateList: ArrayList<NavCategoryModel>

    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.categoryRecycleViewId.layoutManager = LinearLayoutManager(applicationContext)
        binding.categoryRecycleViewId.setHasFixedSize(true)
        navCateList = arrayListOf()
        navCateAdapter = NavCategoryAdapter(this, navCateList)
        binding.categoryRecycleViewId.adapter = navCateAdapter

        db.collection("navCategory").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        navCateList.add(dc.document.toObject(NavCategoryModel::class.java))
                    }
                }
                navCateAdapter.notifyDataSetChanged()
            }

        })
    }
}