package com.example.snapchat

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.UUID


class createSnap : AppCompatActivity() {

    private var createSnapImageView : ImageView? = null
    private var messageEditText : EditText? = null
    private val imageName = UUID.randomUUID().toString() + ".jpg"

    private fun getPhoto(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getPhoto()
            }
        }
    }

    fun chooseImage(view: View){
        if(checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 1)
        } else{
            getPhoto()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val selectedImage = data?.data

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                createSnapImageView?.setImageBitmap(bitmap)
            } catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    fun next(view: View){
        createSnapImageView?.isDrawingCacheEnabled = true
        createSnapImageView?.buildDrawingCache()
        val bitmap = (createSnapImageView?.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val storage = Firebase.storage
        val storageReference = storage.reference.child("images").child(imageName)

        val uploadTask = storageReference.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
            Log.i("Status", "Unsuccessful Upload")
        }.addOnSuccessListener(this
        ) { taskSnapshot -> //Uri url = taskSnapshot.getDownloadUrl();
            val uri = taskSnapshot.storage.downloadUrl
            while (!uri.isComplete);
            val url = uri.result
            Log.i("URL ", url.toString())

            val intent = Intent(this, ChooseUser::class.java)
            intent.putExtra("imageURL", url.toString())
            intent.putExtra("imageName", imageName)
            intent.putExtra("message", messageEditText?.text.toString())
            startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_snap)

        createSnapImageView = findViewById(R.id.createSnapImageView)
        messageEditText = findViewById(R.id.messageEditText)
    }
}