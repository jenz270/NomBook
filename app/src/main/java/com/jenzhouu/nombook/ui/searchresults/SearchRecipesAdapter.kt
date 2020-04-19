package com.jenzhouu.nombook.ui.searchresults

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

class SearchRecipesAdapter(private val mealList: List<Meal>) : RecyclerView.Adapter<RecipeViewHolder>(), Filterable {
    var mealFilterList = ArrayList<Meal>()

    init {
        mealFilterList = ArrayList(mealList.map { it.copy() })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.recipes_list_item, parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return mealFilterList.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(mealFilterList[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                // TODO: Check conditions and make sure to make api call then populate filtered list
                val currentSearch = constraint.toString()
                mealFilterList = if (currentSearch.isEmpty()) {
                    ArrayList(mealList.map { it.copy() })
                } else {
                    val resultList = ArrayList<Meal>()
                    for (meal in mealList) {
                        if (meal.name.toLowerCase(Locale.ROOT).contains(currentSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(meal)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = mealFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mealFilterList = results?.values as ArrayList<Meal>
                notifyDataSetChanged()
            }

        }
    }
}