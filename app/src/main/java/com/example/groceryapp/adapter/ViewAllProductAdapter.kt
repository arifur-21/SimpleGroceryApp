package com.example.groceryapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapp.DetailsActivity
import com.example.groceryapp.R
import com.example.groceryapp.model.ViewAllModel
import com.squareup.picasso.Picasso

class ViewAllProductAdapter(var context: Context, var allProductList: List<ViewAllModel>): RecyclerView.Adapter<ViewAllProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       var view = LayoutInflater.from(context).inflate(R.layout.view_all_item,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var allProduct = allProductList[position]

        holder.name.text = allProduct.name
        holder.description.text = allProduct.description
        holder.price.text = allProduct.price.toLong().toString()
        holder.rating.text = allProduct.rating
        Picasso.get().load(allProduct.img_url).placeholder(R.drawable.profile).into(holder.image)

        holder.itemView.setOnClickListener{
            val intent = Intent(Intent(context, DetailsActivity::class.java))
            intent.putExtra("detail", allProductList.get(position))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return allProductList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var name = itemView.findViewById<TextView>(R.id.viewAll_nameId)
        var description = itemView.findViewById<TextView>(R.id.viewAll_descriptionId)
        var price = itemView.findViewById<TextView>(R.id.viewAll_priceId)
        var rating = itemView.findViewById<TextView>(R.id.viewAll_ratingId)
        var image = itemView.findViewById<ImageView>(R.id.viewAll_img)
    }
}