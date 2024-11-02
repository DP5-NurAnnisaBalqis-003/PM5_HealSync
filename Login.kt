package com.lab5.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {
    
    private val sharedPrefFile = "com.lab5.myapplication.PREFERENCE_FILE_KEY"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mylogin)

        val loginButton = findViewById<Button>(R.id.btn_login)
        val usernameInput = findViewById<EditText>(R.id.namapengguna)
        val passwordInput = findViewById<EditText>(R.id.password)

        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("REGISTERED_EMAIL", null)
        val savedPassword = sharedPreferences.getString("REGISTERED_PASSWORD", null)

        loginButton.setOnClickListener {
            val enteredUsername = usernameInput.text.toString()
            val enteredPassword = passwordInput.text.toString()

            if (enteredUsername == savedUsername && enteredPassword == savedPassword) {
                Toast.makeText(this, "Masuk berhasil", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, Upload1::class.java)
                startActivity(intent)
                finish() 
            } else {
                Toast.makeText(this, "Nama pengguna atau kata sandi salah", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
