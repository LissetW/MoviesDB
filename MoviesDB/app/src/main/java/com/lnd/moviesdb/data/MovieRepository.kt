package com.lnd.moviesdb.data

import com.lnd.moviesdb.data.db.MovieDao
import com.lnd.moviesdb.data.db.model.MovieEntity

class MovieRepository (
    private val movieDao: MovieDao){

        suspend fun insertMovie(movie: MovieEntity){
            movieDao.insertMovie(movie)
        }

        suspend fun getAllMovies(): MutableList<MovieEntity> =
            movieDao.getAllMovies()

        suspend fun getMovieById(movieId: Long): MovieEntity? =
            movieDao.getMovieById(movieId)

        suspend fun updateMovie(movie: MovieEntity){
            movieDao.updateMovie(movie)
        }

        suspend fun deleteMmovie(movie: MovieEntity){
            movieDao.deleteMovie(movie)
        }
    }