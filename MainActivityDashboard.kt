package com.example.projectuas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import com.example.projectuas.databinding.ActivityMainDashboardBinding

class MainActivityDashboard : AppCompatActivity() {

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var binding: ActivityMainDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipes = listOf(
            Recipe("Jahe", "Kue jahe mengacu pada kategori luas makanan yang dipanggang.", R.drawable.jahe_image),
            Recipe("Onde-onde Ketawa", "Kue ketawa adalah camilan khas Kota Pematangsiantar.", R.drawable.ondeonde_image),
            Recipe("Cokelat Mete", "Kue kering rasa cokelat diberi topping mete di atas nya.", R.drawable.mete_image),
            Recipe("Almond Crispy", "Kue almond crispy adalah sejenis kue tipis yang renyah.", R.drawable.almond_image),
            Recipe("Sagu Pandan Keju", "Sagu pandan keju adalah kue kering yang terbuat dari tepung sagu.", R.drawable.sagu_image)
        )

        recipeAdapter = RecipeAdapter(recipes)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = recipeAdapter

        // Set search functionality
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                recipeAdapter.filter.filter(newText)
                return false
            }
        })

        recipeAdapter.onItemClickListener = { recipe ->
            val intent = when (recipe.title) {

                "Jahe" -> Intent(this, ResepJahe::class.java)
                "Onde-onde Ketawa" -> Intent(this, ResepOnde::class.java)
                "Cokelat Mete" -> Intent(this, ResepMete::class.java)
                "Almond Crispy" -> Intent(this, ResepAlmond::class.java)
                "Sagu Pandan Keju" -> Intent(this, ResepSagu::class.java)
                else -> null
            }
            intent?.let { startActivity(it) }
        }
    }
}
