package com.example.cinemax.ui.discover

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemax.service.DiscoverMoviesAPI
import com.example.cinemax.data.Movie
import kotlinx.coroutines.launch

class DiscoverViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    val releaseYears: List<String> = generateYearsList()
    val genreOptionsKeys: List<String> = GenreDictionary.keys.toList()
    val sortOptions: List<String> = SortByDictionary.keys.toList()

    var releaseYear: String? = null
    var genre: String? = null
    var sortBy: String? = null

    private val genreOptions = GenreDictionary

    private val discoverMoviesAPI = DiscoverMoviesAPI("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5NDgzMmIyNmM5NTZkNDgyM2Q3NTY5OTBjMDcyNjFlMSIsInN1YiI6IjY1ZGQ4NmRiYzQzM2VhMDE2MzNjMmQwNCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.6BVWyOI4744LaPF53-IlpW_2wx_t6BYRqah35Aujaek")

    fun discoverMovies() {
        val year = releaseYear ?: ""
        val selectedGenre = genre ?: ""
        val sortOption = sortBy ?: ""

        viewModelScope.launch {
            val movieList = discoverMoviesAPI.getMovies(
                genreOptions,
                year,
                SortByDictionary[sortOption] ?: "",
                genreOptions[selectedGenre]?.toString() ?: ""
            )

            if(movieList != null) {
                _movies.postValue(movieList!!)
            } else {
                _movies.postValue(emptyList())
            }
        }
    }

    companion object {
        val GenreDictionary: Map<String, Int> = mapOf(
            "Action" to 28,
            "Adventure" to 12,
            "Animation" to 16,
            "Comedy" to 35,
            "Crime" to 80,
            "Documentary" to 99,
            "Drama" to 18,
            "Family" to 10751,
            "Fantasy" to 14,
            "History" to 36,
            "Horror" to 27,
            "Music" to 10402,
            "Mystery" to 9648,
            "Romance" to 10749,
            "Science Fiction" to 878,
            "TV Movie" to 10770,
            "Thriller" to 53,
            "War" to 10752,
            "Western" to 37
        )

        val SortByDictionary: Map<String, String> = mapOf(
            "Popularity" to "popularity.desc",
            "Release date (ascending)" to "primary_release_date.asc",
            "Release date (descending)" to "primary_release_date.desc",
            "Number of votes" to "vote_count.desc"
        )

        fun generateYearsList(): List<String> {
            val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            val years = mutableListOf<String>()
            for (year in currentYear downTo 2014) {
                years.add(year.toString())
            }
            return years
        }
    }
}