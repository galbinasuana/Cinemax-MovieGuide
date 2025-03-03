package com.example.cinemax.ui.discover

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cinemax.databinding.FragmentDiscoverBinding

class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private lateinit var discoverViewModel: DiscoverViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        discoverViewModel = ViewModelProvider(requireActivity()).get(DiscoverViewModel::class.java)
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupSpinners()

        binding.discoverButton.setOnClickListener {
            discoverViewModel.releaseYear = binding.spinnerReleaseYear.selectedItem as String?
            discoverViewModel.genre = binding.spinnerGenre.selectedItem as String?
            discoverViewModel.sortBy = binding.spinnerSortBy.selectedItem as String?

            if (discoverViewModel.releaseYear.isNullOrEmpty() || discoverViewModel.genre.isNullOrEmpty() || discoverViewModel.sortBy.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please select all options", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            discoverViewModel.discoverMovies()

            discoverViewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
                if (movies != null && movies.isNotEmpty()) {
                    val intent = Intent(activity, MoviesActivity::class.java).apply {
                        putParcelableArrayListExtra("movies_list", ArrayList(movies))
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "No movies found", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return root
    }

    private fun setupSpinners() {
        val releaseYearsAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            discoverViewModel.releaseYears
        )
        releaseYearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerReleaseYear.adapter = releaseYearsAdapter

        val genresAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            discoverViewModel.genreOptionsKeys
        )
        genresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGenre.adapter = genresAdapter

        val sortByAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            discoverViewModel.sortOptions
        )
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSortBy.adapter = sortByAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}