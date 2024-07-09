package com.example.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SnapsActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private var unopenedSnaps : ListView? = null
    private var emails : ArrayList<String> = ArrayList()
    var snaps: ArrayList<DataSnapshot> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)
        setSupportActionBar(findViewById(R.id.menuBar))
        title = "Current Snaps"

        unopenedSnaps = findViewById(R.id.unopenedSnaps)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,emails)
        unopenedSnaps?.adapter = adapter
        auth = Firebase.auth

        auth.currentUser?.uid?.let {
            FirebaseDatabase.getInstance("https://snapchat-1-febc1-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("users").child(
                it
            ).child("snaps").addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    emails.add(snapshot.child("from").value as String)
                    snaps.add(snapshot)
                    adapter.notifyDataSetChanged()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {
                    for((index, snap : DataSnapshot) in snaps.withIndex()){
                        if(snap.key == snapshot.key){
                            snaps.removeAt(index)
                            emails.removeAt(index)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}

            })
        }

        unopenedSnaps?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val snapshot = snaps[position]

            val intent = Intent(this, viewSnapActivity::class.java)
            intent.putExtra("imageName", snapshot.child("imageName").value as String)
            intent.putExtra("imageURL", snapshot.child("imageURL").value as String)
            intent.putExtra("message", snapshot.child("message").value as String)
            intent.putExtra("snapKey", snapshot.key)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.createSnap){
            val intent = Intent(this, createSnap::class.java)
            startActivity(intent)
        } else if(item.itemId == R.id.logout){
            auth.signOut()
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            auth.signOut()

            val intent = Intent(this@SnapsActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}