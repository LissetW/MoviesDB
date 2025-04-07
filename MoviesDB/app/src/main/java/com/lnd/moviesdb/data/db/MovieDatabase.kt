package com.lnd.moviesdb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lnd.moviesdb.data.db.model.MovieEntity
import com.lnd.moviesdb.util.Constants

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = true
)

abstract class MovieDatabase: RoomDatabase() {
    //Aqui va el DAO
    abstract fun movieDao(): MovieDao
    companion object{
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    Constants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}