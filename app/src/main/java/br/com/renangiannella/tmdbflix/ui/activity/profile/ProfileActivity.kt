package br.com.renangiannella.tmdbflix.ui.activity.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.renangiannella.tmdbflix.ClickListener
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.data.db.modeldb.User
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.home.HomeActivity
import br.com.renangiannella.tmdbflix.ui.activity.login.LoginActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.include_toobar.*
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

class ProfileActivity : AppCompatActivity() {

    lateinit var viewModel: ProfileViewModel
    lateinit var userEmail: String
    lateinit var mUser: User
    lateinit var profileAdapter: ProfileAdapter

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

        HomeActivity.getImage(this, userEmail)?.let {
            if (it != 0) Picasso.get().load(it).into(imgProfile)
        }

        viewModel.getUserbyEmail(userEmail).observe(this, Observer {
            it?.let { user ->
                nameProfile.text = "Olá, ${user.name}"
                emailProfile.text = user.email
                passwordProfile.text = user.password
                mUser = user
            }
        })

        btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            click?.click()
            finish()
        }

        btnEditIcon.setOnClickListener {
            val view = View.inflate(this@ProfileActivity, R.layout.dialog_view, null)

            val builder = AlertDialog.Builder(this@ProfileActivity)

            builder.setView(view)

            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewProfile)
            profileAdapter = ProfileAdapter(imageList()) {
                Picasso.get().load(it).into(imgProfile)
                HomeActivity.saveImage(this@ProfileActivity, userEmail, it)
                dialog.dismiss()
            }

            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
                setHasFixedSize(true)
                adapter = profileAdapter
            }

            dialog.show()
        }


        deleteUser()
    }

    fun imageList() = listOf(R.drawable.avatar1, R.drawable.avatar2,
        R.drawable.avatar3, R.drawable.avatar4,
        R.drawable.avatar5, R.drawable.avatar6,
        R.drawable.avatar7, R.drawable.avatar8)

    fun deleteUser() {
        btnDeleteAccount.setOnClickListener {
            it?.let {
                viewModel.deleteUser(mUser)
            }
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    companion object {
        private var click: ClickListener? = null
        fun setFinish(listener: ClickListener) {
            click = listener
        }
    }
}
