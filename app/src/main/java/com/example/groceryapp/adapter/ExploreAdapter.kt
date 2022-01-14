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
import com.example.groceryapp.ViewAllProduct
import com.example.groceryapp.model.ExploreModel
import com.squareup.picasso.Picasso

class ExploreAdapter(private var context: Context, private var cateList: ArrayList<ExploreModel>): RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.explore_cate_item,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val category = cateList[position]
        holder.catename.text = category.name
        Picasso.get().load(category.img_url).placeholder(R.drawable.milk).into(holder.cateImg)

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ViewAllProduct::class.java)
            intent.putExtra("type", category.type)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return  cateList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val cateImg = itemView.findViewById<ImageView>(R.id.home_cate_imgId)
        val catename = itemView.findViewById<TextView>(R.id.home_cate_nameId)

    }
}