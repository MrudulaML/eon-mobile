package `in`.bitspilani.eon

import `in`.bitspilani.eon.utils.ModelPreferencesManager
import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber
import timber.log.Timber.DebugTree


class BitsEonApp: Application() {

    override fun onCreate() {
        super.onCreate()
        //Preference Handler
        ModelPreferencesManager.with(this)
        Stetho.initializeWithDefaults(this)
        //Timber setup
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

}
