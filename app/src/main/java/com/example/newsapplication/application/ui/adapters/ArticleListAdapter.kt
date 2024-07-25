package com.example.newsapplication.application.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.newsapplication.R
import com.example.newsapplication.application.ui.adapters.interfaces.OnArticleClickListener
import com.example.newsapplication.application.model.localdb.Article
import com.example.newsapplication.databinding.LayoutArticleListItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ArticleListAdapter(
    private val isDelete: Boolean,
    private val onClickListener: OnArticleClickListener
) :
    ListAdapter<Article, ArticleListAdapter.ArticleItemViewHolder>(ArticleItemDiffUtil()) {

    class ArticleItemViewHolder(
        private val isDelete: Boolean,
        private val binding: LayoutArticleListItemBinding,
        private val onClick: OnArticleClickListener,
    ) : ViewHolder(binding.root) {
        fun bind(item: Article) = with(binding) {
            articleTime.text = formatDate(item.publishedAt.toString())
            articleTitle.text = item.title

            if (isDelete) {
                actionBtn.setImageResource(R.drawable.vector_delete)
            }
            item.urlToImage?.let {
                Glide.with(articleImage.context).load(it).into(articleImage)
                cardView.visibility = View.VISIBLE
            } ?: run {
                cardView.visibility = View.GONE
            }

            item.description?.let {
                articleDescription.text = it
                articleDescription.visibility = View.VISIBLE
            } ?: run {
                articleDescription.visibility = View.GONE
            }

            item.author?.let {
                articleAuthor.text = it
                articleAuthor.visibility = View.VISIBLE
            } ?: run {
                articleAuthor.visibility = View.INVISIBLE
            }

            articleWebOpen.setOnClickListener {
                onClick.onClickOpenArticle(article = item)
            }

            actionBtn.setOnClickListener {
                onClick.onAddArticleClickAction(article = item)
            }
        }

        private fun formatDate(getData: String): String {
            val inputDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            inputDate.timeZone = TimeZone.getTimeZone("UTC")
            val date: Date = inputDate.parse(getData) ?: return "invalid date"
            val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getDefault()

            return outputFormat.format(date)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutArticleListItemBinding.inflate(inflater, parent, false)
        return ArticleItemViewHolder(isDelete, binding, onClickListener)
    }

    override fun onBindViewHolder(holder: ArticleItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ArticleItemDiffUtil : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}