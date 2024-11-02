package com.lab5.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Upload2 : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_upload2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ingredientsText = findViewById<EditText>(R.id.bahandancarmemasak)
        val addButton = findViewById<Button>(R.id.btn_tambah)

        addButton.setOnClickListener {
            val ingredients = ingredientsText.text.toString()
            if (ingredients.isNotEmpty()) {
                // Handle saving ingredients to database or list
                Toast.makeText(this, "Menu berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                ingredientsText.text.clear()
            } else {
                Toast.makeText(this, "Harap masukkan bahan dan cara membuat", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
