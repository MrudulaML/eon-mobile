package `in`.bitspilani.eon

import `in`.bitspilani.eon.utils.ModelPreferencesManager
import `in`.bitspilani.eon.utils.PrefHandler
import android.app.Application
import com.facebook.stetho.Stetho

class BitsEonApp: Application() {

    companion object{
        var localStorageHandler: PrefHandler? = null
    }

    override fun onCreate() {
        super.onCreate()
        localStorageHandler = PrefHandler(this)
        ModelPreferencesManager.with(this)
        Stetho.initializeWithDefaults(this)
    }

}
