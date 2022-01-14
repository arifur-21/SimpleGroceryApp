package com.example.groceryapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.groceryapp.databinding.ActivityProfileBinding
import com.example.groceryapp.model.UserModel
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var imageUri: Uri? = null
    private var myUrl = ""
    private lateinit var db: FirebaseFirestore
    private var storagePostPicRef: StorageReference?=null
    private lateinit var userDatabaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        storagePostPicRef = FirebaseStorage.getInstance().reference.child("Prifile Picture")
        userDatabaseRef = FirebaseDatabase.getInstance().reference.child("users")


        binding.profileImgId.setOnClickListener{
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 77)
        }

        ///add user information
        binding.proidleUploadBtnId.setOnClickListener{
            val name = binding.profileUserNameId.text.toString()
            val email = binding.profileEmailNameId.text.toString()
            val phone = binding.profilePhoneId.text.toString()
            val address = binding.profileAddressId.text.toString()

            if (imageUri == null){
                Toast.makeText(this, "Select Profile Image", Toast.LENGTH_SHORT).show()
            }

           else if (name.isEmpty()){
                binding.profileUserNameId.setError("Entery your Name")
            }
            else if (email.isEmpty()){
                binding.profileEmailNameId.setError("Entery your Email")
            }
            else if (phone.isEmpty()){
                binding.profilePhoneId.setError("Entery your phone number")
            }
            else if (address.isEmpty()){
                binding.profileAddressId.setError("Entery your Address")
            }
            else{

                /// store image firebaseStorage and get image url
                val fileRef = storagePostPicRef!!.child(System.currentTimeMillis().toString() + ".jpg")
                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw  it
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener(OnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val userInfo: HashMap<String, Any> = HashMap()
                        userInfo["name"] = name
                        userInfo["email"] = email
                        userInfo["pnone"] = phone
                        userInfo["address"] = address
                        userInfo["image"] = myUrl

                        userDatabaseRef.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(userInfo).addOnCompleteListener { task->
                            if (task.isSuccessful){
                                Toast.makeText(this,"user info uploaded successfull",Toast.LENGTH_SHORT).show()
                            }
                        }

                    }

                })

            }
        }


        /// get user profile image
        userDatabaseRef.child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)

                if (user?.image == null){
                    binding.profileImgId.setImageResource(R.drawable.profile)
                }
                else{
                    Picasso.get().load(user!!.image).placeholder(R.drawable.profile).into(binding.profileImgId)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== 77 && resultCode == Activity.RESULT_OK && data != null ){
            imageUri = data.data
            binding.profileImgId.setImageURI(imageUri)

            Toast.makeText(this,"picture uploaded",Toast.LENGTH_SHORT).show()
        }
    }
}