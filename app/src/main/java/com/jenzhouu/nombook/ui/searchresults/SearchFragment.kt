package com.jenzhouu.nombook.ui.searchresults

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jenzhouu.nombook.R
import com.jenzhouu.nombook.ui.TopSpacingItemDecoration
import com.jenzhouu.nombook.ui.home.TopRecipesAdapter

class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by lazy { SearchViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.search_recipes_rv)
        val topRecipesAdapter = TopRecipesAdapter()
        val searchView = view.findViewById<SearchView>(R.id.search_view)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                homeViewModel.loadSearchMovies(query)
//                addSearchDataSet()
                  Toast.makeText(context,"Clicked on search on search fragment", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                topRecipesAdapter.filter.filter(newText)
                return false
            }
        })

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = topRecipesAdapter
            val spacingDecoration = TopSpacingItemDecoration(resources.getInteger(R.integer.list_item_padding))
            addItemDecoration(spacingDecoration)

        }

//        searchViewModel.retrieveRecipes()
//        searchViewModel.getMealsList().observe(this, Observer {
//            topRecipesAdapter.setTopRecipesList(it)
//            topRecipesAdapter.notifyDataSetChanged()
//        })


        return view
    }
}