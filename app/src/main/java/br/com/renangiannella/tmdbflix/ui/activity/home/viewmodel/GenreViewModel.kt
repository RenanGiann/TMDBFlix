package br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel

import android.app.Application
import androidx.lifecycle.*
import br.com.renangiannella.tmdbflix.core.State
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.model.response.MovieResponse
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class GenreViewModel(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher): ViewModel() {

    val movieResponse = MutableLiveData<State<MovieResponse>>()
    val genreMovieResponse = MutableLiveData<State<MovieResponse>>()

    fun getGenreMovie(apiKey: String, language: String, genre: Int) = viewModelScope.launch {
        try {
            genreMovieResponse.value = State.loading(true)
            val _response= withContext(ioDispatcher) {
                repository.getMovieGenre(apiKey, language, genre)
            }
            genreMovieResponse.value = State.success(_response)
        } catch (t: Throwable) {
            genreMovieResponse.postValue(State.error(t))
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