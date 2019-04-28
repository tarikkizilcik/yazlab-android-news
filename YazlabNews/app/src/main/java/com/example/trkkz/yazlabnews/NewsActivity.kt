package com.example.trkkz.yazlabnews

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.trkkz.yazlabnews.adapters.NewsAdapter
import com.example.trkkz.yazlabnews.adapters.OnItemClickListener
import com.example.trkkz.yazlabnews.data.News
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_news.*
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "NewsActivity"

class NewsActivity : AppCompatActivity(), OnItemClickListener {
    private val newsList = mutableListOf<News>()
    private val newsAdapter = NewsAdapter(newsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        newsAdapter.setOnItemClickListener(this)

        recyclerViewNews.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@NewsActivity)
            adapter = newsAdapter
        }

        val queue = Volley.newRequestQueue(this)
        val url = "${getString(R.string.url)}/api/news"

        val stringRequest = StringRequest(Request.Method.GET, url,
                responseListenerGetNews,
                errorListenerGetNews)

        queue.add(stringRequest)
    }

    private val responseListenerGetNews = Response.Listener<String> {
        // Add all messages to the list in the response
        val gson = Gson()

        val jsonArray = JSONArray(it)
        for (jsonObject in jsonArray) {
            if (jsonObject !is JSONObject) continue

            val news: News = gson.fromJson(jsonObject.toString(), News::class.java)
            newsList.add(news)
        }

        this@NewsActivity.runOnUiThread {
            newsAdapter.notifyDataSetChanged()
        }
    }

    private val errorListenerGetNews = Response.ErrorListener {
        it.printStackTrace()
        Log.e(TAG, it.localizedMessage)
    }

    override fun onItemClick(position: Int, view: View) {
        val intent = Intent(this@NewsActivity, DetailsActivity::class.java)

        intent.putExtra(News.EXTRA_AUTHOR, newsList[position].author)
        intent.putExtra(News.EXTRA_TITLE, newsList[position].title)
        intent.putExtra(News.EXTRA_BODY, newsList[position].body)
        intent.putExtra(News.EXTRA_TYPE, newsList[position].type)
        intent.putExtra(News.EXTRA_IMAGE, newsList[position].image)
        intent.putExtra(News.EXTRA_PUBLICATION_DATE, newsList[position].publicationDate)
        intent.putExtra(News.EXTRA_CREATED_AT, newsList[position].createdAt)
        intent.putExtra(News.EXTRA_UPDATED_AT, newsList[position].updatedAt)

        startActivity(intent)
    }
}

