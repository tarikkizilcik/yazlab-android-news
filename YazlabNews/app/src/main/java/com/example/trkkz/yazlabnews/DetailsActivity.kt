package com.example.trkkz.yazlabnews

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.trkkz.yazlabnews.data.News
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_details.*
import org.json.JSONArray
import org.json.JSONObject
import java.sql.Timestamp
import java.util.*


class DetailsActivity : AppCompatActivity() {
    private var isAlreadyLiked = false
    private var isAlreadyDisliked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        postViewed()

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

        val bmp = BitmapFactory.decodeByteArray(image.toByteArray(), 0, image.size)
        imageView.setImageBitmap(bmp)

        textViewPublicationDate.text = Date(Timestamp(publicationDate).time).toString()

        buttonBack.setOnClickListener { finish() }

        textViewNumViews.text = getString(R.string.str_colon_num, getString(R.string.num_views), 0)

        buttonLike.setOnClickListener(onClickLikeButton)
        buttonDislike.setOnClickListener(onClickDisLikeButton)
    }

    private val onClickLikeButton = View.OnClickListener {
        textViewNumLikes.text = getString(R.string.str_colon_num, getString(R.string.num_likes), 0)
    }

    private val onClickDisLikeButton = View.OnClickListener {
        textViewNumDislikes.text = getString(R.string.str_colon_num, getString(R.string.num_dislikes), 0)

    }

    private val responseListenerViewed = Response.Listener<String> {
        val gson = Gson()

        val jsonArray = JSONArray(it)
        for (jsonObject in jsonArray) {
            if (jsonObject !is JSONObject) continue

            val news: News = gson.fromJson(jsonObject.toString(), News::class.java)
        }
    }

    private val errorListenerViewed = Response.ErrorListener {

    }

    private fun postViewed() {
        val queue = Volley.newRequestQueue(this)
        val url = "${getString(R.string.url)}/api/news"
        val stringRequest = StringRequest(Request.Method.GET, url,
                responseListenerViewed,
                errorListenerViewed)

        queue.add(stringRequest)
    }
}


