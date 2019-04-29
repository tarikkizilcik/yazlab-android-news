package com.example.trkkz.yazlabnews

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.trkkz.yazlabnews.data.News
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_details.*
import org.json.JSONObject
import java.sql.Timestamp
import java.util.*

class DetailsActivity : AppCompatActivity() {
    companion object {
        const val TAG = "DetailsActivity"
    }

    private var isAlreadyLiked = false
    private var isAlreadyDisliked = false
    private lateinit var queue: RequestQueue
    private lateinit var news: News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        queue = Volley.newRequestQueue(this)

        val id = intent.getStringExtra(News.EXTRA_ID)
        val author = intent.getStringExtra(News.EXTRA_AUTHOR)
        val title = intent.getStringExtra(News.EXTRA_TITLE)
        val body = intent.getStringExtra(News.EXTRA_BODY)
        val type = intent.getStringExtra(News.EXTRA_TYPE)
        @Suppress("UNCHECKED_CAST")
        val image = intent.getSerializableExtra(News.EXTRA_IMAGE) as? Array<Byte> ?: return
        val publicationDate = intent.getLongExtra(News.EXTRA_PUBLICATION_DATE, -1)
        // TODO Show createdAt
        @Suppress("UNUSED_VARIABLE")
        val createdAt = intent.getLongExtra(News.EXTRA_CREATED_AT, -1)
        // TODO Show updatedAt
        @Suppress("UNUSED_VARIABLE")
        val updatedAt = intent.getLongExtra(News.EXTRA_UPDATED_AT, -1)

        textViewAuthor.text = author
        textViewTitle.text = title
        textViewBody.text = body
        textViewType.text = type

        news = News(author, title, body, type, image, publicationDate, createdAt, updatedAt, id)

        val bmp = BitmapFactory.decodeByteArray(image.toByteArray(), 0, image.size)
        imageView.setImageBitmap(bmp)

        textViewPublicationDate.text = Date(Timestamp(publicationDate).time).toString()

        buttonBack.setOnClickListener { finish() }

        textViewNumViews.text = getString(R.string.str_colon_num, getString(R.string.num_views), 0)

        buttonLike.setOnClickListener(onClickLikeButton)
        buttonDislike.setOnClickListener(onClickDisLikeButton)

        postViewed()
    }

    private val responseListener = Response.Listener<JSONObject> {
        val gson = Gson()

        val news: News = gson.fromJson(it.getJSONObject("news").toString(), News::class.java)

        textViewNumLikes.text = getString(R.string.str_colon_num, getString(R.string.num_likes), news.likes)
        textViewNumDislikes.text = getString(R.string.str_colon_num, getString(R.string.num_dislikes), news.dislikes)
        textViewNumViews.text = getString(R.string.str_colon_num, getString(R.string.num_views), news.views)
    }

    private val errorListener = Response.ErrorListener {
        it.printStackTrace()
        Log.e(TAG, it.toString())
    }

    private fun requestPutVal(url: String) {
        val gson = Gson()
        val jsonBody = JSONObject()
        jsonBody.put("news", JSONObject(gson.toJson(news)))

        val request = JsonObjectRequest(Request.Method.PUT, url, jsonBody, responseListener, errorListener)

        queue.add(request)
    }

    private val onClickLikeButton = View.OnClickListener {
        requestPutVal("${getString(R.string.url)}/api/news/${news._id}/like")
    }

    private val onClickDisLikeButton = View.OnClickListener {
        requestPutVal("${getString(R.string.url)}/api/news/${news._id}/dislike")
    }

    private fun postViewed() {
        requestPutVal("${getString(R.string.url)}/api/news/${news._id}/view")
    }
}


