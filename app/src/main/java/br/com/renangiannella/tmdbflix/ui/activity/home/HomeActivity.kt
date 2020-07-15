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
import br.com.renangiannella.tmdbflix.ui.pageadapter.HomePageAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.Dispatchers

class HomeActivity : AppCompatActivity() {

    private val fragmentAdapter = HomePageAdapter(supportFragmentManager)
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when(item.itemId) {
//            R.id.menuFavorite -> {
//
//            }
//            R.id.menuSearch -> {
//
//            }
//            R.id.menuPerson -> {
//
//            }
            else -> false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewPagerMain.adapter = fragmentAdapter
        tabsMain.setupWithViewPager(viewPagerMain)

        val bottomNavigation: BottomNavigationView = bottomNavigationView
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }
}