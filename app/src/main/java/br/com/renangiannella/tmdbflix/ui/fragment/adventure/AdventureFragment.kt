package br.com.renangiannella.tmdbflix.ui.fragment.adventure

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
import kotlinx.android.synthetic.main.fragment_adventure.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdventureFragment: Fragment() {

    lateinit var mAdapter: MovieAdapter
    lateinit var viewModel: GenreViewModel
    var listFavorite = listOf<FavoriteMovie>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_adventure, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = (it as HomeActivity).viewModel
            val idAdventure: Int = 12

            viewModel.getGenreMovie(BuildConfig.API_KEY, "pt-BR", idAdventure)

            viewModel.getFavoriteMovie("renan@zup.com").observe(viewLifecycleOwner, Observer {
                it?.let {
                    listFavorite = it
                }
            })

            viewModel.movieResponse.observe(viewLifecycleOwner, Observer { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        response.data?.let {
                            setupRecyclerView(it.results)
                        }
                    }
                    Status.ERROR -> {
                        response.errorMessage?.let {
                            Log.d("AdventureFragment", "Error Message $it")
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
            GlobalScope.launch {
                viewModel.insertMovie(
                    FavoriteMovie(
                        movieResult.id, "renan@zup.com", movieResult.poster_path,
                        movieResult.overview, movieResult.release_date,
                        movieResult.genre_ids, movieResult.original_title, movieResult.vote_average
                    )
                )
            }
        }, { movieResult ->
            GlobalScope.launch {
                viewModel.deleteMovie(
                    FavoriteMovie(
                        movieResult.id, "renan@zup.com", movieResult.poster_path,
                        movieResult.overview, movieResult.release_date,
                        movieResult.genre_ids, movieResult.original_title, movieResult.vote_average
                    )
                )
            }
        })
        with(recyclerViewAdventure) {
            layoutManager = GridLayoutManager(activity, 2)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }
}