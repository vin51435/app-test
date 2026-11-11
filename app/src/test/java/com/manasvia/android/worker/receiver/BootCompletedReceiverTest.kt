package com.manasvia.android.worker.receiver

import android.content.ContextWrapper
import android.content.Intent
import com.manasvia.android.worker.WallpaperWorkScheduler
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BootCompletedReceiverTest {

    private lateinit var receiver: BootCompletedReceiver
    private lateinit var fakeScheduler: FakeWallpaperWorkScheduler

    @Before
    fun setUp() {
        fakeScheduler = FakeWallpaperWorkScheduler()
        receiver = BootCompletedReceiver(customScheduler = fakeScheduler)
    }

    @Test
    fun onReceive_bootCompleted_triggersScheduleDailyMorningWorker() {
        val intent = object : Intent() {
            override fun getAction(): String = ACTION_BOOT_COMPLETED
        }

        receiver.onReceive(ContextWrapper(null), intent)

        assertTrue("Expected scheduleDailyMorningWorker to be called", fakeScheduler.scheduledCalled)
    }

    @Test
    fun onReceive_packageReplaced_triggersScheduleDailyMorningWorker() {
        val intent = object : Intent() {
            override fun getAction(): String = ACTION_MY_PACKAGE_REPLACED
        }

        receiver.onReceive(ContextWrapper(null), intent)

        assertTrue("Expected scheduleDailyMorningWorker to be called", fakeScheduler.scheduledCalled)
    }

    @Test
    fun onReceive_unrelatedAction_doesNotTriggerSchedule() {
        val intent = object : Intent() {
            override fun getAction(): String = ACTION_AIRPLANE_MODE_CHANGED
        }

        receiver.onReceive(ContextWrapper(null), intent)

        assertFalse("Scheduler should not be called for unrelated intents", fakeScheduler.scheduledCalled)
    }

    @Test
    fun onReceive_nullIntent_doesNotThrow() {
        receiver.onReceive(ContextWrapper(null), null)
        assertFalse(fakeScheduler.scheduledCalled)
    }

    private class FakeWallpaperWorkScheduler : WallpaperWorkScheduler(ContextWrapper(null)) {
        var scheduledCalled = false

        override fun scheduleDailyMorningWorker(targetHour: Int) {
            scheduledCalled = true
        }
    }
}
