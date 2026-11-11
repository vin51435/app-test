package com.manasvia.android.worker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.manasvia.android.worker.WallpaperWorkScheduler

/**
 * BroadcastReceiver triggered on phone reboot or application package update.
 * Asserts that the 6:00 AM daily morning wallpaper update is maintained in WorkManager.
 *
 * Designed with a secondary constructor for clean, zero-mock JVM testability without
 * forcing heavy Hilt dependency tree initialization during device boot.
 */
class BootCompletedReceiver(
    private val customScheduler: WallpaperWorkScheduler? = null
) : BroadcastReceiver() {

    // Default 0-argument constructor required by Android OS
    constructor() : this(null)

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        if (action == Intent.ACTION_BOOT_COMPLETED || action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            Log.i(TAG, "Received broadcast action '$action'. Asserting daily wallpaper schedule.")
            val scheduler = customScheduler ?: WallpaperWorkScheduler(context.applicationContext ?: context)
            scheduler.scheduleDailyMorningWorker()
        }
    }

    companion object {
        private const val TAG = "BootCompletedReceiver"
    }
}
