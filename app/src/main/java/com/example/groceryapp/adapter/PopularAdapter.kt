package com.example.groceryapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapp.DetailsActivity
import com.example.groceryapp.R
import com.example.groceryapp.ViewAllProduct
import com.example.groceryapp.model.PopularModel
import com.squareup.picasso.Picasso

class PopularAdapter(private var context: Context, private var popularList: ArrayList<PopularModel>): RecyclerView.Adapter<PopularAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.popular_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var popular = popularList[position]

        holder.name.text = popular.name
        holder.description.text = popular.description
        holder.dicount.text = popular.discount.toString()
        holder.price.text = "TK."+popular.price.toString()
        holder.rating.text = popular.rating
        Picasso.get().load(popular.img_url).placeholder(R.drawable.imag3).into(holder.popImg)

        holder.itemView.setOnClickListener{
                val intent = Intent(context,ViewAllProduct::class.java)
                intent.putExtra("type", popular.type)
                context.startActivity(intent)


        }
    }

    override fun getItemCount(): Int {
       return popularList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val popImg = itemView.findViewById<ImageView>(R.id.pop_imageId)
        val name = itemView.findViewById<TextView>(R.id.pop_nameId)
        val description = itemView.findViewById<TextView>(R.id.pop_desId)
        val rating = itemView.findViewById<TextView>(R.id.pop_ratingId)
        val dicount = itemView.findViewById<TextView>(R.id.pop_discountId)
        val price = itemView.findViewById<TextView>(R.id.pop_priceId)



    }
}