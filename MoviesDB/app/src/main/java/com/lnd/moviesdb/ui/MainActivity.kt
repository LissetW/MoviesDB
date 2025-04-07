package com.lnd.moviesdb.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lnd.moviesdb.R
import com.lnd.moviesdb.application.MoviesDBApp
import com.lnd.moviesdb.data.MovieRepository
import com.lnd.moviesdb.data.db.model.MovieEntity
import com.lnd.moviesdb.databinding.ActivityMainBinding
import com.lnd.moviesdb.util.Constants
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var movies: MutableList<MovieEntity> = mutableListOf()
    private lateinit var repository: MovieRepository
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repository = (application as MoviesDBApp).repository
        // Instanciar el movie adapter
        movieAdapter = MovieAdapter{ selectedMovie ->
            val dialog = MovieDialog(
                newMovie = false,
                movie = selectedMovie,
                updateUI = {
                    updateUI()
                },
                message = { text ->
                    message(text)
                })
            dialog.show(supportFragmentManager, Constants.MOVIE_DIALOG_2)
        }
        binding.apply {
            rvMovies.layoutManager = LinearLayoutManager(this@MainActivity)
            rvMovies.adapter = movieAdapter
        }
        updateUI()
    }
    private fun updateUI(){
        lifecycleScope.launch {
            // Obtener todas las peliculas
            movies = repository.getAllMovies()

            binding.tvNoRecords.visibility =
                if(movies.isNotEmpty()) View.INVISIBLE else View.VISIBLE
            movieAdapter.updateList(movies)
        }
    }

    fun click(view: View){
        // Mostrar el dialogo
        val dialog = MovieDialog(
            updateUI = {
                updateUI()
            },
            message = { text ->
                message(text)
            })
        dialog.show(supportFragmentManager, Constants.MOVIE_DIALOG_1)
    }
    private fun message(text: String){
        Snackbar.make(binding.main, text, Snackbar.LENGTH_SHORT)
            .setTextColor(getColor(R.color.colorBackground))
            .setBackgroundTint(getColor(R.color.my_blue))
            .show()
    }
}