package com.example.groceryapp.customercare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapp.R
import com.example.groceryapp.customer.MessageAdapter
import com.example.groceryapp.customer.UserMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CustomerCareActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var button: ImageView

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private lateinit var messageAdapter : MessageAdapter
    private lateinit var msgList: ArrayList<UserMessage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_care)

        recyclerView = findViewById(R.id.cus_CareRecycleViewId)
        etMessage = findViewById(R.id.cus_CaremessageBox)
        button = findViewById(R.id.cus_CaresentBtnId)


        msgList = arrayListOf()
        messageAdapter = MessageAdapter(msgList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference

        val senderUid = auth.currentUser!!.uid
        val ricever = senderUid + System.currentTimeMillis()


        dbRef.child("chats").child(senderUid!!).child("message").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                msgList.clear()

                for (messageSn in snapshot.children){
                    val msg = messageSn.getValue(UserMessage::class.java)
                    msgList.add(msg!!)
                    Log.e("msg","message${msg!!.message}")
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        dbRef.child("chat").child(senderUid!!).child("message").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                msgList.clear()

                for (messageSn in snapshot.children){
                    val msg = messageSn.getValue(UserMessage::class.java)
                    msgList.add(msg!!)
                    Log.e("msg","message${msg!!.message}")
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


       button.setOnClickListener {

           val messgae = etMessage.text.toString()
           val objMessage = UserMessage(messgae, senderUid)

           dbRef.child("chat").child(senderUid).child("message").push()
                   .setValue(objMessage).addOnCompleteListener {
                       dbRef.child("chat").child(ricever).child("message").push()
                               .setValue(objMessage)
                       Toast.makeText(this,"Send", Toast.LENGTH_SHORT).show()
                   }
           etMessage.setText("")
       }


    }
}