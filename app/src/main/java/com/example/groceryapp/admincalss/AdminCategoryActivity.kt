package com.example.groceryapp.admincalss

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.groceryapp.databinding.ActivityAdminCategoryBinding

class AdminCategoryActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAdminCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addAllProductId.setOnClickListener{
            val intent = Intent(this, SubAdminCategoryActivity::class.java)
            startActivity(intent)
        }
        binding.addPopularId.setOnClickListener{
            val intent1 = Intent(this, AddAdminPopularProductActivity::class.java)
            startActivity(intent1)
        }
        binding.addrecommentedId.setOnClickListener{
            val intent2 = Intent(this, AdminRecommentedActivity::class.java)
            startActivity(intent2)
        }

    }
}