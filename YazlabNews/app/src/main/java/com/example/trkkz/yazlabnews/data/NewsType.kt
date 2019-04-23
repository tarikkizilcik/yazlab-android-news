package com.example.trkkz.yazlabnews.data

enum class NewsType {
    BREAKING,
    SPORT,
    ECONOMY,
    HEALTH,
    FASHION,
    EDUCATION;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}