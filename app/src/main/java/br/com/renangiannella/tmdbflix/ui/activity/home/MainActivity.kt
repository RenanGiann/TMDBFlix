package br.com.renangiannella.tmdbflix.ui.activity.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.MovieViewModelProviderFactory
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.core.Status
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by lazy {
       val viewModelProviderFactory = MovieViewModelProviderFactory(MovieRepository(), Dispatchers.IO)
       ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getPopularMovie(BuildConfig.API_KEY, "pt-BR")

        viewModel.movieResponse.observe(this, Observer {response ->
            when(response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        //it.results
                        Log.d("MAINACTIVITY", "FILME -> ${it.results[0].original_title}")
                    }
                }
                Status.ERROR -> {
                    response.errorMessage?.let {
                        Log.d("MAIN ACTIVITY", "Error Message $it")
                    }
                }

                Status.LOADING -> {

                }
            }
        })
    }
}