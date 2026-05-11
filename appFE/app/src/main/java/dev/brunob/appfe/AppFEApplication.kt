package dev.brunob.appfe

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.getSystemService
import dev.brunob.appfe.data.AppRepository
import dev.brunob.appfe.notifications.WeeklyReminderScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppFEApplication : Application() {

    val repository: AppRepository by lazy { AppRepository.get(this) }
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        appScope.launch { repository.init() }
        WeeklyReminderScheduler.schedule(this)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Recordatorios semanales",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Recuerda gestionar los días de la semana"
            }
            getSystemService<NotificationManager>()?.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "appfe_weekly_reminder"
    }
}
