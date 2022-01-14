package com.example.groceryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.groceryapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
       db = FirebaseFirestore.getInstance()

        binding.signInTextView.setOnClickListener{
            val intent = Intent(Intent(this, LoginActivity::class.java))
            startActivity(intent)
           finish()
        }
        binding.signUpBtnId.setOnClickListener{
            registerUser()
        }

    }

    private fun registerUser() {
        val userName = binding.regUserName.text.toString().trim()
        val email = binding.regEmailId.text.toString().trim()
        val password = binding.regPassword.text.toString().trim()

        if (userName.isEmpty()){
            binding.regUserName.setError("Enter your Name")
        }
        else if (email.isEmpty()){
            binding.regEmailId.setError("Enter your Email Address")
        }
        else if (password.isEmpty()){
            binding.regPassword.setError("Enter your Passwrd")
        }
        else{

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(applicationContext, "Error ${task.exception.toString()}", Toast.LENGTH_SHORT).show()
                } else {

                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    val userInfo: MutableMap<String, Any> = HashMap()

                    userInfo.put("email", email)
                    userInfo.put("username", userName)
                    userInfo.put("userId",userId)

                    databaseReference.child("user").child(userId).setValue(userInfo).addOnCompleteListener { task->
                        if(task.isSuccessful){
                            val intent = Intent(Intent(applicationContext, MainActivity::class.java))
                            startActivity(intent)
                            finish()
                        }
                    }


                }
            }



        }
    }
 

}