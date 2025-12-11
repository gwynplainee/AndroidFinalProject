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

        setupCountrySpinner(view)
        setupBookmarksNavigation(view)
    }

    private fun setupCountrySpinner(view: View) {
        val countrySpinner: Spinner = view.findViewById(R.id.country_spinner)
        val app = requireActivity().application as NewsApplication

        val countryData = listOf(
            "United States" to "us",
            "Canada" to "ca",
            "Australia" to "au",
            "India" to "in",
            "Germany" to "de",
            "France" to "fr",
            "Japan" to "jp",
            "South Korea" to "kr",
            "Russia" to "ru",
            "China" to "cn",
            "Brazil" to "br",
            "Mexico" to "mx",
            "Italy" to "it",
            "Spain" to "es"
        )

        val countryNames = countryData.map { it.first }
        val countryCodes = countryData.map { it.second }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner.adapter = adapter

        val currentCountry = app.preferencesManager.getCountry()
        val currentIndex = countryCodes.indexOf(currentCountry)
        if (currentIndex != -1) {
            countrySpinner.setSelection(currentIndex)
        }

        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountryCode = countryCodes[position]
                val previousCountry = app.preferencesManager.getCountry()

                if (selectedCountryCode != previousCountry) {
                    app.preferencesManager.saveCountry(selectedCountryCode)

                    Toast.makeText(
                        requireContext(),
                        "News source changed to: ${countryNames[position]}",
                        Toast.LENGTH_SHORT
                    ).show()

                    homeViewModel.refreshNewsForCountry()
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
}