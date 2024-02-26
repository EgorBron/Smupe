package net.blusutils.smupe

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import jonathanfinerty.once.Once
import net.blusutils.smupe.data.image_sources.ApiDefFileWrapper.loadAllApiDefs
import net.blusutils.smupe.data.proto_datastore.SettingsProtobufSerializer
import net.blusutils.smupe.data.proto_datastore.settingsDataStore
import net.blusutils.smupe.data.room.AppDatabase.Companion.initDb
import net.blusutils.smupe.ui.App
import net.blusutils.smupe.ui.SmupeTheme
import java.lang.Thread.UncaughtExceptionHandler

class MainActivity : AppCompatActivity(), UncaughtExceptionHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.initDb()

        this.loadAllApiDefs()

        Once.initialise(this)

//        if (!BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(this)
//        }

        setContent {
            val ds by this.settingsDataStore.data.collectAsState(
                SettingsProtobufSerializer.defaultValue
            )
            requestedOrientation =
                if (ds.orientationLocked)
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                else
                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

            SmupeTheme(
                when (ds.themePreference.number) {
                    0 -> isSystemInDarkTheme()
                    1 -> false
                    2 -> true
                    else -> isSystemInDarkTheme()
                },
                ds.dynamicColorEnabled
            ) { App() }
        }
    }
    override fun uncaughtException(t: Thread, e: Throwable) {
        Log.e(t.name, Log.getStackTraceString(e))
        val intent = Intent(this, ErrorActivity::class.java)
        val data = Bundle()
        data.putString("thread", t.name)
        data.putString("exception", Log.getStackTraceString(e))
        intent.putExtras(data)
        this.startActivity(intent)
        this.finish()
    }
}