package `in`.bitspilani.eon.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by dilip on 9/11/17.
 */

class PrefHandler(context: Context) {

    var pref: SharedPreferences

    var editor: SharedPreferences.Editor

    init {
        pref = context.getSharedPreferences(Constants.PREF_CONST, Context.MODE_PRIVATE)
        editor = pref.edit()
    }
    internal var PRIVATE_MODE = 0

    var name: String?
        get() = pref.getString("name", null)
        set(name) {
            editor.putString("name", name)
            editor.apply()
        }

    var id: String?
        get() = pref.getString("user_id", null)
        set(id) {
            editor.putString("user_id", id)
            editor.apply()
        }
    var user_email: String?
        get() = pref.getString("user_email", null)
        set(id) {
            editor.putString("user_email", id)
            editor.apply()
        }
    var user_id: String?
        get() = pref.getString("user_id",null)
        set(id) {
            editor.putString("user_id", id)
            editor.apply()
        }

    var token: String?
        get() = pref.getString("d_token", null)
        set(token) {
            editor.putString("d_token", token)
            editor.apply()
        }

    var user_role: String?
        get() = pref.getString("user_role", "")
        set(id) {
            editor.putString("user_role", id)
            editor.apply()
        }

    var fcmToken: String?
        get() = pref.getString("fcm_token", "")
        set(name) {
            editor.putString("fcm_token", name)
            editor.apply()
        }

    fun clearData() {
        editor.clear()
        editor.apply()
    }


}
