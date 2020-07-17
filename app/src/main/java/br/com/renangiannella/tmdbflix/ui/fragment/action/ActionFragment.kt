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
import br.com.renangiannella.tmdbflix.data.model.result.MovieResult
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel.GenreViewModel
import br.com.renangiannella.tmdbflix.ui.fragment.MovieGenreViewModelProviderFactory
import br.com.renangiannella.tmdbflix.ui.fragment.adapter.MovieAdapter
import kotlinx.android.synthetic.main.fragment_action.*
import kotlinx.coroutines.Dispatchers

class ActionFragment: Fragment() {

    lateinit var mAdapter: MovieAdapter

    private val viewModel: GenreViewModel by lazy {
        val viewModelProviderFactory =
            MovieGenreViewModelProviderFactory(
                MovieRepository(),
                Dispatchers.IO
            )
        ViewModelProvider(this, viewModelProviderFactory).get(GenreViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_action, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val idAction: Int = 28

        viewModel.getGenreMovie(BuildConfig.API_KEY, "pt-BR", idAction)

        viewModel.movieResponse.observe(viewLifecycleOwner, Observer { response ->
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

    private fun setupRecyclerView(movies: List<MovieResult>) {
        mAdapter = MovieAdapter(movies, {}, {}, {})
        with(recyclerViewAction) {
            layoutManager = GridLayoutManager(activity, 2)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }
}