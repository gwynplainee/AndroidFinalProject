package com.example.androidfinal.core.ui.fragments

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.NewsApplication
import com.example.androidfinal.R
import com.example.androidfinal.core.data.db.SavedArticleEntity
import com.example.androidfinal.core.ui.adapter.OnSavedArticleClickListener
import com.example.androidfinal.core.ui.adapter.SavedNewsAdapter
import com.example.androidfinal.core.ui.viewmodel.BookmarksViewModel
import com.example.androidfinal.core.ui.viewmodel.BookmarksViewModelFactory
import com.google.android.material.snackbar.Snackbar
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

        setupSwipeToDelete(recyclerView)

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
        val newsArticle = com.example.androidfinal.core.data.db.NewsArticleEntity(
            id = 0,
            source = article.source,
            author = article.author,
            title = article.title,
            description = article.description,
            url = article.url,
            imageUrl = article.imageUrl,
            publishedAt = article.publishedAt,
            content = article.content,
        )
        val action = BookmarksFragmentDirections.actionBookmarksFragmentToDetailFragment(newsArticle)
        findNavController().navigate(action)
    }

    private fun setupSwipeToDelete(recyclerView: RecyclerView){
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean{
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val adapter = recyclerView.adapter as SavedNewsAdapter
                if(position != RecyclerView.NO_POSITION){
                    val articleToDelete = adapter.getArticleAt(position)
                    viewModel.deleteArticle(articleToDelete)

                    Snackbar.make(recyclerView, "Article deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            viewModel.saveArticle(articleToDelete)
                        }.show()
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val background = ColorDrawable(Color.RED)

                if(dX<0){
                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom)
                    background.draw(c)
                }else{
                    background.setBounds(
                        itemView.left + dX.toInt(),
                        itemView.top,
                        itemView.left,
                        itemView.bottom)
                    background.draw(c)
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recyclerView)
        }
    }

}