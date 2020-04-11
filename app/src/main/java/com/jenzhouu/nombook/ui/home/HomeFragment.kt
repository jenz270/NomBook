package com.jenzhouu.nombook.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SearchView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jenzhouu.nombook.R
import com.jenzhouu.nombook.ui.TopSpacingItemDecoration
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.jenzhouu.nombook.api.Result
import com.jenzhouu.nombook.api.Service

const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(Service())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val randomizeButton = view.findViewById<Button>(R.id.randomize_button)
        val recyclerView = view.findViewById<RecyclerView>(R.id.top_ten_recipes_rv)
        val topRecipesAdapter = TopRecipesAdapter()
        val searchView = view.findViewById<SearchView>(R.id.search_view)

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = topRecipesAdapter
            val spacingDecoration =
                TopSpacingItemDecoration(resources.getInteger(R.integer.list_item_padding))
            addItemDecoration(spacingDecoration)

        }
        viewModel.retrieveRandomRecipe()

        randomizeButton.setOnClickListener {
            getRandomRecipeResult(view)
        }

        viewModel.retrieveTopRecipes()
        getTopRecipes(topRecipesAdapter)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.retrieveSearchResults(query)
                    getSearchResults()
                }
                // TODO: pass in the data in the navigation route, remember to check for null data and return text on ui when null
                findNavController().navigate(R.id.action_navigation_home_to_navigation_search)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                topRecipesAdapter.filter.filter(newText)
                return false
            }
        })

        return view
    }

    /**
     * getSearchResults observes the search results recieved from the API
     */
    private fun getSearchResults() {
        viewModel.getSearchResults().observe(this, Observer { result ->
            when (result) {
                is Result.InProgress -> {
                    Log.d(TAG, "search results are loading...")
                }
                is Result.Success -> {
                    Log.d(TAG, "Successfully retrieved search results.")
                }
                is Result.Failure -> {
                    Log.d(TAG, "Results failed to be retrieved.")
                }
            }
        })
    }

    /**
     * getTopRecipes observes the top recipes data from the API, then sets the recycler view with the results
     *
     * @param adapter is used to set the result into the TopRecipesAdapter
     */
    private fun getTopRecipes(adapter: TopRecipesAdapter) {
        viewModel.getRecipes().observe(this, Observer { result ->
            when (result) {
                is Result.InProgress -> {
                    Log.d(TAG, "search results are loading...")
                }
                is Result.Success -> {
                    if (result.data != null) {
                        adapter.setTopRecipesList(result.data.mealsList)
                        adapter.notifyDataSetChanged()
                    }
                    Log.d(TAG, "Successfully retrieved top recipes.")
                }
                is Result.Failure -> {
                    Log.d(TAG, "Results failed to be retrieved.")
                }
            }
        })
    }

    /**
     * getRandomRecipeResult will send a random recipe to the recipe details page
     *
     * @param view View required to perform the fragment navigation
     */
    private fun getRandomRecipeResult(view: View) {
        viewModel.getRandomRecipe().observe(this, Observer { result ->
            when (result) {
                is Result.InProgress -> {
                    // TODO: Add in spinner for in progress
                    //spinner.visibility = VISIBLE
                    Log.d(TAG, "random recipe is loading...")
                }
                is Result.Success -> {
                    if (result.data != null) {
                        val bundle = bundleOf("randomMeal" to result.data.mealsList[0])
                        Navigation.findNavController(view)
                            .navigate(
                                R.id.action_navigation_home_to_navigation_recipe_details,
                                bundle
                            )
                    }
                }
                is Result.Failure -> {
                    // TODO: Lead to error fragment
                    Log.e(TAG, "random recipe ran into an error.")
                }
            }

        })
    }
}