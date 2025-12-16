package com.example.androidfinal.core.ui.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.NewsApplication
import com.example.androidfinal.R
import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.db.SavedArticleEntity
import com.example.androidfinal.core.ui.adapter.CategoryAdapter
import com.example.androidfinal.core.ui.adapter.NewsAdapter
import com.example.androidfinal.core.ui.adapter.OnArticleClickListener

import com.example.androidfinal.core.ui.state.HomeUiState
import com.example.androidfinal.core.ui.viewmodel.HomeViewModel
import com.example.androidfinal.core.ui.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), OnArticleClickListener {


    private val viewModel: HomeViewModel by viewModels {
        val app = requireActivity().application as NewsApplication
        HomeViewModelFactory(app.repository, app.preferencesManager)
    }

    private lateinit var categoryAdapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategoryRecycler()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleUiState(state)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.scrollToTopEvent.collect {
                    view.findViewById<RecyclerView>(R.id.news_recycler_view)?.scrollToPosition(0)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        val app = requireActivity().application as NewsApplication
        val isSourceActive = !app.preferencesManager.getSource().isNullOrEmpty()

        if (::categoryAdapter.isInitialized) {
            categoryAdapter.updateSourceState(isSourceActive)
        }
        if (app.preferencesManager.hasPreferencesChanged()) {
            viewModel.refreshNewsFromNetworkIfNeeded()
            app.preferencesManager.setPreferencesChanged(false)
        }
    }

    private fun setupCategoryRecycler() {
        val categoryList = listOf("General", "Business", "Entertainment", "Health", "Science", "Sports", "Technology")

        val navRecyclerView: RecyclerView = view?.findViewById(R.id.nav_recycler_view) ?: return

        navRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val app = requireActivity().application as NewsApplication


        val currentSavedCategory = app.preferencesManager.getCategory()
        val currentSource = app.preferencesManager.getSource()

        val isSourceActive = !currentSource.isNullOrEmpty()

        categoryAdapter = CategoryAdapter(categoryList, currentSavedCategory, isSourceActive) { selectedCategory ->
            viewModel.refreshNews(selectedCategory)
        }

        navRecyclerView.adapter = categoryAdapter
    }



    override fun onArticleClicked(article: NewsArticleEntity) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(article)
        findNavController().navigate(action)
    }

    private fun setupRecyclerView(articles: List<NewsArticleEntity>) {
        val recyclerView: RecyclerView? = view?.findViewById(R.id.news_recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        val adapter = NewsAdapter(articles, this)
        recyclerView?.adapter = adapter
    }

    private fun handleUiState(state: HomeUiState) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.news_recycler_view)
        val loadingIndicator = view?.findViewById<ProgressBar>(R.id.loading_indicator)
        val errorText = view?.findViewById<TextView>(R.id.error_message_text)

        recyclerView?.visibility = View.GONE
        loadingIndicator?.visibility = View.GONE
        errorText?.visibility = View.GONE

        when (state) {
            is HomeUiState.Loading -> {
                loadingIndicator?.visibility = View.VISIBLE
            }
            is HomeUiState.Error -> {
                errorText?.visibility = View.VISIBLE
                errorText?.text = state.message
            }
            is HomeUiState.Success -> {
                recyclerView?.visibility = View.VISIBLE
                setupRecyclerView(state.articles)
            }
        }
    }

    fun requestScrollToTop(){
        viewModel.requestScrollToTop()
    }

}