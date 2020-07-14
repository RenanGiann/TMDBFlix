package br.com.renangiannella.tmdbflix

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineDispatcher

class MovieViewModelProviderFactory(val repository: MovieRepository, val ioDispatcher: CoroutineDispatcher): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository, ioDispatcher) as T
    }


}