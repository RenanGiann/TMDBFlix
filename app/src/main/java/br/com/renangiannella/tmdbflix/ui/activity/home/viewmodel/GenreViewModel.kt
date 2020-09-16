package br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel

import android.app.Application
import androidx.lifecycle.*
import br.com.renangiannella.tmdbflix.core.State
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.db.modeldb.WatchMovie
import br.com.renangiannella.tmdbflix.data.model.response.MovieResponse
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class GenreViewModel(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher): ViewModel() {

    val movieResponse = MutableLiveData<State<MovieResponse>>()
    val actionMovieResponse = MutableLiveData<State<MovieResponse>>()
    val comedyMovieResponse = MutableLiveData<State<MovieResponse>>()
    val adventureMovieResponse = MutableLiveData<State<MovieResponse>>()

    fun getActionMovie(apiKey: String, language: String, genre: Int) = viewModelScope.launch {
        try {
            actionMovieResponse.value = State.loading(true)
            val _response= withContext(ioDispatcher) {
                repository.getMovieGenre(apiKey, language, genre)
            }
            actionMovieResponse.value = State.success(_response)
        } catch (t: Throwable) {
            actionMovieResponse.postValue(State.error(t))
        }

    }

    fun getComedyMovie(apiKey: String, language: String, genre: Int) = viewModelScope.launch {
        try {
            comedyMovieResponse.value = State.loading(true)
            val _response= withContext(ioDispatcher) {
                repository.getMovieGenre(apiKey, language, genre)
            }
            comedyMovieResponse.value = State.success(_response)
        } catch (t: Throwable) {
            comedyMovieResponse.postValue(State.error(t))
        }

    }

    fun getAdventureMovie(apiKey: String, language: String, genre: Int) = viewModelScope.launch {
        try {
            adventureMovieResponse.value = State.loading(true)
            val _response= withContext(ioDispatcher) {
                repository.getMovieGenre(apiKey, language, genre)
            }
            adventureMovieResponse.value = State.success(_response)
        } catch (t: Throwable) {
            adventureMovieResponse.postValue(State.error(t))
        }

    }

    fun getFavoriteMovie(userEmail: String): LiveData<List<FavoriteMovie>> = repository.getFavoriteMovie(userEmail)

    fun deleteMovie(favoriteMovie: FavoriteMovie) = viewModelScope.launch {
        repository.deleteFavoriteMovie(favoriteMovie)
    }

    fun insertMovie(favoriteMovie: FavoriteMovie) =
        viewModelScope.launch {
            repository.insertFavoriteMovie(favoriteMovie)
        }

    fun getWatchMovie(userEmail: String): LiveData<List<WatchMovie>> = repository.getWatchMovie(userEmail)

    fun deleteWatchMovie(watchMovie: WatchMovie) = viewModelScope.launch {
        repository.deleteWatchMovie(watchMovie)
    }

    fun insertWatchMovie(watchMovie: WatchMovie) = viewModelScope.launch {
            repository.insertWatchMovie(watchMovie)
        }


    class MovieGenreViewModelProviderFactory(
        val repository: MovieRepository,
        val ioDispatcher: CoroutineDispatcher
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GenreViewModel::class.java)) {
                return GenreViewModel(repository, ioDispatcher) as T
            }
            throw IllegalArgumentException("unknown viewmodel class")
        }
    }

}