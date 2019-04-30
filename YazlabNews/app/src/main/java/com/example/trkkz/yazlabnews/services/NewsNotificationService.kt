package com.example.trkkz.yazlabnews.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.trkkz.yazlabnews.R
import org.json.JSONObject
import java.util.*

class NewsNotificationService : Service() {
    companion object {
        private const val TAG = "NewsNotificationService"
        private const val INTERVAL = 3
    }

    private val handler = Handler()
    private lateinit var timer: Timer
    private lateinit var queue: RequestQueue
    private lateinit var url: String
    private var timestamp = System.currentTimeMillis()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        super.onStartCommand(intent, flags, startId)

        //set a new Timer
        timer = Timer()

        //initialize the TimerTask's job
        val timerTask = object : TimerTask() {
            override fun run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post {
                    Log.d(TAG, "sync")

                    val jsonBody = JSONObject()
                    jsonBody.put("time", timestamp)
                    val request = JsonObjectRequest(Request.Method.POST, url,
                            jsonBody,
                            responseListener,
                            errorListener)
                    request.retryPolicy = DefaultRetryPolicy(
                            1000,
                            0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

                    queue.add(request)
                }
            }
        }

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, INTERVAL * 1000L) //

        return START_STICKY
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate")

        queue = Volley.newRequestQueue(this)
        url = "${getString(R.string.url)}/api/news/check"

        createNotificationChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")

        //stop the timer, if it's not already null
        timer.cancel()
    }

    private val responseListener = Response.Listener<JSONObject> {
        val jsonArray = it.getJSONArray("news")

        for (i in 0 until jsonArray.length()) {
            val builder = NotificationCompat.Builder(applicationContext, TAG)
                    .setSmallIcon(R.drawable.ic_like)
                    .setContentTitle("Yeni Haber Eklendi!")
                    .setContentText(jsonArray.getJSONObject(i).getString("title"))
//                .setStyle(NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(applicationContext)) {
                // notificationId is a unique int for each notification that you must define
                notify(0, builder.build())
            }
        }

        timestamp = System.currentTimeMillis()
    }

    private val errorListener = Response.ErrorListener { it.printStackTrace() }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = TAG
            val descriptionText = TAG
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(TAG, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}