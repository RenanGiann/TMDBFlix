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

    fun getGenreMovie(apiKey: String, language: String, genre: Int) = viewModelScope.launch {
        movieResponse.value = State.loading(true)
        val response = withContext(ioDispatcher) {
            repository.getMovieGenre(apiKey, language, genre)
        }
        movieResponse.postValue(randomMovieResponse(response))
    }

    fun getFavoriteMovie(userEmail: String): LiveData<List<FavoriteMovie>> = repository.getFavoriteMovie(userEmail)

    suspend fun deleteMovie(favoriteMovie: FavoriteMovie) = repository.deleteFavoriteMovie(favoriteMovie)

    fun randomMovieResponse(response: Response<MovieResponse>): State<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return State.success(resultResponse)
            }
        }
        return State.errorMessage(response.message(), response.code())
    }

    suspend fun insertMovie(favoriteMovie: FavoriteMovie) =
        repository.insertFavoriteMovie(favoriteMovie)


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