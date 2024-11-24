package com.lab5.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Dashboard : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var userImage: ImageView
    private lateinit var userIcon: ImageView
    private lateinit var bottomNavigationView: BottomNavigationView

    private val sharedPrefFile = "com.lab5.myapplication.PREFERENCE_FILE_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize DrawerLayout, NavigationView, and BottomNavigationView
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        userImage = findViewById(R.id.userImage)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Get the header view from NavigationView and set up user image and username
        val headerView = navigationView.getHeaderView(0)
        userIcon = headerView.findViewById(R.id.userIcon)

        // Retrieve username from SharedPreferences
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("REGISTERED_USERNAME", "Guest") // Default to "Guest"

        // Set username in the nav header
        val usernameTextView = headerView.findViewById<TextView>(R.id.username)
        usernameTextView.text = username

        // Open Drawer when userImage is clicked
        userImage.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Set up NavigationItemSelectedListener for the drawer menu
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_settings -> {
                    val intent = Intent(this, pengaturan2::class.java)
                    startActivityForResult(intent, REQUEST_IMAGE_UPDATE)
                }
                R.id.nav_logout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()  // Close this activity
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Set up Bottom Navigation click listeners
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            if (bottomNavigationView.selectedItemId != menuItem.itemId) { // Only navigate if different item is selected
                when (menuItem.itemId) {
                    R.id.nav_menu -> {
                        val intent = Intent(this, MainActivityDashboard::class.java)
                        startActivity(intent)
                        finish() // Finish current activity to avoid overlapping
                    }
                    R.id.nav_upload -> {
                        val intent = Intent(this, Upload1::class.java)
                        startActivity(intent)
                        finish() // Finish current activity to avoid overlapping
                    }
                    R.id.nav_save_recipe -> {
                        val intent = Intent(this, SimpanResep::class.java)
                        startActivity(intent)
                        finish() // Finish current activity to avoid overlapping
                    }
                }
            }
            true
        }

        // Set default BottomNavigationView item to highlight Dashboard
        bottomNavigationView.selectedItemId = R.id.nav_dashboard
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_UPDATE && resultCode == RESULT_OK) {
            val imageUriString = data?.getStringExtra("imageUri")
            if (imageUriString != null) {
                val imageUri = Uri.parse(imageUriString)
                userImage.setImageURI(imageUri)
                userIcon.setImageURI(imageUri) // Update userIcon in the header
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_UPDATE = 1
    }
}
