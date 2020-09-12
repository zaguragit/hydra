package posidon.icons.hydra.tools

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import java.lang.ref.WeakReference
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin

object Tools {

	fun tryAnimate(d: Drawable): Drawable {
        when (d) {
            is Animatable2 -> {
                d.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable) = d.start()
                })
                d.start()
            }
            is Animatable2Compat -> {
                d.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable) = d.start()
                })
                d.start()
            }
            is AnimationDrawable -> d.start()
            is MaskedDrawable -> tryAnimate(d.drawable)
        }
        return d
    }

	inline fun clearAnimation(d: Drawable?) {
        when (d) {
            is Animatable2 -> d.clearAnimationCallbacks()
            is Animatable2Compat -> d.clearAnimationCallbacks()
            is Animatable -> d.stop()
        }
    }

	inline fun isInstalled(packageName: String, packageManager: PackageManager) = try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: Exception) { false }

    var publicContextReference = WeakReference<Context>(null)
    inline val publicContext get() = publicContextReference.get()

    inline fun springInterpolate(x: Float) = 1 + (2f.pow(-10f * x) * sin(2 * PI * (x - 0.01f)) / 0.4).toFloat()

    fun searchOptimize(s: String) = s.toLowerCase()
            .replace('ñ', 'n')
            .replace('e', '3')
            .replace('a', '4')
            .replace('i', '1')
            .replace('¿', '?')
            .replace('¡', '!')
            .replace("wh", "w")
            .replace(Regex("(k|cc|ck)"), "c")
            .replace(Regex("(z|ts|sc|cs|tz)"), "s")
            .replace(Regex("([-'&/_,.:;*\"]|gh)"), "")
}

inline fun Drawable.copy() = constantState?.newDrawable()?.mutate()

inline val Number.dp get() = Tools.publicContext!!.resources.displayMetrics.density * toFloat()

inline fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
}