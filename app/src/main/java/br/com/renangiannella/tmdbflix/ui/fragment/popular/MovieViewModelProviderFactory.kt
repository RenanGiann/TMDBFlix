package br.com.renangiannella.tmdbflix.ui.fragment.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel.PopViewModel
import kotlinx.coroutines.CoroutineDispatcher

class MovieViewModelProviderFactory(val repository: MovieRepository, val ioDispatcher: CoroutineDispatcher): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PopViewModel(repository, ioDispatcher) as T
    }


}