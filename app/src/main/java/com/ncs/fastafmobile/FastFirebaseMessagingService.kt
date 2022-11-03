package com.ncs.fastafmobile

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

const val channelID = "auth_channel"
const val channelName = "com.ncs.fastafmobile"

class FastFirebaseMessagingService : FirebaseMessagingService(){


    val db = Firebase.firestore



    companion object {
        private const val TAG = "MyFirebaseMsgService"
        private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    }
    override fun onNewToken(token: String) {
        Timber.tag(TAG).d("Refreshed token: %s", token)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        saveTokenOnUserDB(token)
    }

    private fun saveTokenOnUserDB(token: String) {
                val tokenHash = hashMapOf(
                    "fcmToken" to token
                )
        Timber.tag(TAG).d("Token: %s", token)

//            db.collection("users").document()
//                .set(tokenHash)
//                .addOnSuccessListener {
//                    Timber.tag(TAG).d("Token added!")
//                }
//                .addOnFailureListener { e ->
//                    Timber.tag(TAG).e(e, "Error writing document")
//                }
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Timber.tag(TAG).d("From: %s", remoteMessage.from)


        if (remoteMessage.notification != null) {
            generateNotification(
                remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!
            )
        }
    }


    fun getRemoteView(title: String,body:String) : RemoteViews {
        val remoteView = RemoteViews("com.ncs.fastafmobile",R.layout.auth_notification)
        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.body,body)
        remoteView.setImageViewResource(R.id.logo,R.drawable.ic_baseline_fingerprint_24)
        return remoteView
    }


    fun generateNotification(title:String, message:String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        var pendingIntent: PendingIntent? = null
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }


        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            channelID)
            .setSmallIcon(R.drawable.ic_baseline_fingerprint_24)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setAutoCancel(false)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        builder = builder.setContent(getRemoteView(title, message))
        val notificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelID, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())

    }
}