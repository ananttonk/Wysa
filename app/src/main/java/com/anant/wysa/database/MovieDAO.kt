package com.anant.wysa.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anant.wysa.model.MovieListData
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieListData.Result)

    @Delete
    suspend fun deleteMovie(movie: MovieListData.Result)

    @Query("DELETE FROM movieList WHERE id = :id")
    suspend fun deleteMovieById(id:Int)

    @Query("SELECT * FROM movieList")
    fun getMovieList(): Flow<List<MovieListData.Result>>

    @Query("SELECT EXISTS(SELECT * FROM movieList WHERE id = :movieId)")
    suspend fun isMovieExist(movieId: Int): Boolean

}