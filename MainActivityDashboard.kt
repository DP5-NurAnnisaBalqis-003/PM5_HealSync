package com.lab5.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.content.Context
import android.widget.TextView

class Dashboard : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var userImage: ImageView
    private lateinit var userIcon: ImageView

    private val sharedPrefFile = "com.lab5.myapplication.PREFERENCE_FILE_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        userImage = findViewById(R.id.userImage)

        // Mendapatkan header dari NavigationView
        val headerView = navigationView.getHeaderView(0)
        userIcon = headerView.findViewById(R.id.userIcon)

        // Ambil username dari SharedPreferences
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("REGISTERED_USERNAME", "Guest") // Ambil username

        // Tampilkan username di nav_header
        val usernameTextView = headerView.findViewById<TextView>(R.id.username)
        usernameTextView.text = username // Menampilkan username yang disimpan

        // Membuka drawer ketika userImage ditekan
        userImage.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Mengatur navigasi item listener
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_settings -> {
                    val intent = Intent(this, pengaturan2::class.java)
                    startActivityForResult(intent, REQUEST_IMAGE_UPDATE)
                }
                R.id.nav_logout -> {
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_UPDATE && resultCode == RESULT_OK) {
            val imageUriString = data?.getStringExtra("imageUri")
            if (imageUriString != null) {
                val imageUri = Uri.parse(imageUriString)
                userImage.setImageURI(imageUri)
                userIcon.setImageURI(imageUri) // Update userIcon di header
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_UPDATE = 1
    }
}
