package com.example.groceryapp.admincalss

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.groceryapp.databinding.ActivitySubAdminCategoryBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

class SubAdminCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubAdminCategoryBinding
    private var imageUri: Uri? = null
    private var myUrl = ""
    private lateinit var db: FirebaseFirestore
    private var storagePostPicRef: StorageReference?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubAdminCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = intent.getStringExtra("category")


       db = FirebaseFirestore.getInstance()
        storagePostPicRef = FirebaseStorage.getInstance().reference.child("All Product Picture")

        binding.subAdminCateImg.setOnClickListener{
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 77)
            Toast.makeText(this,"picture uploaded", Toast.LENGTH_SHORT).show()
        }

        binding.adminUploadProductBtnId.setOnClickListener{
            saveProduct()
        }
    }

    private fun saveProduct() {
        val productName = binding.subAdminCateProductName.text.toString()
        val productPrice = binding.subAdminProductPrice.text.toString()
        val productType = binding.adminProductTypeId.text.toString()
        val productDes = binding.subAdminProductdescription.text.toString()
        val rating = binding.adminProductRatingId.text.toString()

        if (productName.isEmpty()){
            binding.subAdminCateProductName.setError("Enter your Product Name")
        }
       else if (productPrice.isEmpty()){
            binding.subAdminProductPrice.setError("Enter your Product price")
        }
        else if (productType.isEmpty()){
            binding.adminProductTypeId.setError("Enter your Product Type")
        }
        else if (productDes.isEmpty()){
            binding.subAdminProductdescription.setError("Enter your Product Description")
        }
        else if (rating.isEmpty()){
            binding.adminProductRatingId.setError("Enter your Product Description")
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
                    userInfo["name"] = productName
                    userInfo["price"] = productPrice.toLong()
                    userInfo["description"] = productDes
                    userInfo["type"] = productType
                    userInfo["img_url"] = myUrl
                    userInfo["rating"] = rating

                    db.collection("viewAllProducts").add(userInfo).addOnCompleteListener { task->
                        if (task.isSuccessful){
                            Toast.makeText(this,"product added successfull",Toast.LENGTH_SHORT).show()
                        }
                    }


                }

            })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== 77 && resultCode == Activity.RESULT_OK && data != null ){
            imageUri = data.data
            binding.subAdminCateImg.setImageURI(imageUri)

            Toast.makeText(this,"picture uploaded", Toast.LENGTH_SHORT).show()
        }
    }
}