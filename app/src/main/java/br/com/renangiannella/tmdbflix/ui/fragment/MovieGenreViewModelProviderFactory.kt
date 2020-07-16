package br.com.renangiannella.tmdbflix.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel.GenreViewModel
import kotlinx.coroutines.CoroutineDispatcher

class MovieGenreViewModelProviderFactory(val repository: MovieRepository, val ioDispatcher: CoroutineDispatcher): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GenreViewModel(repository, ioDispatcher) as T
    }
}