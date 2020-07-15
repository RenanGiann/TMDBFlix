package br.com.renangiannella.tmdbflix.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.MovieViewModelProviderFactory
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.core.Status
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers

class ActionFragment: Fragment() {

    private val viewModel: HomeViewModel by lazy {
        val viewModelProviderFactory =
            MovieViewModelProviderFactory(MovieRepository(), Dispatchers.IO)
        ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_action, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getPopularMovie(BuildConfig.API_KEY, "pt-BR")

        viewModel.movieResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        //it.results
                        Log.d("ActionFragment", "FILME -> ${it.results[1].original_title}")
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