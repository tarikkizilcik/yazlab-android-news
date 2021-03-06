package com.example.trkkz.yazlabnews.data

class News(val author: String,
           val title: String,
           val body: String,
           val type: String,
           val image: Array<Byte>,
           val publicationDate: Long,
           val createdAt: Long,
           val updatedAt: Long,
           val _id: String,
           val likes: Int = 0,
           val dislikes: Int = 0,
           val views: Int = 0) {
    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_AUTHOR = "EXTRA_AUTHOR"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_BODY = "EXTRA_BODY"
        const val EXTRA_TYPE = "EXTRA_TYPE"
        const val EXTRA_IMAGE = "EXTRA_IMAGE"
        const val EXTRA_PUBLICATION_DATE = "EXTRA_PUBLICATION_DATE"
        const val EXTRA_CREATED_AT = "EXTRA_CREATED_AT"
        const val EXTRA_UPDATED_AT = "EXTRA_UPDATED_AT"
    }
}
