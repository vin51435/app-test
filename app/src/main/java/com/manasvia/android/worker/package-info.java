/**
 * Background Automation and Scheduled Workers Layer (Android WorkManager).
 *
 * <h2>Architectural Purpose:</h2>
 * Implements Workflow 3: Guaranteed, battery-conscious background scheduling that
 * respects Android 14/15 Doze modes and battery constraints. Executes the 6:00 AM daily
 * wallpaper generation and Glance widget cache pre-warming.
 */
package com.manasvia.android.worker;
