package com.example.androidfinal.core.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.NewsApplication
import com.example.androidfinal.R
import com.example.androidfinal.core.data.model.NewsArticle
import com.example.androidfinal.core.ui.adapter.SearchAdapter
import com.example.androidfinal.core.ui.state.SearchUiState
import com.example.androidfinal.core.ui.viewmodel.SearchViewModel
import com.example.androidfinal.core.ui.viewmodel.SearchViewModelFactory
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory((requireActivity().application as NewsApplication).repository)
    }

    private lateinit var searchInput: EditText
    private lateinit var btnBack: ImageButton
    private lateinit var btnClear: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingView: ProgressBar
    private lateinit var initialText: TextView
    private lateinit var errorText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search ,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchInput = view.findViewById(R.id.search_input)
        btnBack = view.findViewById(R.id.btn_back)
        btnClear = view.findViewById(R.id.btn_clear)
        recyclerView = view.findViewById(R.id.search_recycler_view)
        loadingView = view.findViewById(R.id.search_loading)
        initialText = view.findViewById(R.id.search_initial_text)
        errorText = view.findViewById(R.id.search_error_text)

        setupViews()
        observeState()
    }
    private fun setupViews() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        btnBack.setOnClickListener { findNavController().navigateUp() }
        btnClear.setOnClickListener { searchInput.text.clear()
            viewModel.clearSearch()
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClear.isVisible = !s.isNullOrEmpty()
                viewModel.searchNews(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val q = searchInput.text.toString()
                viewModel.searchNews(q)
                true
            } else false
        }

        searchInput.requestFocus()
    }
    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleState(state)
                }
            }
        }
    }

    private fun handleState(state: SearchUiState) {
        recyclerView.isVisible = false
        loadingView.isVisible = false
        initialText.isVisible = false
        errorText.isVisible = false

        when (state) {
            is SearchUiState.Initial -> {
                initialText.isVisible = true
            }
            is SearchUiState.Loading -> {
                loadingView.isVisible = true
            }
            is SearchUiState.Success -> {
                recyclerView.isVisible = true
                displayResults(state.articles)
            }
            is SearchUiState.Error -> {
                errorText.isVisible = true
                errorText.text = state.message
            }
        }
    }
    private fun displayResults(articles: List<NewsArticle>) {
        val adapter = SearchAdapter(articles) { article ->
        }
        recyclerView.adapter = adapter
    }
}