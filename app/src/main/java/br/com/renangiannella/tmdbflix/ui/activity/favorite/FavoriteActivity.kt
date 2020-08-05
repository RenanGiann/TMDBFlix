package br.com.renangiannella.tmdbflix.ui.activity.favorite

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.data.utils.SharedPreference
import br.com.renangiannella.tmdbflix.ui.activity.favorite.viewmodel.FavoriteViewModel
import br.com.renangiannella.tmdbflix.ui.activity.home.HomeActivity
import br.com.renangiannella.tmdbflix.ui.activity.login.LoginActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.include_toobar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    lateinit var userEmail: String
    lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        toolbarMovies.title = "Favoritos"
        setSupportActionBar(toolbarMovies)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val repository = MovieRepository(this)
        viewModel = FavoriteViewModel.FavoriteViewModelFactory(repository, Dispatchers.IO).create(
            FavoriteViewModel::class.java)

        HomeActivity.getData(this)?.let { loggedUser ->
            userEmail = loggedUser
        }


        viewModel.getFavoriteMovie(userEmail).observe(this, Observer {
            it?.let {
                with(recyclerViewFavorite){
                    layoutManager = GridLayoutManager(this@FavoriteActivity, 2)
                    setHasFixedSize(true)
                    adapter = FavoriteAdapter(it, {}, {
                            viewModel.deleteMovie(
                                FavoriteMovie(
                                    it.id, userEmail, it.poster_path,
                                    it.overview, it.release_date,
                                    it.genre_ids, it.original_title, it.vote_average
                                )
                            )
                    })
                }
            }
        })
    }




}