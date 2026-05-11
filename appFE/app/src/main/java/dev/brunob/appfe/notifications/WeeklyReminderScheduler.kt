package dev.brunob.appfe.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import java.util.concurrent.TimeUnit

object WeeklyReminderScheduler {
    private const val WORK_NAME = "weekly_reminder"

    fun schedule(context: Context) {
        val now = LocalDateTime.now()
        // Próximo viernes a las 18:00.
        var next = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY))
            .withHour(18).withMinute(0).withSecond(0).withNano(0)
        if (!next.isAfter(now)) {
            next = next.with(TemporalAdjusters.next(DayOfWeek.FRIDAY))
                .withHour(18).withMinute(0).withSecond(0).withNano(0)
        }
        val delayMin = java.time.Duration.between(now, next).toMinutes().coerceAtLeast(1)

        val request = PeriodicWorkRequestBuilder<WeeklyReminderWorker>(7, TimeUnit.DAYS)
            .setInitialDelay(delayMin, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}
