package com.example.groceryapp.admincalss

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.groceryapp.databinding.ActivityAddAdminPopularProductBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

class AddAdminPopularProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAdminPopularProductBinding
    private var imageUri: Uri? = null
    private var myUrl = ""
    private lateinit var db: FirebaseFirestore
    private var storagePostPicRef: StorageReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAdminPopularProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = intent.getStringExtra("category")


        db = FirebaseFirestore.getInstance()
        storagePostPicRef = FirebaseStorage.getInstance().reference.child("Popular Product Picture")

        binding.adimPopProductImgId.setOnClickListener{
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 77)
            Toast.makeText(this,"picture uploaded", Toast.LENGTH_SHORT).show()
        }

        binding.adminUploadPopularProductBtnId.setOnClickListener{
            saveProduct()
        }
    }
    private fun saveProduct() {
        val productName = binding.adminPopProdutNmae.text.toString()
        val productPrice = binding.adminPopProdutPrice.text.toString()
        val productType = binding.adminPopProdutTypeId.text.toString()
        val productDes = binding.adminPopProdutdescription.text.toString()
        val discountPrice = binding.adminPopProdutDiscountPrice.text.toString()
        val rating = binding.adminPopProdutRatingId.text.toString()


        if (productName.isEmpty()){
            binding.adminPopProdutNmae.setError("Enter your Product Name")
        }
        else if (productPrice.isEmpty()){
            binding.adminPopProdutPrice.setError("Enter your Product price")
        }
        else if (productType.isEmpty()){
            binding.adminPopProdutTypeId.setError("Enter your Product Type")
        }
        else if (productDes.isEmpty()){
            binding.adminPopProdutdescription.setError("Enter your Product Description")
        }
        else if (discountPrice.isEmpty()){
            binding.adminPopProdutDiscountPrice.setError("Enter your Product Discount ")
        }
        else if (rating.isEmpty()){
            binding.adminPopProdutRatingId.setError("Enter your Product Rating ")
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
                    userInfo["discount"] = discountPrice.toInt()
                    userInfo["img_url"] = myUrl
                    userInfo["rating"] = rating

                    db.collection("popularProduct").add(userInfo).addOnCompleteListener { task->
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
            binding.adimPopProductImgId.setImageURI(imageUri)

            Toast.makeText(this,"picture uploaded", Toast.LENGTH_SHORT).show()
        }
    }
}