package com.anant.wysa.repository

import com.anant.wysa.model.MovieDetailsData
import com.anant.wysa.model.MovieListData
import com.anant.wysa.util.Constant
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPIService {

    @GET("discover/movie")
    suspend fun movieList(
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("language") language: String = Constant.LANGUAGE,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = Constant.SORT_BY,
        @Query("api_key") apiKey: String = Constant.API_KEY
    ): MovieListData

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = Constant.API_KEY
    ): MovieDetailsData

}