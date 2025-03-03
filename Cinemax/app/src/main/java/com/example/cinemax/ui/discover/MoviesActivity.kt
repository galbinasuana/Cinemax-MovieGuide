package com.example.cinemax.ui.discover

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinemax.data.Movie
import com.example.cinemax.databinding.ActivityMoviesBinding

class MoviesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoviesBinding
    private lateinit var discoverViewModel: DiscoverViewModel
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        discoverViewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)

        setupRecyclerView()

        val moviesList: ArrayList<Movie>? = intent.getParcelableArrayListExtra("movies_list")
        moviesList?.let {
            moviesAdapter.updateMovies(it)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(this)
        moviesAdapter = MoviesAdapter(emptyList())
        binding.recyclerViewMovies.adapter = moviesAdapter
    }
}