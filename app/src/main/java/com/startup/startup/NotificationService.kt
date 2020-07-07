package com.startup.startup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.startup.startup.network.Database
import com.startup.startup.ui.main.MainActivity
import com.startup.startup.ui.splash.SplashActivity
import com.startup.startup.util.Constants.USER_TYPE
import javax.inject.Inject


class NotificationService: FirebaseMessagingService() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if(remoteMessage.notification != null){
            createNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }

    override fun onNewToken(token: String) {
        if(FirebaseAuth.getInstance().currentUser != null){
            uploadToken(token)
        }
        val sp = getSharedPreferences("notification", Context.MODE_PRIVATE)
        sp.edit().putString("token", token).apply()
    }

    private fun uploadToken(token: String){
        Database().uploadToken(token)
    }

    private fun createNotification(title: String, message: String){
        val i = Intent(this, SplashActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT)

        val channelId = "CHANNEL_ID"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel title", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}