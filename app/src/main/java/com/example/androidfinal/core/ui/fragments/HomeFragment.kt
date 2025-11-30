package com.example.androidfinal.core.ui.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.R
import com.example.androidfinal.core.ui.adapter.CategoryAdapter
import com.example.androidfinal.core.ui.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun setupCategoryRecycler() {
        val categoryList = listOf("General", "Business", "Entertainment", "Health", "Science", "Sports", "Technology")

        val navRecyclerView: RecyclerView = view?.findViewById(R.id.nav_recycler_view) ?: return

        navRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Initialize Adapter
        val categoryAdapter = CategoryAdapter(categoryList) { selectedCategory ->

//            viewModel.fetchNews(selectedCategory.lowercase())
        }

        navRecyclerView.adapter = categoryAdapter
    }
}