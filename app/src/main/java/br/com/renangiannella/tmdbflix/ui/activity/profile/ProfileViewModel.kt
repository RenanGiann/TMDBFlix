package br.com.renangiannella.tmdbflix.ui.activity.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.renangiannella.tmdbflix.data.db.modeldb.User
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository

class ProfileViewModel (val repository: MovieRepository, application: Application): AndroidViewModel(application) {

    fun getUserbyEmail (userEmail: String): LiveData<User> = repository.getUserByEmail(userEmail)

    class ProfileViewModelFactory(val repository: MovieRepository, private val application: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(repository, application) as T
            }
            throw IllegalArgumentException("unknown viewmodel class")
        }

    }
}