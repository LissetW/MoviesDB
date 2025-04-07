package com.lnd.moviesdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lnd.moviesdb.data.db.model.MovieEntity
import com.lnd.moviesdb.databinding.MovieElementBinding

class MovieAdapter(
    private val onMovieClick: (MovieEntity) -> Unit
): RecyclerView.Adapter<MovieViewHolder>() {

    private var movies: List<MovieEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener{
            onMovieClick(movie)
        }
    }
    //Actualizamos el adapter para los nuevos elementos actualizados
    fun updateList(list: MutableList<MovieEntity>){
        movies = list
        notifyDataSetChanged()
    }
}