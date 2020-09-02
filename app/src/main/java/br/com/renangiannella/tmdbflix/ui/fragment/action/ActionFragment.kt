package br.com.renangiannella.tmdbflix.ui.fragment.action

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
import br.com.renangiannella.tmdbflix.data.model.result.MovieResult
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.home.HomeActivity
import br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel.GenreViewModel
import br.com.renangiannella.tmdbflix.ui.fragment.MovieGenreViewModelProviderFactory
import br.com.renangiannella.tmdbflix.ui.fragment.adapter.MovieAdapter
import kotlinx.android.synthetic.main.fragment_action.*
import kotlinx.android.synthetic.main.fragment_popular.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ActionFragment: Fragment() {

    lateinit var mAdapter: MovieAdapter
    lateinit var viewModel: GenreViewModel
    lateinit var userEmail: String
    var listFavorite = listOf<FavoriteMovie>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_action, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = (it as HomeActivity).viewModel
            val idAction = 28

            viewModel.getActionMovie(BuildConfig.API_KEY, "pt-BR", idAction)

            HomeActivity.getData(it)?.let {loggedUser ->
                userEmail = loggedUser
            }

            viewModel.getFavoriteMovie(userEmail).observe(viewLifecycleOwner, Observer {
                it?.let {
                    listFavorite = it
                }
            })

            viewModel.actionMovieResponse.observe(viewLifecycleOwner, Observer { response ->
                loadingAction.visibility = if (response.loading == true) View.VISIBLE else View.GONE
                when (response.status) {
                    Status.SUCCESS -> {
                        response.data?.let {
                            setupRecyclerView(it.results)
                        }
                    }
                    Status.ERROR -> {
                        response.errorMessage?.let {
                            Log.d("ActionFragment", "Error Message $it")
                        }
                    }

                    Status.LOADING -> {

                    }
                }
            })
        }

    }

    private fun setupRecyclerView(movies: List<MovieResult>) {
        mAdapter = MovieAdapter(movies, listFavorite, {}, { movieResult ->
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
        })
        with(recyclerViewAction) {
            layoutManager = GridLayoutManager(activity, 2)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }
}