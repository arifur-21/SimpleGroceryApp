package com.example.groceryapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapp.R
import com.example.groceryapp.SubCategoryActivity
import com.example.groceryapp.model.NavCategoryModel
import com.squareup.picasso.Picasso

class NavCategoryAdapter (var context: Context, var navCateList: ArrayList<NavCategoryModel>): RecyclerView.Adapter<NavCategoryAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.nav_cate_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = navCateList[position]
        holder.description.text = category.desciption
        holder.discount.text = category.discount.toString()
        holder.name.text = category.name
        Picasso.get().load(category.img_url).placeholder(R.drawable.profile).into(holder.image)

        holder.itemView.setOnClickListener{
            val intent = Intent(Intent(context, SubCategoryActivity::class.java))
            intent.putExtra("type", category.type)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return navCateList.size
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var name = itemView.findViewById<TextView>(R.id.cae_nav_nameId)
        var description = itemView.findViewById<TextView>(R.id.cae_nav_descriptionId)
        var discount = itemView.findViewById<TextView>(R.id.cae_nav_discountId)
        var image = itemView.findViewById<ImageView>(R.id.cate_nav_img)
    }
}