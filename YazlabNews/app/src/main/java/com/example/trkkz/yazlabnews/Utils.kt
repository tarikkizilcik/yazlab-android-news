package com.example.trkkz.yazlabnews

import org.json.JSONArray

operator fun JSONArray.iterator(): Iterator<Any> = (0 until length()).asSequence().map { get(it) }.iterator()
