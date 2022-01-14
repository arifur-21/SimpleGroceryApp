package com.example.groceryapp.admincalss

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.groceryapp.databinding.ActivityAdminRecommentedBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

class  AdminRecommentedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminRecommentedBinding
    private var imageUri: Uri? = null
    private var myUrl = ""
    private lateinit var db: FirebaseFirestore
    private var storagePostPicRef: StorageReference?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRecommentedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = intent.getStringExtra("category")


        db = FirebaseFirestore.getInstance()
        storagePostPicRef = FirebaseStorage.getInstance().reference.child("Recommented Image")

        binding.adimRecoProductImgId.setOnClickListener{
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 77)
            Toast.makeText(this,"picture uploaded", Toast.LENGTH_SHORT).show()
        }

        binding.adminUploadRecoProductBtnId.setOnClickListener{
            saveProduct()
        }
    }
    private fun saveProduct() {
        val productName = binding.adminRecoProdutNmae.text.toString()
        val productPrice = binding.adminRecoProdutPrice.text.toString()
        val productType = binding.adminRecoProdutTypeId.text.toString()
        val productDes = binding.adminRecoProdutdescription.text.toString()


        if (productName.isEmpty()){
            binding.adminRecoProdutNmae.setError("Enter your Product Name")
        }
        else if (productPrice.isEmpty()){
            binding.adminRecoProdutPrice.setError("Enter your Product price")
        }
        else if (productType.isEmpty()){
            binding.adminRecoProdutTypeId.setError("Enter your Product Type")
        }
        else if (productDes.isEmpty()){
            binding.adminRecoProdutdescription.setError("Enter your Product Description")
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

                    db.collection("recommended product").add(userInfo).addOnCompleteListener { task->
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
            binding.adimRecoProductImgId.setImageURI(imageUri)

            Toast.makeText(this,"picture uploaded", Toast.LENGTH_SHORT).show()
        }
    }
}