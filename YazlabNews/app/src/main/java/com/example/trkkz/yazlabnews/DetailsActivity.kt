package com.example.trkkz.yazlabnews

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.trkkz.yazlabnews.data.MyPreference
import com.example.trkkz.yazlabnews.data.News
import kotlinx.android.synthetic.main.activity_details.*
import java.sql.Timestamp
import java.util.*


class DetailsActivity : AppCompatActivity() {
    private var counterLike = 0
    private var counterDisLike = 0
    private var loginCount = 0
    private lateinit var myPreference: MyPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        myPreference = MyPreference(this)

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

        loginCount = myPreference.getLoginCount()
        loginCount++
        myPreference.setLoginCount(loginCount)
        textViewNumViews.text = getString(R.string.str_colon_num, getString(R.string.num_views), loginCount)

        buttonLike.setOnClickListener(onClickLikeButton)
        buttonDislike.setOnClickListener(onClickDisLikeButton)
        buttonReset.setOnClickListener(onClickResetButton)
    }

    private val onClickLikeButton = View.OnClickListener {
        counterLike++

        textViewNumLikes.text = getString(R.string.str_colon_num, getString(R.string.num_likes), counterLike)
    }

    private val onClickDisLikeButton = View.OnClickListener {
        counterDisLike++
        textViewNumDislikes.text = getString(R.string.str_colon_num, getString(R.string.num_dislikes), counterDisLike)

    }

    private val onClickResetButton = View.OnClickListener {
        loginCount = 0
        myPreference.setLoginCount(0)
        textViewNumViews.text = getString(R.string.str_colon_num, getString(R.string.num_views), loginCount)
        counterLike = 0
        textViewNumLikes.text = getString(R.string.str_colon_num, getString(R.string.num_likes), counterLike)
        counterDisLike = 0
        textViewNumDislikes.text = getString(R.string.str_colon_num, getString(R.string.num_dislikes), counterDisLike)
    }
}


