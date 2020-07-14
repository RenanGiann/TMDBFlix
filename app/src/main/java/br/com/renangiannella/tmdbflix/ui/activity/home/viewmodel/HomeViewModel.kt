package br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.renangiannella.tmdbflix.core.State
import br.com.renangiannella.tmdbflix.data.model.response.MovieResponse
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class HomeViewModel(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher): ViewModel() {

    val movieResponse = MutableLiveData<State<MovieResponse>>()

    fun getPopularMovie(apiKey: String, language: String) = viewModelScope.launch {
        movieResponse.value = State.loading(true)
        val response = withContext(ioDispatcher) {
            repository.getMoviePopular(apiKey, language)
        }
        movieResponse.postValue(randomMovieResponse(response))
    }

    fun randomMovieResponse(response: Response<MovieResponse>): State<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return State.success(resultResponse)
            }
        }
        return State.errorMessage(response.message(), response.code())
    }
}