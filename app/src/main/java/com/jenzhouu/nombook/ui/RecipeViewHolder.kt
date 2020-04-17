package com.jenzhouu.nombook.ui

import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.jenzhouu.nombook.R
import com.jenzhouu.nombook.databinding.TopRecipesListItemBinding
import com.jenzhouu.nombook.model.Meal
import com.squareup.picasso.Picasso

class RecipeViewHolder(private val binding: TopRecipesListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(meal: Meal) {
        with(binding) {
            recipeTitle.text = meal.name
            val imageLink = meal.image
            Picasso.get()
                .load(imageLink)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(recipeImage)

            setClickListener {
                val bundle = bundleOf("recipe" to meal)
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_navigation_home_to_navigation_recipe_details, bundle)
            }

            executePendingBindings()
        }
    }

}