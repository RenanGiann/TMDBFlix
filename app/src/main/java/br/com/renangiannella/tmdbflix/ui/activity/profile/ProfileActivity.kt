package br.com.renangiannella.tmdbflix.ui.activity.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.data.db.modeldb.User
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.home.HomeActivity
import br.com.renangiannella.tmdbflix.ui.activity.login.LoginActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.include_toobar.*
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

class ProfileActivity : AppCompatActivity() {

    lateinit var viewModel: ProfileViewModel
    lateinit var userEmail: String
    lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        toolbarMovies.title = "Meu Perfil"
        setSupportActionBar(toolbarMovies)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val repository = MovieRepository(this)
        viewModel = ProfileViewModel.ProfileViewModelFactory(repository, Dispatchers.IO, application ).create(ProfileViewModel::class.java)

        HomeActivity.getData(this)?.let { loggedUser ->
            userEmail = loggedUser
        }

        viewModel.getUserbyEmail(userEmail).observe(this, Observer {
            it?.let { user ->
                nameProfile.text = user.name
                emailProfile.text = user.email
                passwordProfile.text = user.password
                mUser = user
            }
        })

        btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        deleteUser()
    }

    fun deleteUser() {
        btnDeleteAccount.setOnClickListener {
            it?.let {
                viewModel.deleteUser(mUser)
            }
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}