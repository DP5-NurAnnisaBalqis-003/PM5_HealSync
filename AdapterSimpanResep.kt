package com.example.simpanresep

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class AdapterSimpanResep(private var recipeList: MutableList<ListResep>) :
    RecyclerView.Adapter<AdapterSimpanResep.RecipeViewHolder>(), Filterable {

    private var filteredRecipeList = recipeList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemsimpanresep, parent, false)
        return RecipeViewHolder(itemView)
    }

    fun removeItem(position: Int) {
        recipeList.removeAt(position)
        filteredRecipeList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, recipeList.size)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = filteredRecipeList[position]
        holder.recipeTitle.text = recipe.title
        holder.recipeDescription.text = recipe.description
        holder.recipeImage.setImageResource(recipe.imageResId)
    }

    override fun getItemCount(): Int = filteredRecipeList.size

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeTitle: TextView = itemView.findViewById(R.id.recipeTitle)
        val recipeDescription: TextView = itemView.findViewById(R.id.recipeDescription)
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase(Locale.getDefault())
                filteredRecipeList = if (query.isNullOrEmpty()) {
                    recipeList.toMutableList()
                } else {
                    recipeList.filter {
                        it.title.lowercase(Locale.getDefault()).contains(query)
                    }.toMutableList()
                }
                return FilterResults().apply { values = filteredRecipeList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredRecipeList = results?.values as MutableList<ListResep>
                notifyDataSetChanged()
            }
        }
    }
}
