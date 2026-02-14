package com.example.domabusiness.services

import android.app.Notification
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class NotificationReaderService : NotificationListenerService(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var ttsReady = false
    private val notificationQueue = mutableListOf<String>()

    companion object {
        private const val TAG = "NotificationReader"
        var isServiceRunning = false
        var shouldReadNotifications = true
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Serviço de notificações iniciado")
        tts = TextToSpeech(this, this)
        isServiceRunning = true
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("pt", "BR"))
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.US)
            }
            ttsReady = true
            Log.i(TAG, "TTS do serviço pronto")
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (!shouldReadNotifications) return

        val notification = sbn.notification
        val extras = notification.extras

        val title = extras.getString(Notification.EXTRA_TITLE) ?: ""
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
        val appName = sbn.packageName

        // Filtra notificações do próprio app
        if (appName == packageName) return

        Log.d(TAG, "Nova notificação: $title - $text")

        // Monta a mensagem
        val message = buildNotificationMessage(title, text, appName)
        speakNotification(message)
    }

    private fun buildNotificationMessage(title: String, text: String, packageName: String): String {
        val appName = getAppName(packageName)
        return when {
            title.isNotEmpty() && text.isNotEmpty() ->
                "Nova notificação de $appName. $title. $text"
            title.isNotEmpty() ->
                "Nova notificação de $appName. $title"
            text.isNotEmpty() ->
                "Nova notificação. $text"
            else ->
                "Nova notificação de $appName"
        }
    }

    private fun getAppName(packageName: String): String {
        return try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            packageName
        }
    }

    private fun speakNotification(message: String) {
        if (ttsReady) {
            tts?.speak(message, TextToSpeech.QUEUE_ADD, null, System.currentTimeMillis().toString())
        } else {
            notificationQueue.add(message)
        }
    }

    fun readPendingNotifications() {
        val activeNotifications = activeNotifications
        if (activeNotifications.isEmpty()) {
            speakNotification("Não há notificações pendentes")
            return
        }

        speakNotification("Você tem ${activeNotifications.size} notificações")

        activeNotifications.take(5).forEach { sbn ->
            val notification = sbn.notification
            val extras = notification.extras
            val title = extras.getString(Notification.EXTRA_TITLE) ?: ""
            val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
            val appName = getAppName(sbn.packageName)

            if (title.isNotEmpty() || text.isNotEmpty()) {
                val message = buildNotificationMessage(title, text, sbn.packageName)
                speakNotification(message)
            }
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        isServiceRunning = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
}