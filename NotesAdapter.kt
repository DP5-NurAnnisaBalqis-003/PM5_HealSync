package com.example.catatan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class NotesAdapter(private var notesList: List<Notes>) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>(), Filterable {

    var filteredNotesList = notesList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)
        return NotesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = filteredNotesList[position]
        holder.noteTitle.text = note.title
        holder.noteDescription.text = note.description
        holder.noteImage.setImageResource(note.imageResId)
    }

    override fun getItemCount(): Int {
        return filteredNotesList.size
    }

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView = itemView.findViewById(R.id.recipeTitle)
        val noteDescription: TextView = itemView.findViewById(R.id.recipeDescription)
        val noteImage: ImageView = itemView.findViewById(R.id.recipeImage)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase(Locale.getDefault())

                filteredNotesList = if (query.isNullOrEmpty()) {
                    notesList
                } else {
                    notesList.filter {
                        it.title.lowercase(Locale.getDefault()).contains(query)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredNotesList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredNotesList = results?.values as List<Notes>
                notifyDataSetChanged()
            }
        }
    }
}
