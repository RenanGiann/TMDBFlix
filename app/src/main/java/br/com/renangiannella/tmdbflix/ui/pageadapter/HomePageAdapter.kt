package br.com.renangiannella.tmdbflix.ui.pageadapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import br.com.renangiannella.tmdbflix.ui.fragment.ActionFragment
import br.com.renangiannella.tmdbflix.ui.fragment.AdventureFragment
import br.com.renangiannella.tmdbflix.ui.fragment.ComedyFragment
import br.com.renangiannella.tmdbflix.ui.fragment.PopularFragment

class HomePageAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> PopularFragment()
            1 -> ActionFragment()
            2 -> ComedyFragment()
            3 -> AdventureFragment()
            else -> PopularFragment()
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "+ Pop"
            1 -> "Ação"
            2 -> "Comédia"
            3 -> "Aventura"
            else -> null
        }
    }
}