package com.jenzhouu.nombook.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jenzhouu.nombook.R
import com.jenzhouu.nombook.api.Result
import com.jenzhouu.nombook.api.Service
import com.jenzhouu.nombook.databinding.FragmentHomeBinding
import com.jenzhouu.nombook.ui.TopSpacingItemDecoration
import com.jenzhouu.nombook.ui.ViewModelFactory

const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels {
        val activity = requireNotNull(this.activity)
        ViewModelFactory(activity.application, Service())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater, R.layout.fragment_home, container, false
        )
        val topRecipesAdapter = TopRecipesAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = topRecipesAdapter
            val spacingDecoration =
                TopSpacingItemDecoration(resources.getInteger(R.integer.list_item_padding))
            addItemDecoration(spacingDecoration)

        }
        viewModel.retrieveRandomRecipe()

        binding.randomizeButton.setOnClickListener {
            getRandomRecipeResult(it)
        }

        viewModel.retrieveTopRecipes()
        getTopRecipes(topRecipesAdapter, binding)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    getSearchResults(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return binding.root
    }

    /**
     * getSearchResults observes the search results recieved from the API
     */
    private fun getSearchResults(query: String) {
        viewModel.retrieveSearchedRecipes(query)
        viewModel.searchRecipes.observe(this, Observer { result ->
            when (result) {
                is Result.InProgress -> {
                    Log.d(TAG, "search results are loading...")
                }
                is Result.Success -> {
                    Log.d(TAG, "Successfully retrieved search results.")
                    // TODO: pass in the data in the navigation route, remember to check for null data and return text on ui when null
                    val bundle = bundleOf("mealList" to result.data)
                    findNavController().navigate(R.id.action_navigation_home_to_navigation_search, bundle)
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
    private fun getTopRecipes(adapter: TopRecipesAdapter, binding: FragmentHomeBinding) {
        viewModel.topRecipes.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.InProgress -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (result.data != null) {
                        adapter.setTopRecipesList(result.data.mealsList)
                        adapter.notifyDataSetChanged()
                    }
                    Log.d(TAG, "Successfully retrieved top recipes.")
                }
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
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
        viewModel.randomRecipe.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.InProgress -> {
                    // TODO: Add in spinner for in progress
                    //spinner.visibility = VISIBLE
                    Log.d(TAG, "random recipe is loading...")
                }
                is Result.Success -> {
                    if (result.data != null) {
                        val bundle = bundleOf("recipe" to result.data.mealsList[0])
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