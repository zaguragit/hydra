package posidon.icons.hydra.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.*
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import java.lang.ref.WeakReference
import kotlin.math.*

object Tools {

	inline fun animate(d: Drawable): Drawable {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && d is Animatable2 -> {
                d.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable) = d.start()
                })
                d.start()
            }
            d is Animatable2Compat -> {
                d.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable) = d.start()
                })
                d.start()
            }
            d is AnimationDrawable -> d.start()
        }
        return d
    }

	inline fun clearAnimation(d: Drawable?) { when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && d is Animatable2 -> d.clearAnimationCallbacks()
        d is Animatable2Compat -> d.clearAnimationCallbacks()
        d is Animatable -> d.stop()
    }}

	inline fun isInstalled(packageName: String, packageManager: PackageManager): Boolean {
        var found = true
        try { packageManager.getPackageInfo(packageName, 0) }
        catch (e: Exception) { found = false }
        return found
    }

    var publicContextReference = WeakReference<Context>(null)
    inline val publicContext get() = publicContextReference.get()

	var navbarHeight = 0

	fun updateNavbarHeight(activity: Activity) {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight = metrics.heightPixels
        activity.windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight = metrics.heightPixels
        navbarHeight = if (realHeight > usableHeight) realHeight - usableHeight else 0
        val resources = activity.resources
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        navbarHeight = min(if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0, navbarHeight)
    }

    inline val isDefaultLauncher: Boolean get() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        return publicContext!!.packageManager.resolveActivity(intent, 0)?.resolvePackageName == "posidon.launcher"
    }

    inline fun springInterpolate(x: Float) = 1 + (2f.pow(-10f * x) * sin(2 * PI * (x - 0.01f)) / 0.4).toFloat()

    fun searchOptimize(s: String) = s.toLowerCase()
            .replace('ñ', 'n')
            .replace('e', '3')
            .replace('a', '4')
            .replace('i', '1')
            .replace('¿', '?')
            .replace('¡', '!')
            .replace(Regex("(k|cc|ck)"), "c")
            .replace(Regex("(z|ts|sc|cs|tz)"), "s")
            .replace(Regex("(gh|wh)"), "h")
            .replace(Regex("[-'&/_,.:;*\"]"), "")
}

inline fun Drawable.copy() = constantState?.newDrawable()?.mutate()

inline val Number.dp get() = Tools.publicContext!!.resources.displayMetrics.density * toFloat()
inline val Number.sp get() = Tools.publicContext!!.resources.displayMetrics.density * toFloat()

inline fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
}

inline fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}