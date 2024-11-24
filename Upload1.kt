package com.example.cakemate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class Upload1 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSelanjutnya = findViewById<Button>(R.id.btnSelanjutnya)
        btnSelanjutnya.setOnClickListener {
            val intent = Intent(this, Upload2::class.java)
            startActivity(intent)
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener { menuItem ->
            if (bottomNav.selectedItemId != menuItem.itemId) { // Hanya navigasi jika item berbeda
                when (menuItem.itemId) {
                    R.id.nav_dashboard -> {
                        startActivity(Intent(this, Dashboard::class.java))
                        finish() // Tutup aktivitas saat ini untuk mencegah tumpukan aktivitas
                    }
                    R.id.nav_menu -> {
                        startActivity(Intent(this, MainActivityDashboard::class.java))
                        finish()
                    }
                    R.id.nav_save_recipe -> {
                        startActivity(Intent(this, SimpanResep::class.java))
                        finish()
                    }
                }
            }
            true
        }

        bottomNav.selectedItemId = R.id.nav_upload
    }
}
