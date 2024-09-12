package com.example.AllAroundTheBirds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.AllAroundTheBirds.MenuHelper.SideMenuHelper
import com.example.AllAroundTheBirds.Model.Bird
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ViewCaptured : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var sideMenuHelper: SideMenuHelper
    private val aut: FirebaseAuth = FirebaseAuth.getInstance()
    private var user = aut.currentUser
    private lateinit var databaseReference: DatabaseReference
  //  private lateinit var adapter: BirdAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var birdAdapter: BirdAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_captured)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val contentLayout: LinearLayout = findViewById(R.id.content_Layout)
        sideMenuHelper = SideMenuHelper(this, drawerLayout)
        sideMenuHelper.setupMenu(navigationView, contentLayout)

        recyclerView = findViewById(R.id.listView)
        birdAdapter = BirdAdapter()

        // Set up RecyclerView
        recyclerView.adapter = birdAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch bird data and update the adapter
        fetchBirdsFromDatabase()
    }
    // ViewCaptured.kt
    private fun fetchBirdsFromDatabase() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child("${user?.uid}").child("birds")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val birdsList: MutableList<Bird> = mutableListOf()

                for (birdSnapshot in dataSnapshot.children) {
                    val name = birdSnapshot.child("name").getValue(String::class.java) ?: ""
                    val species = birdSnapshot.child("species").getValue(String::class.java) ?: ""
                    val imageUrl = birdSnapshot.child("imageUrl").getValue(String::class.java) ?: ""

                    val bird = Bird(name, species, imageUrl)
                    birdsList.add(bird)
                }

                birdAdapter.setBirds(birdsList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FirebaseError", "Error fetching birds: ${databaseError.message}")
            }
        })
    }


}