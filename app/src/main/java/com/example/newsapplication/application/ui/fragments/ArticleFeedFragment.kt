package com.example.newsapplication.application.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapplication.application.model.localdb.Article
import com.example.newsapplication.application.room.ArticleLocalDatabase
import com.example.newsapplication.application.ui.adapters.ArticleListAdapter
import com.example.newsapplication.application.ui.adapters.interfaces.OnArticleClickListener
import com.example.newsapplication.application.viewmodel.ArticleViewModel
import com.example.newsapplication.application.viewmodel.repository.ArticleRepository
import com.example.newsapplication.databinding.FragmentArticleFeedBinding

private const val COUNTRY: String = "ua"
private const val API_KEY: String = "41f558ffcba04981b95d5f732811e141"

class ArticleFeedFragment : Fragment() {
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var listAdapter: ArticleListAdapter
    private lateinit var articleList: List<Article>

    private var _binding: FragmentArticleFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleFeedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // application context.
        val dao = ArticleLocalDatabase.getDatabaseInstance(requireContext()).articleDao()
        val repository = ArticleRepository(dao)
        articleViewModel = ArticleViewModel(repository)

        articleViewModel.fetchArticlesFromInternet(COUNTRY, API_KEY)

        initUi()
        observeViewModel()
        searchView()
    }

    private fun observeViewModel() {
        articleViewModel.articles().observe(viewLifecycleOwner) {
            articleList = it
            listAdapter.submitList(articleList)
        }
    }

    private fun initUi() {
        listAdapter = ArticleListAdapter(false, object : OnArticleClickListener {
            override fun onClickOpenArticle(article: Article) {
                openArticleThroughWebView(url = article.url.toString())
            }

            override fun onAddArticleClickAction(article: Article) {
                addArticleToBookmarks(article = article)
                Toast.makeText(requireContext(), "Saved to Bookmarks", Toast.LENGTH_SHORT).show()
            }
        })
        binding.articleListAdapter.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.articleListAdapter.setHasFixedSize(false)
        binding.articleListAdapter.adapter = listAdapter
    }

    private fun openArticleThroughWebView(url: String) = binding.apply {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        webView.visibility = View.VISIBLE
        closeWebViewBtn.visibility = View.VISIBLE

        closeArticleWebView()
    }

    private fun closeArticleWebView() = binding.apply {
        closeWebViewBtn.setOnClickListener {
            webView.loadUrl("about:blank")
            webView.visibility = View.GONE
            closeWebViewBtn.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun searchView() = binding.apply {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return searchFilters(query = query)
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return searchFilters(query = newText)
            }
        })
    }

    private fun searchFilters(query: String?): Boolean {
        val filteredList = articleList.filter {
            it.title!!.contains(query ?: "", ignoreCase = true) || it.description?.contains(
                query ?: "", ignoreCase = true
            ) == true
        }
        if (filteredList.isNotEmpty()) {
            listAdapter.submitList(filteredList)
        }
        return true
    }

    private fun addArticleToBookmarks(article: Article) {
        articleViewModel.insertArticle(article = article)
    }
}
