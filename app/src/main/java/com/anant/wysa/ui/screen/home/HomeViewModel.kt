package com.anant.wysa.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anant.wysa.ui.model.MovieDisplayData
import com.anant.wysa.model.MovieListData
import com.anant.wysa.repository.MovieRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


private const val INITIAL_PAGE = 1

class HomeViewModel(private val movieRepo: MovieRepo) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uistate = _uiState.asStateFlow()

    private val _favMovieList = MutableStateFlow<List<MovieListData.Result>>(emptyList())
    val favMovieList = _favMovieList.asStateFlow()

    private var currentPage: Int = INITIAL_PAGE
    private var isPageBeingLoaded = false

    init {
        getMovieList(INITIAL_PAGE)
        viewModelScope.launch {
            movieRepo.getFavoriteMovieList().onEach { favList ->
                _favMovieList.update { favList }
                _uiState.update {
                    it.copy(data = it.data.map {
                        it.copy(isAddedToFav = favList.map { it.id }.contains(it.result.id))
                    })
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onLastItemVisible() {
        if (isPageBeingLoaded) return else getMovieList(++currentPage)
    }

    fun getMovieList(page: Int) {
        viewModelScope.launch {
            isPageBeingLoaded = true
            try {
                _uiState.update { it.copy(isLoading = true) }
                movieRepo.movieList(page).onSuccess { response ->
                    isPageBeingLoaded = false
                    _uiState.update {
                        it.copy(isLoading = false, data = it.data + response)
                    }
                }.onFailure { e ->
                    isPageBeingLoaded = false
                    _uiState.update { it.copy(isLoading = false, exception = e) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, exception = e) }
                Log.d("TAG", "getLatestMovie: ${e.printStackTrace()}")
            }
        }
    }

    fun addItemToFav(result: MovieListData.Result) {
        viewModelScope.launch{
            movieRepo.insertMovie(result)
        }
    }

    fun remove(item : Int) {
        viewModelScope.launch{
            movieRepo.deleteMovie(item)
        }
    }
}


data class UIState(
    val isLoading: Boolean = true,
    val data: List<MovieDisplayData> = emptyList(),
    val exception: Throwable? = null
)