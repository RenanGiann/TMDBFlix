package br.com.renangiannella.tmdbflix.ui.activity.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.renangiannella.tmdbflix.core.State
import br.com.renangiannella.tmdbflix.data.model.response.MovieResponse
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher): ViewModel() {

    val searchResponse = MutableLiveData<State<MovieResponse>>()

    fun searchMovies(query: String, apiKey: String, language: String) = viewModelScope.launch {
        searchResponse.value = State.loading(true)
        try {
            val _response = withContext(ioDispatcher) {
                repository.searchMovies(query, apiKey, language)
            }
            searchResponse.value = State.success(_response)
        } catch (t: Throwable) {
            searchResponse.postValue(State.error(t))
        }
    }

    class SearchViewModelFactory(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                return SearchViewModel(repository, ioDispatcher) as T
            }
            throw IllegalArgumentException("unknow viewmodel class")
        }
    }

}




