package com.lnd.moviesdb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lnd.moviesdb.util.Constants

@Entity(tableName = Constants.DATABASE_MOVIE_TABLE)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.COLUMN_MOVIE_ID)
    var id: Long = 0,
    @ColumnInfo(name = Constants.COLUMN_MOVIE_TITLE)
    var title: String,
    @ColumnInfo(name = Constants.COLUMN_MOVIE_GENRE)
    var genre: String,
    @ColumnInfo(name = Constants.COLUMN_MOVIE_DIRECTOR)
    var director: String,
)