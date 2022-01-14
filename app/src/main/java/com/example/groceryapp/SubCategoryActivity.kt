package com.example.groceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapp.adapter.SubCategoryAdapter
import com.example.groceryapp.model.SubCategoryModel
import com.google.firebase.firestore.*

class SubCategoryActivity : AppCompatActivity() {
    private lateinit var subCategoryAdapter: SubCategoryAdapter
    private lateinit var subList: List<SubCategoryModel>
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)

        recyclerView = findViewById(R.id.subCateRecyViewId)
        var type = intent.getStringExtra("type")



        recyclerView.layoutManager = LinearLayoutManager(this)
        subList = arrayListOf()
        subCategoryAdapter = SubCategoryAdapter(this, subList)
        recyclerView.adapter = subCategoryAdapter

        db = FirebaseFirestore.getInstance()
            if (type != null && type.contentEquals("drink")) {
                db.collection("viewAllProducts").whereEqualTo("type", "drink")
                    .addSnapshotListener(object : EventListener<QuerySnapshot> {
                        override fun onEvent(
                            value: QuerySnapshot?,
                            error: FirebaseFirestoreException?
                        ) {
                            if (error != null) {
                                Log.e("error", error.message.toString())
                                return
                            }
                            for (dc: DocumentChange in value?.documentChanges!!) {
                                if (dc.type == DocumentChange.Type.ADDED) {
                                    (subList as ArrayList<SubCategoryModel>).add(
                                        dc.document.toObject(
                                            SubCategoryModel::class.java

                                        )
                                    )

                                }
                            }
                            subCategoryAdapter.notifyDataSetChanged()
                        }

                    })

            }




    }
}