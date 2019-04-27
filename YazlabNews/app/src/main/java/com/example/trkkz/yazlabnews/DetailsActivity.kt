package com.example.trkkz.yazlabnews

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.trkkz.yazlabnews.data.News
import kotlinx.android.synthetic.main.activity_details.*
import java.sql.Timestamp
import java.util.*


class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val author = intent.getStringExtra(News.EXTRA_AUTHOR)
        val title = intent.getStringExtra(News.EXTRA_TITLE)
        val body = intent.getStringExtra(News.EXTRA_BODY)
        val type = intent.getStringExtra(News.EXTRA_TYPE)
        val image = intent.getSerializableExtra(News.EXTRA_IMAGE) as? Array<Byte> ?: return
        val createdAt = intent.getLongExtra(News.EXTRA_CREATED_AT, -1)
        val updatedAt = intent.getLongExtra(News.EXTRA_UPDATED_AT, -1)

        textViewAuthor.text = author
        textViewTitle.text = title
        textViewBody.text = body
        textViewType.text = type

        val bmp = BitmapFactory.decodeByteArray(image.toByteArray(), 0, image.size)
        imageView.setImageBitmap(bmp)

        textViewCreatedAt.text = Date(Timestamp(createdAt).time).toString()

        buttonBack.setOnClickListener { finish() }
    }
}
