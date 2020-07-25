package br.com.renangiannella.tmdbflix.ui.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.ui.activity.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login()
    }

    fun login() {
        btnLogin.setOnClickListener {
            when(viewModel.isValid(edtEmail, edtPassword)) {
                0 -> Toast.makeText(this@LoginActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(this@LoginActivity, "Preencha o campo senha", Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(this@LoginActivity, "Preencha o campo email", Toast.LENGTH_SHORT).show()
                else -> {
                    viewModel.getUserDB(edtEmail.text.toString(), edtPassword.text.toString()).observe(this, Observer {user ->
                        user?.let {
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
}