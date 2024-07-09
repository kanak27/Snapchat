package com.example.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText : EditText
    private lateinit var passwordEditText: EditText
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        auth = Firebase.auth

        if(auth.currentUser != null){
            logIn()
        }
    }

    fun goClicked(view : View){
        // Check if login is possible
        auth.signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    logIn()
                } else {
                    // If sign in fails, try to sign up a new user.
                    auth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if(task.isSuccessful){
                                // Add to database
                                task.result.user?.let {
                                    FirebaseDatabase.getInstance("https://snapchat-1-febc1-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users").child(
                                        it.uid).child("email").setValue(emailEditText.text.toString())
                                }
                                logIn()
                            }
                            else{
                                Toast.makeText(
                                    baseContext,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                }
            }
    }

    private fun logIn(){
        //Move to next activity
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }

}