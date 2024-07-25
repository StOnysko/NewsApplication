package com.example.newsapplication.application.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapplication.R
import com.example.newsapplication.application.model.localdb.Article
import com.example.newsapplication.application.room.ArticleLocalDatabase
import com.example.newsapplication.application.ui.adapters.ArticleListAdapter
import com.example.newsapplication.application.ui.adapters.interfaces.OnArticleClickListener
import com.example.newsapplication.application.viewmodel.ArticleViewModel
import com.example.newsapplication.application.viewmodel.repository.ArticleRepository
import com.example.newsapplication.databinding.FragmentArticleSavedBinding
import com.example.newsapplication.databinding.LayoutArticleListItemBinding

class ArticleSavedFragment : Fragment() {
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var articleList: ArticleListAdapter
    private var _binding: FragmentArticleSavedBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleSavedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = ArticleLocalDatabase.getDatabaseInstance(requireContext()).articleDao()
        val repository = ArticleRepository(dao)
        articleViewModel = ArticleViewModel(repository)
        initBookmarksList()
        val articleView = LayoutArticleListItemBinding.inflate(layoutInflater)
        articleView.actionBtn.setImageResource(R.drawable.vector_delete)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBookmarksList() = binding.apply {
        articleList = ArticleListAdapter(true, object : OnArticleClickListener {
            override fun onClickOpenArticle(article: Article) {
                openArticleThroughWebView(article.url.toString())
            }

            override fun onAddArticleClickAction(article: Article) {
                deleteArticleFromBookmarks(article = article)
                Toast.makeText(requireContext(), "Removed", Toast.LENGTH_SHORT).show()
            }
        })
        rcView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rcView.setHasFixedSize(false)
        rcView.adapter = articleList

        articleViewModel.localArticles.observe(viewLifecycleOwner) { article ->
            articleList.submitList(article)
            for (elem in article) {
                Log.d("LISTCHECK", "${elem.id}")
            }
        }
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

    private fun deleteArticleFromBookmarks(article: Article) {
        articleViewModel.removeArticle(article = article)
    }
}