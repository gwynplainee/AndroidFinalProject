package com.example.androidfinal.core.ui.fragments

import android.graphics.Color
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.androidfinal.NewsApplication
import com.example.androidfinal.R
import com.example.androidfinal.core.ui.viewmodel.DetailViewModel
import com.example.androidfinal.core.ui.viewmodel.DetailViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels {
        val app = requireActivity().application as NewsApplication
        DetailViewModelFactory(app.repository, args.article)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleView: TextView = view.findViewById(R.id.detail_title)
        val metadataView: TextView = view.findViewById(R.id.detail_metadata)
        val bodyView: TextView = view.findViewById(R.id.detail_body)
        val imageView: ImageView = view.findViewById(R.id.detail_image)
        val saveButton: ImageView = view.findViewById(R.id.fab_save)

        saveButton.setOnClickListener {
            viewModel.toggleBookmark()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        titleView.text = state.article.title
                        metadataView.text = state.formattedMetadata

                        bodyView.text = HtmlCompat.fromHtml(state.formattedBody, HtmlCompat.FROM_HTML_MODE_LEGACY)

                        if (!state.article.imageUrl.isNullOrEmpty()) {
                            Glide.with(this@DetailFragment)
                                .load(state.article.imageUrl)
                                .placeholder(R.drawable.news_placeholder)
                                .error(R.drawable.error)
                                .into(imageView)
                        } else {
                            imageView.setImageResource(R.drawable.news_placeholder)
                        }
                    }
                }

                launch {
                    viewModel.isArticleSaved.collect { isSaved ->
                        if (isSaved) {
                            saveButton.setImageResource(R.drawable.ic_bookmark_added)
                        } else {
                            saveButton.setImageResource(R.drawable.ic_bookmark)
                        }
                    }
                }

                launch {
                    viewModel.event.collect { message ->
                        showCustomSnackbar(view, message)
                    }
                }
            }
        }
    }

    private fun showCustomSnackbar(view: View, message: String) {
        val snackbar = Snackbar.make(view, message,
            Snackbar.LENGTH_LONG)

        val snackbarView = snackbar.view

        val context = view.context
        if (message.contains("removed")) {
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red_500))
            snackbar.setTextColor(Color.WHITE)

            snackbar.setAction("Undo") {
                viewModel.toggleBookmark()
            }
            snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.white))

        } else {
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.green_500))
            snackbar.setTextColor(Color.WHITE)

        }

        snackbar.show()
    }
}