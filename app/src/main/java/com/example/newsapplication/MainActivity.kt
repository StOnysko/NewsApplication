package com.example.newsapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.newsapplication.application.ui.adapters.ViewPagerAdapter
import com.example.newsapplication.application.ui.fragments.ArticleFeedFragment
import com.example.newsapplication.application.ui.fragments.ArticleSavedFragment
import com.example.newsapplication.databinding.ActivityMainBinding
import com.example.newsapplication.databinding.LayoutArticleListItemBinding
import com.google.android.material.tabs.TabLayoutMediator

private const val FIRST_PAGE: Int = 0
private const val SECOND_PAGE: Int = 1

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTabLayout()
    }

    private fun setupTabLayout() = binding.apply {
        val fragments: List<Fragment> = listOf(ArticleFeedFragment(), ArticleSavedFragment())
        val adapter = ViewPagerAdapter(fragments, this@MainActivity)
        fragmentViewPager.adapter = adapter
        TabLayoutMediator(tabLayout, fragmentViewPager) { tab, position ->
            when (position) {
                FIRST_PAGE -> tab.text = "Feed"
                SECOND_PAGE -> tab.text = "Bookmarks"
            }
        }.attach()
    }
}