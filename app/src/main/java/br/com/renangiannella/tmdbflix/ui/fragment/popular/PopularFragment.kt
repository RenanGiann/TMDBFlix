package br.com.renangiannella.tmdbflix.ui.fragment.popular

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.core.Status
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.db.modeldb.WatchMovie
import br.com.renangiannella.tmdbflix.data.model.result.MovieResult
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.data.utils.SharedPreference
import br.com.renangiannella.tmdbflix.ui.activity.home.HomeActivity
import br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel.GenreViewModel
import br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel.PopViewModel
import br.com.renangiannella.tmdbflix.ui.activity.login.LoginActivity
import br.com.renangiannella.tmdbflix.ui.fragment.adapter.MovieAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_action.*
import kotlinx.android.synthetic.main.fragment_popular.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PopularFragment: Fragment() {

    lateinit var mAdapter: MovieAdapter
    lateinit var viewModel: PopViewModel
    lateinit var userEmail: String
    var listFavorite = listOf<FavoriteMovie>()
    var listWatch = listOf<WatchMovie>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popular, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = (it as HomeActivity).popViewModel
            viewModel.getPopularMovie(BuildConfig.API_KEY, "pt-BR")

            HomeActivity.getData(it)?.let {loggedUser ->
                userEmail = loggedUser
            }

            viewModel.getFavoriteMovie(userEmail).observe(viewLifecycleOwner, Observer {
                it?.let { favorite ->
                    listFavorite = favorite
                }
            })

            viewModel.getWatchMovie(userEmail).observe(viewLifecycleOwner, Observer {
                it?.let { watch ->
                    listWatch = watch
                }
            })

            viewModel.popMovieResponse.observe(viewLifecycleOwner, Observer { response ->
                loadingPop.visibility = if (response.loading == true) View.VISIBLE else View.GONE
                when (response.status) {
                    Status.SUCCESS -> {
                        response.data?.let {movie ->
                            setupRecyclerView(movie.results)
                            Log.d("PopularFragment", "FILME -> ${movie.results[0].original_title}")
                        }
                    }
                    Status.ERROR -> {
                        response.errorMessage?.let {
                            Log.d("PopularFragment", "Error Message $it")
                        }
                    }

                    Status.LOADING -> {

                    }
                }
            })
        }
    }

    private fun setupRecyclerView(movies: List<MovieResult>) {
        mAdapter = MovieAdapter(movies, listFavorite, listWatch, {}, { movieResult ->
            viewModel.insertMovie(
                FavoriteMovie(
                    movieResult.id, userEmail, movieResult.poster_path,
                    movieResult.overview, movieResult.release_date,
                    movieResult.genre_ids, movieResult.original_title, movieResult.vote_average
                )
            )
        }, { movieResult ->
            viewModel.deleteMovie(
                FavoriteMovie(
                    movieResult.id, userEmail, movieResult.poster_path,
                    movieResult.overview, movieResult.release_date,
                    movieResult.genre_ids, movieResult.original_title, movieResult.vote_average
                )
            )
        }, { movieResult ->
            viewModel.insertWatchMovie(
                WatchMovie(
                    movieResult.id, userEmail, movieResult.poster_path,
                    movieResult.overview, movieResult.release_date,
                    movieResult.genre_ids, movieResult.original_title, movieResult.vote_average
                )
            )

        }, { movieResult ->
            viewModel.deleteWatchMovie(
                WatchMovie(
                    movieResult.id, userEmail, movieResult.poster_path,
                    movieResult.overview, movieResult.release_date,
                    movieResult.genre_ids, movieResult.original_title, movieResult.vote_average
                )
            )
        })
        with(recyclerViewPop) {
            layoutManager = GridLayoutManager(activity, 3)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }
}