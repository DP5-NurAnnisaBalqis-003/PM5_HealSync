package com.example.cakemate

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Dashboard : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var userImage: ImageView
    private lateinit var userIcon: ImageView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var jaheImage: ImageView  // Reference to Kue Jahe image
    private lateinit var ondeImage: ImageView  // Reference to Onde-Onde image
    private lateinit var meteImage: ImageView  // Reference to Coklat Mete image
    private lateinit var almondImage: ImageView  // Reference to Almond image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize DrawerLayout, NavigationView, and BottomNavigationView
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        userImage = findViewById(R.id.userImage)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Initialize Kue Jahe, Onde-Onde, Coklat Mete, and Almond images
        jaheImage = findViewById(R.id.jaheImage)
        ondeImage = findViewById(R.id.ondeImage)
        meteImage = findViewById(R.id.meteImage)
        almondImage = findViewById(R.id.almondImage)

        // Set up click listener for Kue Jahe
        jaheImage.setOnClickListener {
            val intent = Intent(this, ResepJahe::class.java)
            startActivity(intent)
        }

        // Set up click listener for Onde-Onde
        ondeImage.setOnClickListener {
            val intent = Intent(this, ResepOnde::class.java)
            startActivity(intent)
        }

        // Set up click listener for Coklat Mete
        meteImage.setOnClickListener {
            val intent = Intent(this, ResepMete::class.java)
            startActivity(intent)
        }

        // Set up click listener for Almond
        almondImage.setOnClickListener {
            val intent = Intent(this, ResepAlmond::class.java)
            startActivity(intent)
        }

        // Get the header view from NavigationView and set up user image and username
        val headerView = navigationView.getHeaderView(0)
        userIcon = headerView.findViewById(R.id.userIcon)
        val usernameTextView = headerView.findViewById<TextView>(R.id.username)

        // Default username
        usernameTextView.text = "Guest"

        // Fetch username from Firebase Realtime Database
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val databaseReference = FirebaseDatabase.getInstance("https://cakemate-5fd64-default-rtdb.firebaseio.com/")
                .getReference("users").child(uid)

            databaseReference.get()
                .addOnSuccessListener { snapshot ->
                    val username = snapshot.child("username").value as? String
                    if (username != null) {
                        usernameTextView.text = username
                    } else {
                        Toast.makeText(this, "Username tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal memuat username", Toast.LENGTH_SHORT).show()
                }
        }

        // Open Drawer when userImage is clicked
        userImage.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Set up NavigationItemSelectedListener for the drawer menu
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_settings -> {
                    val intent = Intent(this, Pengaturan2::class.java)
                    startActivityForResult(intent, REQUEST_IMAGE_UPDATE)
                }
                R.id.nav_logout -> {
                    // Logout process
                    auth.signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Set up Bottom Navigation click listeners
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            if (bottomNavigationView.selectedItemId != menuItem.itemId) {
                when (menuItem.itemId) {
                    R.id.nav_menu -> {
                        val intent = Intent(this, MainActivityDashboard::class.java)
                        startActivity(intent)
                        finish()
                    }
                    R.id.nav_upload -> {
                        val intent = Intent(this, Upload1::class.java)
                        startActivity(intent)
                        finish()
                    }
                    R.id.nav_save_recipe -> {
                        val intent = Intent(this, SimpanResep::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            true
        }

        // Set default BottomNavigationView item
        bottomNavigationView.selectedItemId = R.id.nav_dashboard
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_UPDATE && resultCode == RESULT_OK) {
            val imageUriString = data?.getStringExtra("imageUri")
            if (imageUriString != null) {
                val imageUri = Uri.parse(imageUriString)
                userImage.setImageURI(imageUri)
                userIcon.setImageURI(imageUri)
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_UPDATE = 1
    }
}
