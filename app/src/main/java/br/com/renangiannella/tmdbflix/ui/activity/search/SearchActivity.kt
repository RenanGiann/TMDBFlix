package br.com.renangiannella.tmdbflix.ui.activity.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.core.Status
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.model.response.MovieResponse
import br.com.renangiannella.tmdbflix.data.model.result.MovieResult
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.favorite.FavoriteAdapter
import br.com.renangiannella.tmdbflix.ui.activity.home.HomeActivity
import br.com.renangiannella.tmdbflix.ui.fragment.adapter.MovieAdapter
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_popular.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    lateinit var viewModel: SearchViewModel
    lateinit var searchAdapter: SearchAdapter
    lateinit var userEmail: String
    var listFavorite = listOf<FavoriteMovie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val repository = MovieRepository(this)
        viewModel = SearchViewModel.SearchViewModelFactory(repository, Dispatchers.IO).create(SearchViewModel::class.java)

        HomeActivity.getData(this)?.let {
            userEmail = it
        }

        viewModel.getFavoriteMovie(userEmail).observe(this, Observer {
            it?.let {
                listFavorite = it
            }
        })

        viewModel.searchResponse.observe(this, Observer {
            loading.visibility = if (it.loading == true) VISIBLE else GONE
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { result ->
                        setupRecycleView(result)
                    }
                }
                Status.ERROR -> {
                    Log.i("SEARCH", ">>>>>>ERROR $it")
                }
                Status.LOADING -> {
                }
            }
        })

        searchBar.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchMovies(it, BuildConfig.API_KEY, "pt-BR")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i("QUERY", "Palavra enviada para request -> $newText")
                return true
            }

        } )

    }

    private fun setupRecycleView(result: MovieResponse) {
        searchAdapter = SearchAdapter(result.results, listFavorite, {}, { movieResult ->
            viewModel.insertMovie(
                FavoriteMovie(
                    movieResult.id,
                    userEmail,
                    movieResult.poster_path,
                    movieResult.overview,
                    movieResult.release_date,
                    movieResult.genre_ids,
                    movieResult.original_title,
                    movieResult.vote_average
                )
            )
        }, {
            viewModel.deleteMovie(
                FavoriteMovie(
                    it.id, userEmail, it.poster_path, it.overview, it.release_date,
                    it.genre_ids, it.original_title, it.vote_average
                )
            )
        })
        recycleSearch.apply {
            layoutManager = GridLayoutManager(this@SearchActivity, 2)
            setHasFixedSize(true)
            adapter = searchAdapter
        }
    }


}

