package com.jenzhouu.nombook.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jenzhouu.nombook.R
import com.jenzhouu.nombook.ui.TopSpacingItemDecoration
import java.lang.Integer.getInteger

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.top_ten_recipes_rv)
        val topRecipesAdapter = TopRecipesAdapter()

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = topRecipesAdapter
            val spacingDecoration = TopSpacingItemDecoration(resources.getInteger(R.integer.recipes_list_item_padding))
            addItemDecoration(spacingDecoration)

        }
        homeViewModel.retrieveRecipes()
        homeViewModel.getRecipes().observe(this, Observer {
            topRecipesAdapter.setTopRecipesList(it)
            topRecipesAdapter.notifyDataSetChanged()
        })
        setHasOptionsMenu(true)
        return root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_favorite).setVisible(false)
        super.onPrepareOptionsMenu(menu)
    }
}