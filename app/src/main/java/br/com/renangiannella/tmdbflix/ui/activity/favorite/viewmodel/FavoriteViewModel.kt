package br.com.renangiannella.tmdbflix.ui.activity.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher

class FavoriteViewModel(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher): ViewModel() {

    fun getFavoriteMovie(userEmail: String): LiveData<List<FavoriteMovie>> = repository.getFavoriteMovie(userEmail)

    suspend fun deleteMovie(favoriteMovie: FavoriteMovie) = repository.deleteFavoriteMovie(favoriteMovie)

    class FavoriteViewModelFactory(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
                return FavoriteViewModel(repository, ioDispatcher) as T
            }
            throw IllegalArgumentException("unknown viewmodel class")
        }

    }
}