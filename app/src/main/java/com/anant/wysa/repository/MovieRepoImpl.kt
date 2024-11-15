package com.anant.wysa.repository

import com.anant.wysa.database.MovieDAO
import com.anant.wysa.displayData.MovieDisplayData
import com.anant.wysa.model.MovieDetailsData
import com.anant.wysa.model.MovieListData

class MovieRepoImpl(private val movieAPIService: MovieAPIService, private val movieDAO: MovieDAO) :
    MovieRepo {
    override suspend fun movieList(page: Int): Result<List<MovieDisplayData>> = runCatching {
        movieAPIService.movieList(page = page).results
    }.map {
        it.map {
            MovieDisplayData(it, isAddedToFav = movieDAO.isMovieExist(it.id))
        }
    }

    override suspend fun movieDetails(movieId: Int): Result<MovieDetailsData> =
        runCatching { movieAPIService.getMovieDetails(movieId) }

    override suspend fun getFavoriteMovieList() = movieDAO.getMovieList()
    override suspend fun insertMovie(movie: MovieListData.Result) = movieDAO.insertMovie(movie)
    override suspend fun deleteMovie(movieId: Int) = movieDAO.deleteMovieById(movieId)
}