package com.example.androidfinal.core.ui.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.androidfinal.R
import com.example.androidfinal.core.ui.viewmodel.DetailViewModel
import com.example.androidfinal.core.ui.viewmodel.DetailViewModelFactory
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(args.article)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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

                    val saveButton: ImageView = view.findViewById(R.id.fab_save)
                    saveButton.setOnClickListener {
                        viewModel.saveCurrentArticle()
                        Toast.makeText(context, "Article saved", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}