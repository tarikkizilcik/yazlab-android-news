package com.example.trkkz.yazlabnews.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.trkkz.yazlabnews.R
import com.example.trkkz.yazlabnews.data.News

class NewsAdapter(private val dataSet: List<News>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
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

        p0.apply {
            textViewAuthor.text = news.author
            textViewTitle.text = news.title
            textViewBody.text = news.body
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewAuthor: TextView = view.findViewById(R.id.textViewAuthor)
        val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        val textViewBody: TextView = view.findViewById(R.id.textViewBody)
    }
}