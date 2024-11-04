package com.example.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Sample data
        val cookies = listOf(
            Cookie("Kacang", R.drawable.kacang),
            Cookie("Onde-Onde Ketawa", R.drawable.onde),
            Cookie("Kue Jahe", R.drawable.jahe),
            Cookie("Sagu Pandan Keju", R.drawable.sagu)
        )

        val adapter = CookieAdapter(cookies)
        recyclerView.adapter = adapter
    }
}
