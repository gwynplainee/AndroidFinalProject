package com.example.androidfinal.core.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.androidfinal.NewsApplication
import com.example.androidfinal.R
import com.example.androidfinal.core.ui.viewmodel.HomeViewModel
import com.example.androidfinal.core.ui.viewmodel.HomeViewModelFactory

class MoreFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as NewsApplication
        homeViewModel = ViewModelProvider(
            requireActivity(),
            HomeViewModelFactory(app.repository, app.preferencesManager)
        )[HomeViewModel::class.java]

        setupSourceSpinner(view)
        setupBookmarksNavigation(view)
        setupSearchNavigation(view)
    }

    private fun setupSourceSpinner(view: View) {
        val sourceSpinner: Spinner = view.findViewById(R.id.country_spinner)
        val app = requireActivity().application as NewsApplication

        val sourceData = listOf(
            "All sources" to null,
            "BBC News" to "bbc-news",
            "ABC News" to "abc-news",
            "ESPN" to "espn",
            "Bloomberg" to "bloomberg",
            "Fox News" to "fox-news",
            "IGN" to "ign",
            "TechCrunch" to "techcrunch",
            "The Verge" to "the-verge"
        )

        val sourceNames = sourceData.map { it.first }
        val sourceCodes = sourceData.map { it.second }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sourceNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourceSpinner.adapter = adapter

        val currentSource = app.preferencesManager.getSource()
        val currentIndex = sourceCodes.indexOf(currentSource)
        if (currentIndex != -1) {
            sourceSpinner.setSelection(currentIndex)
        }

        sourceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSourceCode = sourceCodes[position]
                val previousSource = app.preferencesManager.getSource()

                if (selectedSourceCode != previousSource) {
                    app.preferencesManager.saveSource(selectedSourceCode)
                    app.preferencesManager.setPreferencesChanged(true)

                    Toast.makeText(
                        requireContext(),
                        "News source changed to: ${sourceNames[position]}",
                        Toast.LENGTH_SHORT
                    ).show()

                    homeViewModel.refreshNewsForSource()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    private fun setupBookmarksNavigation(view: View) {
        val bookmarksCard: CardView = view.findViewById(R.id.bookmarks_card)
        bookmarksCard.setOnClickListener {
            findNavController().navigate(R.id.action_moreFragment_to_bookmarksFragment)
        }
    }
    private fun setupSearchNavigation(view: View) {
        val searchCard: CardView = view.findViewById(R.id.search_card)
        searchCard.setOnClickListener {
            findNavController().navigate(R.id.action_moreFragment_to_searchFragment)
        }
    }
}