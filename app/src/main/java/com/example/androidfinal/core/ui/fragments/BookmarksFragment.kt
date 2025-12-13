package com.example.androidfinal.core.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.db.SavedArticleEntity
import com.example.androidfinal.core.ui.adapter.OnArticleClickListener
import com.example.androidfinal.core.ui.adapter.OnSavedArticleClickListener
import com.example.androidfinal.core.ui.adapter.SavedNewsAdapter
import com.example.androidfinal.core.ui.viewmodel.BookmarksViewModel
import com.example.androidfinal.core.ui.viewmodel.BookmarksViewModelFactory
import kotlinx.coroutines.launch

class BookmarksFragment : Fragment(), OnSavedArticleClickListener {

    private val viewModel: BookmarksViewModel by viewModels {
        BookmarksViewModelFactory(
            (requireActivity().application as NewsApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.bookmarks_recycler_view)
        val emptyText: TextView = view.findViewById(R.id.empty_bookmarks_text)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bookmarkedArticles.collect { articles ->
                    if (articles.isEmpty()) {
                        recyclerView.visibility = View.GONE
                        emptyText.visibility = View.VISIBLE
                    } else {
                        recyclerView.visibility = View.VISIBLE
                        emptyText.visibility = View.GONE
                        recyclerView.adapter = SavedNewsAdapter(articles, this@BookmarksFragment)
                    }
                }
            }
        }
    }

    override fun onArticleClicked(article: SavedArticleEntity) {
        val action = BookmarksFragmentDirections.actionBookmarksFragmentToDetailFragment(article)
        findNavController().navigate(action)
    }
}