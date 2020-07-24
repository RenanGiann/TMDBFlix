package br.com.renangiannella.tmdbflix.ui.activity.login

import android.app.Application
import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.renangiannella.tmdbflix.data.db.modeldb.User
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.favorite.viewmodel.FavoriteViewModel
import kotlinx.coroutines.CoroutineDispatcher
import org.w3c.dom.Text

class LoginViewModel(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher, application: Application): AndroidViewModel(application) {

    fun getUserDB(email: String, password: String): LiveData<User> = repository.getUser(email, password)

    fun isValid(email: EditText, password: EditText): Int {

        return if (TextUtils.isEmpty(email.text.toString()) && TextUtils.isEmpty(password.text.toString())) {
            return  0
        } else if (TextUtils.isEmpty(password.text.toString())) {
            return  1
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches() || TextUtils.isEmpty(email.text.toString())) {
            return 2
        } else -1
    }

    class FavoriteViewModelFactory(val repository: MovieRepository, private val ioDispatcher: CoroutineDispatcher, private val application: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(repository, ioDispatcher, application) as T
            }
            throw IllegalArgumentException("unknown viewmodel class")
        }

    }

}