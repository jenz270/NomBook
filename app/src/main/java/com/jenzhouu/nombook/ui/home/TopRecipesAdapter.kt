package com.jenzhouu.nombook.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jenzhouu.nombook.R
import com.jenzhouu.nombook.model.Meal
import com.jenzhouu.nombook.ui.RecipeViewHolder
import java.util.*
import kotlin.collections.ArrayList

class TopRecipesAdapter : RecyclerView.Adapter<RecipeViewHolder>(), Filterable {
    private var topRecipesList: List<Meal> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.top_recipes_list_item, parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
         holder.bind(topRecipesList[position])
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
}