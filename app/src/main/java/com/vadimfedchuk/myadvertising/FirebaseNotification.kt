package com.vadimfedchuk.myadvertising

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context;
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vadimfedchuk.myadvertising.utils.ShPreferences

const val CHANNEL_ID = "AdvertisingChannel"
class FirebaseNotification : FirebaseMessagingService() {

    private var mNotificationManager:NotificationManager? = null

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        ShPreferences.setTokenFirebase(s, this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
        val notificationBuilder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(remoteMessage.notification?.title)
                .setContentText(remoteMessage.notification?.body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
        mNotificationManager?.notify(0, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            mNotificationManager?.getNotificationChannel(CHANNEL_ID) == null
        ) {
            val name = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.setSound(null, null)
            mChannel.enableVibration(false)
            mNotificationManager?.createNotificationChannel(mChannel)
        }
    }
}