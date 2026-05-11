package dev.brunob.appfe.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.brunob.appfe.AppFEApplication
import dev.brunob.appfe.MainActivity
import dev.brunob.appfe.R
import dev.brunob.appfe.data.AppRepository
import java.time.LocalDate

class WeeklyReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val repo = AppRepository.get(applicationContext)
        repo.init()
        if (repo.currentStudent.value == null) return Result.success()

        val pendientes = repo.pendientesSemanaActual(LocalDate.now())
        if (pendientes.isEmpty()) return Result.success()

        val texto = if (pendientes.size == 1) {
            "Tienes 1 día sin gestionar esta semana"
        } else {
            "Tienes ${pendientes.size} días sin gestionar esta semana"
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return Result.success()
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pi = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notif = NotificationCompat.Builder(applicationContext, AppFEApplication.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Completa tu semana")
            .setContentText(texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "$texto. Abre la app para marcar asistencia o ausencia en los días pendientes."
            ))
            .setAutoCancel(true)
            .setContentIntent(pi)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(NOTIF_ID, notif)
        return Result.success()
    }

    companion object {
        const val NOTIF_ID = 1001
    }
}
