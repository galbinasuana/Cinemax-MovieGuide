package com.example.cinemax.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.cinemax.R
import com.example.cinemax.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.cardNews.setOnClickListener {
            findNavController().navigate(R.id.ToNewsFragment, null, NavOptions.Builder().setPopUpTo(R.id.navigation_home, true).build())
        }

        binding.cardDiscoverMovies.setOnClickListener {
            findNavController().navigate(R.id.ToDiscoverMoviesFragment, null, NavOptions.Builder().setPopUpTo(R.id.navigation_home, true).build())
        }

        binding.cardMaps.setOnClickListener {
            findNavController().navigate(R.id.ToCinemaFragment, null, NavOptions.Builder().setPopUpTo(R.id.navigation_home, true).build())
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}