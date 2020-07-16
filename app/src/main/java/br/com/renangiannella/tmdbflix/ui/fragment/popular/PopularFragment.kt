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
import br.com.renangiannella.tmdbflix.data.model.result.MovieResult
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel.PopViewModel
import br.com.renangiannella.tmdbflix.ui.fragment.adapter.MovieAdapter
import kotlinx.android.synthetic.main.fragment_popular.*
import kotlinx.coroutines.Dispatchers

class PopularFragment: Fragment() {

    lateinit var mAdapter: MovieAdapter

    private val viewModel: PopViewModel by lazy {
        val viewModelProviderFactory =
            MovieViewModelProviderFactory(
                MovieRepository(),
                Dispatchers.IO
            )
        ViewModelProvider(this, viewModelProviderFactory).get(PopViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popular, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getPopularMovie(BuildConfig.API_KEY, "pt-BR")

        viewModel.movieResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        setupRecyclerView(it.results)
                        Log.d("PopularFragment", "FILME -> ${it.results[0].original_title}")
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

    private fun setupRecyclerView(movies: List<MovieResult>) {
        mAdapter = MovieAdapter(movies, {}, {}, {})
        with(recyclerViewPop) {
            layoutManager = GridLayoutManager(activity, 2)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }
}