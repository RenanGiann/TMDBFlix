package br.com.renangiannella.tmdbflix.ui.activity.register

import android.app.Application
import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.renangiannella.tmdbflix.data.db.modeldb.User
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class RegisterViewModel (val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher, application: Application): AndroidViewModel(application) {

    fun insert (user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    fun isValid(name: EditText, email: EditText, password: EditText): Int {
        return if (TextUtils.isEmpty(name.text.toString()) && TextUtils.isEmpty(email.text.toString()) && TextUtils.isEmpty(password.text.toString())) {
            return 0
        } else if (TextUtils.isEmpty(name.text.toString())) {
            return 1
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches() || TextUtils.isEmpty(email.text.toString())) {
            return 2
        } else if (TextUtils.isEmpty(password.text.toString())) {
            return 3
        } else -1
    }

    class RegisterViewModelFactory(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher, private val application: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(repository, ioDispatcher, application) as T
            }
            throw IllegalArgumentException("unknown viewmodel class")
        }

    }
}