package br.com.renangiannella.tmdbflix.ui.activity.favorite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.favorite.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    lateinit var userEmail: String
    lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val repository = MovieRepository(this)
        viewModel = FavoriteViewModel.FavoriteViewModelFactory(repository, Dispatchers.IO).create(
            FavoriteViewModel::class.java)


        viewModel.getFavoriteMovie("renan@zup.com").observe(this, Observer {
            it?.let {
                with(recyclerViewFavorite){
                    layoutManager = GridLayoutManager(this@FavoriteActivity, 2)
                    setHasFixedSize(true)
                    adapter = FavoriteAdapter(it, {}, {
                        GlobalScope.launch {
                            viewModel.deleteMovie(
                                FavoriteMovie(
                                    it.id, "renan@zup.com", it.poster_path,
                                    it.overview, it.release_date,
                                    it.genre_ids, it.original_title, it.vote_average
                                )
                            )
                        }
                    })
                }
            }
        })
    }


}