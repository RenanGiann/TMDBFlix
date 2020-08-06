package br.com.renangiannella.tmdbflix.ui.activity.favorite

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.data.utils.SharedPreference
import br.com.renangiannella.tmdbflix.ui.activity.favorite.viewmodel.FavoriteViewModel
import br.com.renangiannella.tmdbflix.ui.activity.home.HomeActivity
import br.com.renangiannella.tmdbflix.ui.activity.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.include_toobar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    lateinit var userEmail: String
    lateinit var viewModel: FavoriteViewModel
    lateinit var favoriteAdapter: FavoriteAdapter

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

        deleteMovieSwipe(recyclerViewFavorite)

        viewModel.getFavoriteMovie(userEmail).observe(this, Observer {
            it?.let {
                with(recyclerViewFavorite){
                    favoriteAdapter = FavoriteAdapter(it, {}, {favorite ->
                        viewModel.deleteMovie(favorite)
                        showSnackBar(recyclerViewFavorite, favorite)
                    })
                    layoutManager = GridLayoutManager(this@FavoriteActivity, 2)
                    setHasFixedSize(true)
                    adapter = favoriteAdapter
                }
            }
        })
    }

    private fun deleteMovieSwipe(view: View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val favorite = favoriteAdapter.getCurrentMovie(viewHolder.adapterPosition)
                viewModel.deleteMovie(favorite)
                showSnackBar(view, favorite)
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recyclerViewFavorite)
        }
    }

    private fun showSnackBar(view: View, favoriteMovie: FavoriteMovie) {
        Snackbar.make(view, getString(R.string.dislikeTitleSnack), Snackbar.LENGTH_LONG).apply {
            setAction(getString(R.string.cancelSnack)){
                viewModel.insertMovie(favoriteMovie)
            }
            show()
        }
    }


}