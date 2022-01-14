package com.example.groceryapp.customer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapp.R
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val msglist: ArrayList<UserMessage>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT= 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1){
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.receive_layout, parent, false)
            return ReceiveViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.send_layout, parent, false)
            return SendViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMsg = msglist[position]
        if (holder.javaClass == SendViewHolder::class.java){

            val viewHolder = holder as SendViewHolder
            holder.semtMessage.text = currentMsg.message
        }else{
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMsg.text = currentMsg.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = msglist[position]
        if (FirebaseAuth.getInstance().currentUser!!.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }
       else{
           return  ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return msglist.size
    }

    class SendViewHolder(itemView: View):  RecyclerView.ViewHolder(itemView){
        val semtMessage = itemView.findViewById<TextView>(R.id.sent_messageId)

    }
    class ReceiveViewHolder(itemView: View):  RecyclerView.ViewHolder(itemView){
        val receiveMsg = itemView.findViewById<TextView>(R.id.receive_messageId)
    }
}