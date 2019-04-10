package com.example.trkkz.yazlabnews

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.trkkz.yazlabnews.adapters.NewsAdapter
import com.example.trkkz.yazlabnews.data.News
import kotlinx.android.synthetic.main.activity_news.*
import org.json.JSONArray
import org.json.JSONObject

class NewsActivity : AppCompatActivity() {
    private val newsList = mutableListOf<News>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

//        val newsAdapter = NewsAdapter(newsList)
//        recyclerViewNews.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(this@NewsActivity)
//            adapter = newsAdapter
//        }

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "http:/10.0.3.2:3000/api/news"

// Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    // Display the first 500 characters of the response string.
                    val jsonArray = JSONArray(response)
                    for (i: Int in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray[i] as JSONObject
                        val author = jsonObject.getString("author")
                        val title = jsonObject.getString("title")
                        val body = jsonObject.getString("body")

                        newsList.add(News(author, title, body))
                    }

                    val newsAdapter = NewsAdapter(newsList)
                    recyclerViewNews.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@NewsActivity)
                        adapter = newsAdapter
                    }
                },
                Response.ErrorListener { Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show() })

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}
