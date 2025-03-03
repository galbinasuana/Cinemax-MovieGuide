package com.example.cinemax.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemax.R
import com.example.cinemax.data.Movie
import com.squareup.picasso.Picasso

class MoviesAdapter(private var movies: List<Movie>) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie = movies[position]
        holder.bind(currentMovie)
    }

    override fun getItemCount() = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.movie_title)
        private val overviewTextView: TextView = itemView.findViewById(R.id.movie_overview)
        private val posterImageView: ImageView = itemView.findViewById(R.id.movie_poster)
        private val genresTextView: TextView = itemView.findViewById(R.id.movie_genres);
        private val releaseDateTextView: TextView = itemView.findViewById(R.id.movie_release_date)
        private val avgRatingTextView: TextView = itemView.findViewById(R.id.movie_avg_rating)

        fun bind(movie: Movie) {
            titleTextView.text = movie.title
            overviewTextView.text = movie.overview
            Picasso.get().load(movie.posterPath).into(posterImageView)
            genresTextView.text = movie.genres
            releaseDateTextView.text = movie.releaseDate
            avgRatingTextView.text = String.format("%.2f", movie.avgRating)
        }
    }
}