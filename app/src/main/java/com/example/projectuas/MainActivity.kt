package com.example.projectuas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.example.projectuas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var binding: ActivityMainBinding // Using ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize sample recipe list
        val recipes = listOf(
            Recipe("Jahe", "Kue jahe yang manis, dibuat dengan jahe, tepung, gula, dan madu.", R.drawable.jahe_image),
            Recipe("Onde-onde Ketawa", "Kue khas Sumatra berbentuk bulat dengan biji wijen.", R.drawable.ondeonde_image),
            Recipe("Cokelat Mete", "Kue kering cokelat dengan mete sebagai topping.", R.drawable.cokelat_image),
            Recipe("Almond Crispy", "Kue tipis dan renyah dengan irisan almond.", R.drawable.almond_image),
            Recipe("Sagu Pandan Keju", "Kue sagu pandan yang dipadu dengan keju.", R.drawable.sagu_image),
            Recipe("Kacang", "Kue kering kacang dengan rasa manis-gurih.", R.drawable.kacang_image)
        )

        // Set up RecyclerView and Adapter
        recipeAdapter = RecipeAdapter(recipes)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = recipeAdapter

        // Set up SearchView to filter recipes
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recipeAdapter.filter.filter(newText)
                return false
            }
        })
    }
}
