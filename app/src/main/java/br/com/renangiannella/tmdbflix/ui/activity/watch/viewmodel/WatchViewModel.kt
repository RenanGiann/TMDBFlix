package br.com.renangiannella.tmdbflix.ui.activity.watch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.renangiannella.tmdbflix.data.db.modeldb.WatchMovie
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class WatchViewModel (val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher): ViewModel() {

    fun getWatchMovie(userEmail: String): LiveData<List<WatchMovie>> = repository.getWatchMovie(userEmail)

    fun insertWatchMovie(watchMovie: WatchMovie) = viewModelScope.launch {
        repository.insertWatchMovie(watchMovie)
    }

    fun deleteWatchMovie(watchMovie: WatchMovie) = viewModelScope.launch {
        repository.deleteWatchMovie(watchMovie)
    }

    class WatchViewModelFactory(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WatchViewModel::class.java)) {
                return WatchViewModel(repository, ioDispatcher) as T
            }
            throw IllegalArgumentException("unknown viewmodel class")
        }
    }
}