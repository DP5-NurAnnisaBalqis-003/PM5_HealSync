package com.lab5.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ubahnamapengguna : AppCompatActivity() {

    private val sharedPrefFile = "com.lab5.myapplication.PREFERENCE_FILE_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubahnamapengguna)

        val etOldUsername = findViewById<EditText>(R.id.et_username_bawah_gambar)
        val etNewUsername = findViewById<EditText>(R.id.et_username)
        val etPasswordConfirm = findViewById<EditText>(R.id.et_password)
        val btnUpdate = findViewById<Button>(R.id.btn_register)

        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val registeredUsername = sharedPreferences.getString("REGISTERED_EMAIL", "")
        val registeredPassword = sharedPreferences.getString("REGISTERED_PASSWORD", "")

        // Tampilkan nama pengguna lama
        etOldUsername.setText(registeredUsername)
        etOldUsername.textAlignment = EditText.TEXT_ALIGNMENT_CENTER

        btnUpdate.setOnClickListener {
            val newUsername = etNewUsername.text.toString()
            val passwordConfirm = etPasswordConfirm.text.toString()

            // Cek apakah nama pengguna baru sama dengan nama pengguna lama
            if (newUsername == registeredUsername) {
                Toast.makeText(this, "Nama pengguna baru tidak boleh sama dengan nama pengguna lama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi panjang dan karakter alfanumerik
            val alphanumericRegex = "^[a-zA-Z0-9]{5,15}$".toRegex()
            if (!alphanumericRegex.matches(newUsername)) {
                Toast.makeText(this, "Nama pengguna harus terdiri dari 5-15 huruf atau angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi kata sandi
            if (passwordConfirm == registeredPassword) {
                // Simpan nama pengguna baru
                sharedPreferences.edit().putString("REGISTERED_EMAIL", newUsername).apply()
                Toast.makeText(this, "Nama pengguna berhasil diubah", Toast.LENGTH_SHORT).show()

                // Kembali ke halaman login
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Kata sandi salah, tidak dapat mengubah nama pengguna", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
