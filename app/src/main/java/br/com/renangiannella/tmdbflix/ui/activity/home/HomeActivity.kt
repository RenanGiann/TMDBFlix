package br.com.renangiannella.tmdbflix.ui.activity.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.ClickListener
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.data.utils.SharedPreference
import br.com.renangiannella.tmdbflix.ui.activity.favorite.FavoriteActivity
import br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel.GenreViewModel
import br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel.PopViewModel
import br.com.renangiannella.tmdbflix.ui.activity.login.LoginActivity
import br.com.renangiannella.tmdbflix.ui.activity.profile.ProfileActivity
import br.com.renangiannella.tmdbflix.ui.activity.search.SearchActivity
import br.com.renangiannella.tmdbflix.ui.pageadapter.HomePageAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.include_toobar.*
import kotlinx.coroutines.Dispatchers

class HomeActivity : AppCompatActivity(), ClickListener {

    lateinit var popViewModel: PopViewModel
    lateinit var viewModel: GenreViewModel

    private val fragmentAdapter = HomePageAdapter(supportFragmentManager)
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when(item.itemId) {
            R.id.menuFavorite -> {
                startActivity(Intent(this@HomeActivity, FavoriteActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }
            R.id.menuSearch -> {
                startActivity(Intent(this@HomeActivity, SearchActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }
            R.id.menuPerson -> {
                startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }
            else -> false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val repository = MovieRepository(this)
        viewModel = GenreViewModel.MovieGenreViewModelProviderFactory(repository, Dispatchers.IO).create(GenreViewModel::class.java)
        popViewModel = PopViewModel.MovieViewModelProviderFactory(repository, Dispatchers.IO).create(PopViewModel::class.java)

        ProfileActivity.setFinish(this)

        viewPagerMain.adapter = fragmentAdapter
        tabsMain.setupWithViewPager(viewPagerMain)

        val bottomNavigation: BottomNavigationView = bottomNavigationView
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }



    fun refresh() {
        popViewModel.getPopularMovie(BuildConfig.API_KEY, "pt-BR")
        viewModel.getActionMovie(BuildConfig.API_KEY, "pt-BR", 28)
        viewModel.getAdventureMovie(BuildConfig.API_KEY, "pt-BR", 12)
        viewModel.getComedyMovie(BuildConfig.API_KEY, "pt-BR", 35)
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    companion object {
        const val LOGGED_USER = "USER"

        fun getData(context: Context): String? {
            val sharedPref = SharedPreference(context)
            return sharedPref.getData(LOGGED_USER)
        }

        fun saveImage(context: Context,key: String, image: Int) {
            val sharedPref = SharedPreference(context)
            sharedPref.saveImage(key, image)
        }

        fun getImage(context: Context, key: String): Int? {
            val sharedPref = SharedPreference(context)
            return sharedPref.getSavedImage(key)
        }

    }

    override fun click() {
        finish()
    }
}