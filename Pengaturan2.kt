package com.example.pengaturancm

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Pengaturan2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan2)

        val btnChangePassword: Button = findViewById(R.id.btnChangePassword)
        val btnChangeUsername: Button = findViewById(R.id.btnChangeUsername)
        val btnChangeProfilePicture: Button = findViewById(R.id.btnChangeProfilePicture)
        val btnLogout: Button = findViewById(R.id.btnLogout)

        btnChangePassword.setOnClickListener {
        }

        btnChangeUsername.setOnClickListener {
        }

        btnChangeProfilePicture.setOnClickListener {
        }

        btnLogout.setOnClickListener {
        }
    }
}
