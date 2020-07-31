package br.com.renangiannella.tmdbflix.ui.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.data.db.modeldb.User
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.Dispatchers

class RegisterActivity : AppCompatActivity() {

    lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val repository = MovieRepository(this)
        viewModel = RegisterViewModel.RegisterViewModelFactory(repository, Dispatchers.IO, application).create(RegisterViewModel::class.java)

        register()
    }

    fun register() {
        buttonRegister.setOnClickListener {
            when (viewModel.isValid(edtName, edtEmailRegister, edtPasswordRegister)) {
                0 -> Toast.makeText(this@RegisterActivity, "Preencha todos os campos para se cadastrar no aplicativo", Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(this@RegisterActivity, "Preencha o campo nome", Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(this@RegisterActivity, "Preencha corretamente o campo email", Toast.LENGTH_SHORT).show()
                3 -> Toast.makeText(this@RegisterActivity, "Preencha o campo senha", Toast.LENGTH_SHORT).show()
                else -> {
                    viewModel.insert(User(edtEmailRegister.text.toString(), edtName.text.toString(), edtPasswordRegister.text.toString()))
                }
            }
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }
}