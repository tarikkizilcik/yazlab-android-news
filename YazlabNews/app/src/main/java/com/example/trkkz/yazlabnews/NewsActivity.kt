package com.example.trkkz.yazlabnews

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.trkkz.yazlabnews.adapters.NewsAdapter
import com.example.trkkz.yazlabnews.adapters.OnCheckBoxClickListener
import com.example.trkkz.yazlabnews.adapters.OnItemClickListener
import com.example.trkkz.yazlabnews.adapters.TypesAdapter
import com.example.trkkz.yazlabnews.data.News
import com.example.trkkz.yazlabnews.services.NewsNotificationService
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_news.*
import org.json.JSONArray
import org.json.JSONObject

class NewsActivity : AppCompatActivity(), OnItemClickListener, OnCheckBoxClickListener {
    private val newsList = mutableListOf<News>()
    private val selectedNews = mutableListOf<News>()
    private val newsTypes = mutableListOf<String>()
    private val selectedTypes = newsTypes.toMutableList()
    private val newsAdapter = NewsAdapter(selectedNews)
    private val typesAdapter = TypesAdapter(newsTypes)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        newsAdapter.setOnItemClickListener(this)
        typesAdapter.setOnItemClickListener(this)

        recyclerViewNews.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(this@NewsActivity)
            adapter = newsAdapter
        }

        listViewTypes.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@NewsActivity)
            adapter = typesAdapter
        }

        val queue = Volley.newRequestQueue(this)
        val newsUrl = "${getString(R.string.url)}/api/news"
        val newsRequest = StringRequest(Request.Method.GET, newsUrl,
                responseListenerGetNews,
                errorListenerGetNews)
        val newsTypesUrl = "${getString(R.string.url)}/api/news-types"
        val newsTypesRequest = StringRequest(Request.Method.GET, newsTypesUrl,
                responseListenerGetNewsTypes,
                errorListenerGetNewsTypes)

        queue.apply {
            add(newsRequest)
            add(newsTypesRequest)
        }
    }

    private val responseListenerGetNews = Response.Listener<String> {
        // Add all messages to the list in the response
        val gson = Gson()

        val jsonArray = JSONArray(it)
        for (jsonObject in jsonArray) {
            if (jsonObject !is JSONObject) continue

            val news: News = gson.fromJson(jsonObject.toString(), News::class.java)
            selectedNews.add(news)
            newsList.add(news)
        }

        this@NewsActivity.runOnUiThread {
            newsAdapter.notifyDataSetChanged()
        }

        startService(Intent(this, NewsNotificationService::class.java))
    }

    private val errorListenerGetNews = Response.ErrorListener {
        it.printStackTrace()
        Toast.makeText(this@NewsActivity, R.string.server_isnt_responding, Toast.LENGTH_SHORT).show()
    }

    private val responseListenerGetNewsTypes = Response.Listener<String> {
        val jsonArray = JSONArray(it)
        for (jsonObject in jsonArray) {
            if (jsonObject !is JSONObject) continue

            val newsType = jsonObject.getString("name")
            newsTypes.add(newsType)
            selectedTypes.add(newsType)
        }

        this@NewsActivity.runOnUiThread {
            typesAdapter.notifyDataSetChanged()
        }
    }

    private val errorListenerGetNewsTypes = Response.ErrorListener {
        it.printStackTrace()
        Toast.makeText(this@NewsActivity, R.string.server_isnt_responding, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(position: Int, view: View) {
        val intent = Intent(this@NewsActivity, DetailsActivity::class.java)

        val news = selectedNews[position]
        intent.apply {
            putExtra(News.EXTRA_ID, news._id)
            putExtra(News.EXTRA_AUTHOR, news.author)
            putExtra(News.EXTRA_TITLE, news.title)
            putExtra(News.EXTRA_BODY, news.body)
            putExtra(News.EXTRA_TYPE, news.type)
            putExtra(News.EXTRA_IMAGE, news.image)
            putExtra(News.EXTRA_PUBLICATION_DATE, news.publicationDate)
            putExtra(News.EXTRA_CREATED_AT, news.createdAt)
            putExtra(News.EXTRA_UPDATED_AT, news.updatedAt)
        }

        startActivity(intent)
    }

    override fun onCheckBoxClick(position: Int, view: View) {
        val type = newsTypes[position]

        if (selectedTypes.contains(type))
            selectedTypes.remove(type)
        else
            selectedTypes.add(type)

        selectedNews.clear()
        newsList.forEach {
            if (selectedTypes.contains(it.type))
                selectedNews.add(it)
        }

        this@NewsActivity.runOnUiThread {
            newsAdapter.notifyDataSetChanged()
        }
    }
}

