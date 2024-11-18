package com.anant.wysa.repository

import com.anant.wysa.ui.model.MovieDisplayData
import com.anant.wysa.model.MovieDetailsData
import com.anant.wysa.model.MovieListData
import kotlinx.coroutines.flow.Flow

interface MovieRepo{
    suspend fun movieList(page: Int): Result<List<MovieDisplayData>>
    suspend fun movieDetails(movieId: Int): Result<MovieDetailsData>
    suspend fun getFavoriteMovieList(): Flow<List<MovieListData.Result>>
    suspend fun insertMovie(movie: MovieListData.Result)
    suspend fun deleteMovie(movieId: Int)
}