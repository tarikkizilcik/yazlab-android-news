package com.example.trkkz.yazlabnews.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.trkkz.yazlabnews.R
import com.example.trkkz.yazlabnews.data.News

class NewsAdapter(private val dataSet: List<News>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val listItem = inflater.inflate(R.layout.list_item_news, p0, false)

        return ViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val news = dataSet[p1]

        p0.bind(news)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val textViewAuthor: TextView = view.findViewById(R.id.textViewAuthor)
        private val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        private val textViewBody: TextView = view.findViewById(R.id.textViewBody)

        fun bind(news: News) {
            textViewAuthor.text = news.author
            textViewTitle.text = news.title
            textViewBody.text = news.body

            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (::onItemClickListener.isInitialized)
                onItemClickListener.onItemClick(adapterPosition, view)
        }
    }
}