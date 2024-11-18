package com.anant.wysa.ui.model

import com.anant.wysa.model.MovieListData

data class MovieDisplayData(
    val result: MovieListData.Result,
    val isAddedToFav: Boolean
)