package com.lnd.moviesdb.ui

import androidx.recyclerview.widget.RecyclerView
import com.lnd.moviesdb.R
import com.lnd.moviesdb.data.db.model.MovieEntity
import com.lnd.moviesdb.databinding.MovieElementBinding

class MovieViewHolder (
    private val binding: MovieElementBinding
): RecyclerView.ViewHolder(binding.root)
{
    // Programar el viewholder
    fun bind(movie: MovieEntity){
        binding.apply {
            tvTitle.text = movie.title
            tvGenre.text = movie.genre
            tvDeveloper.text = movie.director

            val genres = binding.root.context.resources.getStringArray(R.array.genre_array)

            when(movie.genre){
                genres[0] -> ivIcon.setImageResource(R.drawable.action)
                genres[1] -> ivIcon.setImageResource(R.drawable.comedy)
                genres[2] -> ivIcon.setImageResource(R.drawable.drama)
                genres[3] -> ivIcon.setImageResource(R.drawable.fiction)
                genres[4] -> ivIcon.setImageResource(R.drawable.romance)
                genres[5] -> ivIcon.setImageResource(R.drawable.horror)
            }
        }
    }
}