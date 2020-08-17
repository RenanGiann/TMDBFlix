package br.com.renangiannella.tmdbflix.ui.activity.watch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.db.modeldb.WatchMovie
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.home.HomeActivity
import br.com.renangiannella.tmdbflix.ui.activity.watch.viewmodel.WatchViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_watch.*
import kotlinx.android.synthetic.main.include_toobar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.Dispatcher

class WatchActivity : AppCompatActivity() {

    lateinit var userEmail: String
    lateinit var viewModel: WatchViewModel
    lateinit var watchAdapter: WatchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch)

        toolbarMovies.title = "Assistir"
        setSupportActionBar(toolbarMovies)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val repository = MovieRepository(this)
        viewModel = WatchViewModel.WatchViewModelFactory(repository, Dispatchers.IO).create(WatchViewModel::class.java)

        HomeActivity.getData(this)?.let { loggedUser ->
            userEmail = loggedUser
        }

        deleteWatchMovieSwipe(recyclerWatch)

        viewModel.getWatchMovie(userEmail).observe(this, Observer {
            it?.let {
                with(recyclerWatch) {
                    watchAdapter = WatchAdapter(it, {}, { watch ->
                        viewModel.deleteWatchMovie(watch)
                        showSnackBar(recyclerWatch, watch)
                    })
                    layoutManager = GridLayoutManager(this@WatchActivity, 2)
                    setHasFixedSize(true)
                    adapter = watchAdapter
                }
            }
        })
    }

    private fun deleteWatchMovieSwipe(view: View) {
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
                val watch = watchAdapter.getCurrentMovie(viewHolder.adapterPosition)
                viewModel.deleteWatchMovie(watch)
                showSnackBar(view, watch)
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recyclerViewFavorite)
        }
    }

    private fun showSnackBar(view: View, watchMovie: WatchMovie) {
        Snackbar.make(view, getString(R.string.dislikeTitleSnack), Snackbar.LENGTH_LONG).apply {
            setAction(getString(R.string.cancelSnack)){
                viewModel.insertWatchMovie(watchMovie)
            }
            show()
        }
    }
}