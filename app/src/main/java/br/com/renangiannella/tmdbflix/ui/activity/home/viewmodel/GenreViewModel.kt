package br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.renangiannella.tmdbflix.core.State
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.model.response.MovieResponse
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class GenreViewModel(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher, application: Application): AndroidViewModel(application) {

    val movieResponse = MutableLiveData<State<MovieResponse>>()

    fun getGenreMovie(apiKey: String, language: String, genre: Int) = viewModelScope.launch {
        movieResponse.value = State.loading(true)
        val response = withContext(ioDispatcher) {
            repository.getMovieGenre(apiKey, language, genre)
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

    suspend fun insertMovie(favoriteMovie: FavoriteMovie) = repository.insertFavoriteMovie(favoriteMovie)

    suspend fun deleteMovie(favoriteMovie: FavoriteMovie) = repository.deleteFavoriteMovie(favoriteMovie)
}