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
import android.content.Intent
import android.net.Uri


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val randomizeButton = view.findViewById<Button>(R.id.randomize_button)
        val recyclerView = view.findViewById<RecyclerView>(R.id.top_ten_recipes_rv)
        val topRecipesAdapter = TopRecipesAdapter()
        val searchView = view.findViewById<SearchView>(R.id.search_view)

        randomizeButton.setOnClickListener {
            val uri = Uri.parse("http://www.africau.edu/images/default/sample.pdf") // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context?.startActivity(intent)
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                homeViewModel.loadSearchMovies(query)
//                addSearchDataSet()
                findNavController().navigate(R.id.action_navigation_home_to_navigation_search)
                return true
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
        homeViewModel.retrieveRecipes()
        homeViewModel.getRecipes().observe(this, Observer {
            topRecipesAdapter.setTopRecipesList(it)
            topRecipesAdapter.notifyDataSetChanged()
        })

        return view
    }
}