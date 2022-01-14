package com.example.groceryapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.groceryapp.admincalss.AdminCategoryActivity
import com.example.groceryapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val preference = getSharedPreferences("checkbox", MODE_PRIVATE)
       val checkBox = preference.getString("remember","")
        if (checkBox.equals("true")){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else if (checkBox.equals("false")){
            Toast.makeText(this,"Please Sign In", Toast.LENGTH_SHORT).show()
        }

        binding.adminPenalLink.setOnClickListener{

            val intent = Intent(this, AdminCategoryActivity::class.java)
            startActivity(intent)

        }

       /* if(firebaseAuth !=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }*/

        binding.signUpTextView.setOnClickListener{
            val intent = Intent(Intent(this, RegisterActivity::class.java))
            startActivity(intent)
            finish()
        }


        binding.signInBtnId.setOnClickListener{
            userAuth()
        }

      /*  binding.rememberMeId.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isChecked){
                val myPref = getSharedPreferences("checkbox", MODE_PRIVATE)
                val editor = myPref.edit()
                editor.putString("remember", "true")
                editor.apply()
                Toast.makeText(this,"Checked", Toast.LENGTH_SHORT).show()
            }else if (!buttonView.isChecked){
                val myPref = getSharedPreferences("checkbox", MODE_PRIVATE)
                val editor = myPref.edit()
                editor.putString("remember", "false")
                editor.apply()
                Toast.makeText(this,"UnChecked", Toast.LENGTH_SHORT).show()
            }
        }*/
    }

    private fun userAuth() {
        val email = binding.loginEmailId.text.toString()
        val password = binding.loginPassword.text.toString()

        if (email.isEmpty()){
            binding.loginEmailId.setError("Enter your Email Address")
        }
        else if (password.isEmpty()){
            binding.loginPassword.setError("Enter your Passwrd")
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task->
                if (!task.isSuccessful){
                    Log.e("log", "error${task.exception.toString()}")
                    Toast.makeText(this, "Error ${task.exception.toString()}", Toast.LENGTH_SHORT).show()
                }
                else{

                    val intent = Intent(Intent(this, MainActivity::class.java))
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
    }
