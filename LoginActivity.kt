package com.example.cakemate

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private val sharedPrefFile = "com.example.cakemate.PREFERENCE_FILE_KEY"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mylogin)

        val loginButton = findViewById<Button>(R.id.btn_login)
        val usernameInput = findViewById<EditText>(R.id.namapengguna)
        val passwordInput = findViewById<EditText>(R.id.password)
        val registerTextView = findViewById<TextView>(R.id.registerTextView) // Tambahan untuk navigasi ke registrasi

        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("REGISTERED_USERNAME", null)
        val savedPassword = sharedPreferences.getString("REGISTERED_PASSWORD", null)

        loginButton.setOnClickListener {
            val enteredUsername = usernameInput.text.toString()
            val enteredPassword = passwordInput.text.toString()

            if (enteredUsername == savedUsername && enteredPassword == savedPassword) {
                Toast.makeText(this, "Masuk berhasil", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, Dashboard::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Nama pengguna atau kata sandi salah", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigasi ke MainActivity (Registrasi)
        registerTextView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
