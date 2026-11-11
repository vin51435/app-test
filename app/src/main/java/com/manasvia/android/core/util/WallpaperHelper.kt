package com.manasvia.android.core.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.WindowManager
import com.manasvia.android.domain.model.wallpaper.WallpaperTarget
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * System Wallpaper Applier: Integrates with Android OS [WallpaperManager]
 * to apply rendered bitmaps to Lock and Home screens with strict memory recycling.
 */
@Singleton
open class WallpaperHelper @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    private val wallpaperManager: WallpaperManager by lazy {
        WallpaperManager.getInstance(context)
    }

    /**
     * Resolves the integer bitwise flags for [WallpaperManager] based on [WallpaperTarget].
     */
    open fun resolveTargetFlags(target: WallpaperTarget): Int {
        return when (target) {
            WallpaperTarget.LOCK_SCREEN -> WallpaperManager.FLAG_LOCK
            WallpaperTarget.HOME_SCREEN -> WallpaperManager.FLAG_SYSTEM
            WallpaperTarget.BOTH -> WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM
        }
    }

    /**
     * Applies the composite [Bitmap] to the chosen [target] screen.
     * Enforces strict immediate memory reclamation via [Bitmap.recycle] in a finally block
     * to prevent OutOfMemory (OOM) leaks.
     */
    open fun applyWallpaper(bitmap: Bitmap, target: WallpaperTarget): Result<Unit> {
        return try {
            if (!isSetWallpaperAllowed()) {
                val errorMsg = "System prohibits wallpaper changes (device policy or guest mode restricted)."
                Log.w(TAG, errorMsg)
                return Result.failure(SecurityException(errorMsg))
            }

            val flags = resolveTargetFlags(target)
            wallpaperManager.setBitmap(bitmap, null, true, flags)
            Log.i(TAG, "Successfully applied wallpaper to $target with flags: $flags")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to apply wallpaper to $target", e)
            Result.failure(e)
        } finally {
            if (!bitmap.isRecycled) {
                bitmap.recycle()
                Log.d(TAG, "Wallpaper bitmap memory recycled successfully.")
            }
        }
    }

    /**
     * Resets or clears the wallpaper for the specified [target] screen back to system defaults.
     */
    fun clearWallpaper(target: WallpaperTarget): Result<Unit> {
        return try {
            val flags = resolveTargetFlags(target)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.clear(flags)
            } else {
                wallpaperManager.clear()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to clear wallpaper for $target", e)
            Result.failure(e)
        }
    }

    /**
     * Checks whether the current system environment permits setting wallpapers.
     */
    fun isSetWallpaperAllowed(): Boolean {
        return try {
            wallpaperManager.isSetWallpaperAllowed && wallpaperManager.isWallpaperSupported
        } catch (e: Exception) {
            Log.w(TAG, "Failed to query wallpaper capabilities; assuming true fallback.", e)
            true
        }
    }

    /**
     * Resolves the active hardware screen dimensions (width x height) in physical pixels.
     * Guarantees 1:1 crisp rendering without blur or stretching.
     */
    fun getScreenDimensions(): Pair<Int, Int> {
        return try {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && windowManager != null) {
                val metrics = windowManager.currentWindowMetrics
                val bounds = metrics.bounds
                Pair(bounds.width(), bounds.height())
            } else {
                @Suppress("DEPRECATION")
                val displayMetrics = context.resources.displayMetrics
                Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Unable to resolve device display metrics, falling back to default 1080x2400", e)
            Pair(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT)
        }
    }

    companion object {
        private const val TAG = "WallpaperHelper"
        const val DEFAULT_SCREEN_WIDTH = 1080
        const val DEFAULT_SCREEN_HEIGHT = 2400
    }
}
