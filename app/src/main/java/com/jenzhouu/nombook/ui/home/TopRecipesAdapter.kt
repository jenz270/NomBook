package com.jenzhouu.nombook.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jenzhouu.nombook.R
import com.jenzhouu.nombook.model.Recipe
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.top_recipies_list_item.view.*

class TopRecipesAdapter() : RecyclerView.Adapter<TopRecipesAdapter.TopRecipesViewHolder>() {
    private var topRecipesList: List<Recipe> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRecipesViewHolder {
        return TopRecipesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.top_recipies_list_item, parent,false)
        )
    }

    override fun onBindViewHolder(holder: TopRecipesViewHolder, position: Int) {
         holder.recipeTitle.text = topRecipesList[position].name
         val imageLink = topRecipesList[position].image
         Picasso.get()
            .load(imageLink)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.recipeImage)
    }

    override fun getItemCount(): Int {
        return topRecipesList.size
    }

    fun setTopRecipesList(recipesList: List<Recipe>) {
        topRecipesList = recipesList
    }

    class TopRecipesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeTitle: TextView = itemView.top_recipes_title
        val recipeImage: ImageView = itemView.top_recipes_image
    }
}