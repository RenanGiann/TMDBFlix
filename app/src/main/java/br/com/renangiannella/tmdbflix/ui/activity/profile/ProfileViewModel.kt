package br.com.renangiannella.tmdbflix.ui.activity.profile

import android.app.Application
import androidx.lifecycle.*
import br.com.renangiannella.tmdbflix.data.db.modeldb.User
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class ProfileViewModel (val repository: MovieRepository, private val ioDispatcher:CoroutineDispatcher, application: Application): AndroidViewModel(application) {

    fun getUserbyEmail (userEmail: String): LiveData<User> = repository.getUserByEmail(userEmail)

    fun deleteUser (user: User) = viewModelScope.launch {
        repository.deleteUser(user)
    }

    class ProfileViewModelFactory(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher, private val application: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(repository, ioDispatcher, application) as T
            }
            throw IllegalArgumentException("unknown viewmodel class")
        }

    }
}