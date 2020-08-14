package br.com.renangiannella.tmdbflix.ui.activity.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.data.utils.SharedPreference
import br.com.renangiannella.tmdbflix.data.utils.Utils.hideKeyboard
import br.com.renangiannella.tmdbflix.ui.activity.home.HomeActivity
import br.com.renangiannella.tmdbflix.ui.activity.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers

class LoginActivity : AppCompatActivity() {

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val repository = MovieRepository(this)
        loginViewModel = LoginViewModel.LoginViewModelFactory(repository, Dispatchers.IO, application).create(
            LoginViewModel::class.java)

        val sharedPref = SharedPreference(this)
        sharedPref.getData(USER)?.let {
            edtEmail.setText(it)
        }

        login(sharedPref)
        register()
    }

    private fun register() {
        registerText.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    fun login(sharedPref: SharedPreference) {
        btnLogin.setOnClickListener {
            when(loginViewModel.isValid(edtEmail, edtPassword)) {
                0 -> Toast.makeText(this@LoginActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(this@LoginActivity, "Preencha o campo senha", Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(this@LoginActivity, "Preencha o campo email", Toast.LENGTH_SHORT).show()
                else -> {
                    loginViewModel.getUserDB(edtEmail.text.toString(), edtPassword.text.toString()).observe(this, Observer {user ->
                        user?.let {
                            sharedPref.saveData(USER, it.email)
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        } ?: kotlin.run {
                            Toast.makeText(this@LoginActivity, "email ou senha inválidos", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    companion object {
        const val USER = "USER"
    }

}