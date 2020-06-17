package posidon.icons.hydra

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
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
        if (isInstalled("posidon.launcher", packageManager)) {
            val i = Intent(Intent.ACTION_MAIN)
            i.component = ComponentName("posidon.launcher", "posidon.launcher.external.ApplyIcons")
            i.putExtra("iconpack", BuildConfig.APPLICATION_ID)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        } else Toast.makeText(this, "Not installed", Toast.LENGTH_SHORT).show()
    }

    private fun isInstalled(packageName: String, packageManager: PackageManager): Boolean {
        try { packageManager.getPackageInfo(packageName, 0) }
        catch (e: PackageManager.NameNotFoundException) { return false }
        return true
    }

    fun list(v: View) {
        startActivity(Intent(this, IconList::class.java))
    }

    fun github(v: View) {

    }
}