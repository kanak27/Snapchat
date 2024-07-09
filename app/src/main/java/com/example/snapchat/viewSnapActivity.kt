package com.example.snapchat

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class viewSnapActivity : AppCompatActivity() {

    private var messageTextView : TextView? = null
    private var snapImageView : ImageView? = null
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)

        messageTextView = findViewById(R.id.messageTextView)
        snapImageView = findViewById(R.id.snapImageView)

        auth = Firebase.auth

        messageTextView?.text = intent.getStringExtra("message")

        val task : ImageDownloader = @SuppressLint("StaticFieldLeak")
        object : ImageDownloader(){}

        val myImage : Bitmap

        try {
            myImage = task.execute(intent.getStringExtra("imageURL")).get()

            snapImageView?.setImageBitmap(myImage)
        } catch (e : Exception){
            e.printStackTrace()
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    open class ImageDownloader : AsyncTask<String, Void, Bitmap>(){
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg urls: String?): Bitmap? {
            return try {
                val url = URL(urls[0])
                val connection : HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.connect()

                val input : InputStream = connection.inputStream

                val myBitmap : Bitmap = BitmapFactory.decodeStream(input)

                myBitmap

            } catch(e : Exception){
                e.printStackTrace()
                null
            }
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            auth.currentUser?.uid?.let {
                intent?.getStringExtra("snapKey")?.let { it1 ->
                    FirebaseDatabase.getInstance("https://snapchat-1-febc1-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("users").child(
                        it
                    ).child("snaps").child(it1).removeValue()
                }
            }

            FirebaseStorage.getInstance().reference.child("images").child(intent.getStringExtra("imageName")!!).delete()

            val intent = Intent(this@viewSnapActivity, SnapsActivity::class.java)
            startActivity(intent)
        }
    }
}