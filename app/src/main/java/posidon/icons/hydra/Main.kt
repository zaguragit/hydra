package posidon.icons.hydra

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import posidon.icons.hydra.tools.Tools
import java.lang.ref.WeakReference


class Main : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        Tools.publicContextReference = WeakReference(applicationContext)
    }

    fun applyPosidon(v: View) {
        if (Tools.isInstalled("posidon.launcher", packageManager)) {
            Intent(Intent.ACTION_MAIN).apply {
                component = ComponentName("posidon.launcher", "posidon.launcher.external.ApplyIcons")
                putExtra("iconpack", packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(this)
            }
        } else Toast.makeText(this, "Not installed", Toast.LENGTH_SHORT).show()
    }

    fun applyLawnchair(v: View) {
        try {
            Intent("ch.deletescape.lawnchair.APPLY_ICONS", null).apply {
                putExtra("packageName", packageName)
                startActivity(this)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Not installed", Toast.LENGTH_SHORT).show()
        }
    }

    fun applyNiagara(v: View) {
        try {
            Intent("bitpit.launcher.APPLY_ICONS").apply {
                `package` = "bitpit.launcher"
                putExtra("packageName", packageName)
                startActivity(this)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Not installed", Toast.LENGTH_SHORT).show()
        }
    }

    fun list(v: View) {
        startActivity(Intent(this, IconList::class.java))
    }

    fun github(v: View) {try {
        val uri = Uri.parse("https://github.com/leoxshn/hydra")
        val i = Intent(Intent.ACTION_VIEW, uri)
        startActivity(i)
    } catch (ignore: Exception) {} }
}