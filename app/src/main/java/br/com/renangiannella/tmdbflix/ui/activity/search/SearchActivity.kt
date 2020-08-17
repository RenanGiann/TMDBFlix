package br.com.renangiannella.tmdbflix.ui.activity.search

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.core.Status
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.db.modeldb.WatchMovie
import br.com.renangiannella.tmdbflix.data.model.response.MovieResponse
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.data.utils.Utils
import br.com.renangiannella.tmdbflix.data.utils.Utils.hideKeyboard
import br.com.renangiannella.tmdbflix.ui.activity.home.HomeActivity
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.include_toobar.*
import kotlinx.coroutines.Dispatchers

class SearchActivity : AppCompatActivity() {

    lateinit var viewModel: SearchViewModel
    lateinit var searchAdapter: SearchAdapter
    lateinit var userEmail: String
    var listFavorite = listOf<FavoriteMovie>()
    var listWatch = listOf<WatchMovie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbarMovies.title = "Pesquisa"
        setSupportActionBar(toolbarMovies)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val repository = MovieRepository(this)
        viewModel = SearchViewModel.SearchViewModelFactory(repository, Dispatchers.IO)
            .create(SearchViewModel::class.java)

        viewModel.getTopRatedMovies(BuildConfig.API_KEY, "pt-BR")

        viewModel.movieResponse.observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        setupRecycleView(it)
                    }
                }
                Status.ERROR -> {
                    response.errorMessage?.let {
                        Log.d("Top Rated Activity", "Error Message $it")
                    }
                }

                Status.LOADING -> {

                }
            }
        })

        HomeActivity.getData(this)?.let {
            userEmail = it
        }

        viewModel.getFavoriteMovie(userEmail).observe(this, Observer {
            it?.let {
                listFavorite = it
            }
        })

        viewModel.getWatchMovie(userEmail).observe(this, Observer {
            it?.let {
                listWatch = it
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
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i("QUERY", "Palavra enviada para request -> $newText")
                return true
            }

        } )

    }

    private fun setupRecycleView(result: MovieResponse) {
        searchAdapter = SearchAdapter(result.results, listFavorite, listWatch, {}, { movieResult ->
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
        }, {

            viewModel.insertWatchMovie(
                WatchMovie(
                    it.id,
                    userEmail,
                    it.poster_path,
                    it.overview,
                    it.release_date,
                    it.genre_ids,
                    it.original_title,
                    it.vote_average
                )
            )
        }, {
            viewModel.deleteWatchMovie(
                WatchMovie(
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

