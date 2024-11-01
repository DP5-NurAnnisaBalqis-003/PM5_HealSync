package com.example.mylogin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {
    private val correctUsername = "admin"
    private val correctPassword = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)  // Sesuaikan dengan layout XML

        val loginButton = findViewById<Button>(R.id.loginButton) // Sesuai dengan ID tombol di XML
        loginButton.setOnClickListener {
            // Ambil input dari EditText
            val username = findViewById<EditText>(R.id.namapengguna).text.toString() // ID sesuai XML
            val password = findViewById<EditText>(R.id.password).text.toString() // ID sesuai XML

            if (username == correctUsername && password == correctPassword) {
                // Jika username dan password benar
                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                // Lakukan sesuatu seperti navigasi ke halaman berikutnya
            } else {
                Toast.makeText(this, "Nama pengguna atau kata sandi salah", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
