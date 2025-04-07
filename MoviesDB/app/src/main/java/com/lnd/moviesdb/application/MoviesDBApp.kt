package com.lnd.moviesdb.application

import android.app.Application
import com.lnd.moviesdb.data.MovieRepository
import com.lnd.moviesdb.data.db.MovieDatabase

//clase que representa toda la aplicacion
//va a ser singleton para solo tener una instancia de la bd
class MoviesDBApp: Application() {
    // lazy no gasta recursos hasta que se vaya a utlizar
    private val database by lazy {
        MovieDatabase.getDatabase(this@MoviesDBApp)
    }
    val repository by lazy {
        MovieRepository(database.movieDao())
    }
}