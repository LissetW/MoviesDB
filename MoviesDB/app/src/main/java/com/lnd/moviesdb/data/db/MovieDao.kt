package com.lnd.moviesdb.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lnd.moviesdb.data.db.model.MovieEntity
import com.lnd.moviesdb.util.Constants

@Dao
interface MovieDao {
    // Create
    @Insert
    suspend fun insertMovie(movie: MovieEntity)

    @Insert
    suspend fun insertMovie(movie: MutableList<MovieEntity>)

    // Read
    @Query("SELECT * FROM ${Constants.DATABASE_MOVIE_TABLE}")
    suspend fun getAllMovies(): MutableList<MovieEntity>

    @Query("SELECT * FROM ${Constants.DATABASE_MOVIE_TABLE} WHERE movie_id = :movieId")
    suspend fun getMovieById(movieId: Long): MovieEntity?

    //@Transaction ... tienen rollback automatico

    // Update
    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Update
    suspend fun updateMovie(movies: MutableList<MovieEntity>)

    // Delete
    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movies: MutableList<MovieEntity>)
}