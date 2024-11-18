package com.anant.wysa.ui.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anant.wysa.model.MovieDetailsData
import com.anant.wysa.repository.MovieRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(private val movieRepoImpl: MovieRepo): ViewModel() {

    private val _uiState = MutableStateFlow<DetailUIState>(DetailUIState.Init)
    val uistate = _uiState.asStateFlow()

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                _uiState.update { DetailUIState.Loading }
               movieRepoImpl.movieDetails(movieId).onSuccess {response->
                       _uiState.update { DetailUIState.Result(data = response) }
                }.onFailure {e->
                   _uiState.update { DetailUIState.Error(Exception(e)) }
               }
            } catch (e: Exception) {
                _uiState.update { DetailUIState.Error(e) }
            }
        }
    }
}


sealed class DetailUIState{
    object Loading: DetailUIState()
    data class Error(val e : Exception): DetailUIState()
    data class Result(val data: MovieDetailsData ): DetailUIState()
    object Init: DetailUIState()
}