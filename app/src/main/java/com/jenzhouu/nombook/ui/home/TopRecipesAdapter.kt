package com.jenzhouu.nombook.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.jenzhouu.nombook.R
import com.jenzhouu.nombook.model.Meal
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.top_recipies_list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class TopRecipesAdapter : RecyclerView.Adapter<TopRecipesAdapter.TopRecipesViewHolder>(), Filterable {
    private var topRecipesList: List<Meal> = ArrayList()

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

        holder.cardView.setOnClickListener{
            val bundle = bundleOf("recipe" to topRecipesList[position])
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_home_to_navigation_recipe_details, bundle)
        }
    }

    override fun getItemCount(): Int {
        return topRecipesList.size
    }

    fun setTopRecipesList(recipesList: List<Meal>) {
        topRecipesList = recipesList
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object : Filter() {

        // runs on background thread
        override fun performFiltering(constraint: CharSequence): FilterResults? {
            val filteredList: MutableList<Meal> = mutableListOf()

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(topRecipesList)
            } else {
                topRecipesList.forEach {
                    if (it.name.toLowerCase(Locale.getDefault()).contains(
                            constraint.toString().toLowerCase(
                                Locale.getDefault()
                            )
                        )
                    ) {
                        filteredList.add(it)
                    }
                }
            }

            val filterResults = FilterResults()
            filterResults.values = filteredList

            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
//            topRecipesList.addAll(results.values as Collection<Meals>)
//            notifyDataSetChanged()
        }
    }


    class TopRecipesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.top_recipes_list_item
        val recipeTitle: TextView = itemView.top_recipes_title
        val recipeImage: ImageView = itemView.top_recipes_image
    }
}