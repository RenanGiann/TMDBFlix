package br.com.renangiannella.tmdbflix.ui.activity.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.lifecycle.Observer
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.core.Status
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers

class SearchActivity : AppCompatActivity() {

    lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val repository = MovieRepository(this)
        viewModel = SearchViewModel.SearchViewModelFactory(repository, Dispatchers.IO).create(SearchViewModel::class.java)

        viewModel.searchResponse.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.i("SEARCH", ">>>>>>> Movie response ${it.data?.results?.get(0)?.original_title}")
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


}