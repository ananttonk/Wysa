package com.anant.wysa.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anant.wysa.model.MovieListData
import com.anant.wysa.repository.MovieRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteViewModel(private val movieRepo: MovieRepo): ViewModel() {

    private val _uiState = MutableStateFlow<List<MovieListData.Result>>(emptyList())
    val uiState = _uiState.asStateFlow()

    fun getFavList() {
        viewModelScope.launch {
            movieRepo.getFavoriteMovieList().collect { value->
                _uiState.update { value }
            }
        }
    }

    fun remove(movieId: Int){
        viewModelScope.launch{
            movieRepo.deleteMovie(movieId)
        }
    }

}