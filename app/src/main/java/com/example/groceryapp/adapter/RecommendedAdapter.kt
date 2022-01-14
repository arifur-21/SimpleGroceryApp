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
import com.example.groceryapp.model.RecommendetModel
import com.squareup.picasso.Picasso

class RecommendedAdapter(private val context:Context, private val recomList: List<RecommendetModel>): RecyclerView.Adapter<RecommendedAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recommended_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reList = recomList[position]
        holder.rating.text = reList.rating
        holder.description.text = reList.description
        holder.name.text = reList.name
        holder.price.text ="TK."+reList.price.toLong().toString()
        Picasso.get().load(reList.img_url).placeholder(R.drawable.milk).into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("item", recomList.get(position))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return recomList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name  = itemView.findViewById<TextView>(R.id.recommended_nameId)
        val description  = itemView.findViewById<TextView>(R.id.recommended_desId)
        val rating  = itemView.findViewById<TextView>(R.id.recommended_ratingId)
        val price  = itemView.findViewById<TextView>(R.id.recommended_PriceId)
        val image = itemView.findViewById<ImageView>(R.id.recommended_imageId)
    }
}