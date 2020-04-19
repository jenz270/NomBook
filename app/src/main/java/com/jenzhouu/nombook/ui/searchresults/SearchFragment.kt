package com.jenzhouu.nombook.ui.searchresults

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jenzhouu.nombook.R
import com.jenzhouu.nombook.api.Result
import com.jenzhouu.nombook.api.Service
import com.jenzhouu.nombook.databinding.FragmentHomeBinding
import com.jenzhouu.nombook.model.Meal
import com.jenzhouu.nombook.ui.TopSpacingItemDecoration
import com.jenzhouu.nombook.ui.ViewModelFactory
import com.jenzhouu.nombook.ui.home.TAG

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels {
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

        val bundle = arguments
        var mealList: List<Meal>? = null

        if (bundle != null) {
            mealList = bundle.getParcelableArrayList("mealList")
        }

        val searchRecipesAdapter = SearchRecipesAdapter(mealList ?: listOf())

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchRecipesAdapter
            val spacingDecoration =
                TopSpacingItemDecoration(resources.getInteger(R.integer.list_item_padding))
            addItemDecoration(spacingDecoration)

        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    getSearchResults(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRecipesAdapter.filter.filter(newText)
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
                    Log.d(TAG, "Search results are loading...")
                }
                is Result.Success -> {
                    Log.d(TAG, "Successfully retrieved search results.")
                }
                is Result.Failure -> {
                    Log.d(TAG, "Search results failed to be retrieved.")
                }
            }
        })
    }
}