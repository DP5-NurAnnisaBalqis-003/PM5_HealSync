package com.lab5.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UbahPassword : AppCompatActivity() {
    private val sharedPrefFile = "com.lab5.myapplication.PREFERENCE_FILE_KEY"
    private val passwordKey = "REGISTERED_PASSWORD"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubah_password)

        val oldPasswordInput = findViewById<EditText>(R.id.et_old_password)
        val newPasswordInput = findViewById<EditText>(R.id.et_password)
        val confirmPasswordInput = findViewById<EditText>(R.id.et_confirm_password)
        val updateButton = findViewById<Button>(R.id.btn_update_password)

        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val registeredPassword = sharedPreferences.getString(passwordKey, "")

        updateButton.setOnClickListener {
            val oldPassword = oldPasswordInput.text.toString()
            val newPassword = newPasswordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            when {
                oldPassword != registeredPassword -> {
                    Toast.makeText(this, "Kata sandi lama salah", Toast.LENGTH_SHORT).show()
                }
                newPassword.isEmpty() || confirmPassword.isEmpty() -> {
                    Toast.makeText(this, "Kata sandi baru harus diisi", Toast.LENGTH_SHORT).show()
                }
                newPassword == oldPassword -> {
                    Toast.makeText(this, "Kata sandi baru tidak boleh sama dengan kata sandi lama", Toast.LENGTH_SHORT).show()
                }
                newPassword != confirmPassword -> {
                    Toast.makeText(this, "Kata sandi baru dan konfirmasi harus sesuai", Toast.LENGTH_SHORT).show()
                }
                !newPassword.matches(Regex("^[a-zA-Z0-9]{5,15}$")) -> {
                    Toast.makeText(this, "Kata sandi baru harus terdiri dari 5-15 angka atau huruf", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    sharedPreferences.edit().putString(passwordKey, newPassword).apply()
                    Toast.makeText(this, "Kata sandi berhasil diubah", Toast.LENGTH_SHORT).show()

                    // Redirect to LoginActivity
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
