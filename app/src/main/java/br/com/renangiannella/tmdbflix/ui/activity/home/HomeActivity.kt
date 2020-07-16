package br.com.renangiannella.tmdbflix.ui.activity.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.ui.pageadapter.HomePageAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

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