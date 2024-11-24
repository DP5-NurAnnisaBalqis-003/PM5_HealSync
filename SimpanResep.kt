package com.example.cakemate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cakemate.databinding.SimpanResepBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class SimpanResep : AppCompatActivity() {

    private lateinit var adapterSimpanResep: AdapterSimpanResep
    private lateinit var binding: SimpanResepBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SimpanResepBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipes = mutableListOf(
            ListResep("Kue Jahe", R.drawable.kuejahe),
            ListResep("Coklat Mete", R.drawable.coklatmete),
            ListResep("Almond Crispy", R.drawable.almondnestum),
        )

        val isFromDashboard = intent.getBooleanExtra("IS_FROM_DASHBOARD", false)
        if (isFromDashboard) {
            Toast.makeText(this, "Arahkan dari Dashboard", Toast.LENGTH_SHORT).show()
        }

        val newRecipeTitle = intent.getStringExtra("RESEP_TITLE")
        val newRecipeImage = intent.getIntExtra("RESEP_IMAGE", -1)

        if (newRecipeTitle != null && newRecipeImage != -1) {
            val newRecipe = ListResep(newRecipeTitle, newRecipeImage)
            recipes.add(newRecipe)
        }

        adapterSimpanResep = AdapterSimpanResep(recipes)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapterSimpanResep

        val itemTouchHelper = ItemTouchHelper(HapusSimpanResep(adapterSimpanResep))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterSimpanResep.filter.filter(newText)
                return false
            }
        })

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            if (bottomNavigation.selectedItemId != item.itemId) {
                when (item.itemId) {
                    R.id.nav_dashboard -> {
                        startActivity(Intent(this, Dashboard::class.java))
                        finish()
                    }
                    R.id.nav_menu -> {
                        startActivity(Intent(this, MainActivityDashboard::class.java))
                        finish()
                    }
                    R.id.nav_upload -> {
                        startActivity(Intent(this, Upload1::class.java))
                        finish()
                    }
                }
            }
            true
        }

        bottomNavigation.selectedItemId = R.id.nav_save_recipe
    }
}
