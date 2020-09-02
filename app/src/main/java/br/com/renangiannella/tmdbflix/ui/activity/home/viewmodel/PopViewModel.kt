package br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel

import androidx.lifecycle.*
import br.com.renangiannella.tmdbflix.core.State
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.model.response.MovieResponse
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class PopViewModel(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher): ViewModel() {

    val movieResponse = MutableLiveData<State<MovieResponse>>()
    val popMovieResponse = MutableLiveData<State<MovieResponse>>()

    fun getPopularMovie(apiKey: String, language: String) = viewModelScope.launch {
        try {
            popMovieResponse.value = State.loading(true)
            val _response = withContext(ioDispatcher) {
                repository.getMoviePopular(apiKey, language)
            }
            popMovieResponse.value = State.success(_response)
        } catch (t: Throwable) {
            popMovieResponse.postValue(State.error(t))
        }

    }

    fun getFavoriteMovie(userEmail: String): LiveData<List<FavoriteMovie>> = repository.getFavoriteMovie(userEmail)

    fun deleteMovie(favoriteMovie: FavoriteMovie) = viewModelScope.launch {
        repository.deleteFavoriteMovie(favoriteMovie)
    }

    fun insertMovie(favoriteMovie: FavoriteMovie) = viewModelScope.launch {
        repository.insertFavoriteMovie(favoriteMovie)
    }

    class MovieViewModelProviderFactory(val repository: MovieRepository, val ioDispatcher: CoroutineDispatcher) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PopViewModel::class.java)) {
                return PopViewModel(repository, ioDispatcher) as T
            }
            throw IllegalArgumentException("unknown viewmodel class")
        }
    }
}