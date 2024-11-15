package com.anant.wysa.displayData

import com.anant.wysa.model.MovieListData

data class MovieDisplayData(
    val result: MovieListData.Result,
    val isAddedToFav: Boolean
)