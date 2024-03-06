package ba.out.bring.odoo.mc1.core.utils

import android.content.Context
import com.google.gson.reflect.TypeToken
import ba.out.bring.odoo.mc1.getActiveOdooUser
import ba.out.bring.odoo.mc1.getCookies
import ba.out.bring.odoo.mc1.gson
import ba.out.bring.odoo.mc1.setCookies
import okhttp3.Cookie
import timber.log.Timber

class CookiePrefs(context: Context) : Prefs(CookiePrefs.TAG, context) {

    companion object {
        const val TAG = "CookiePrefs"
    }

    private val type = object : TypeToken<ArrayList<ClonedCookie>>() {}.type

    fun getCookies(): ArrayList<Cookie> {
        val activeUser = context.getActiveOdooUser()
        if (activeUser != null) {
            // val cookiesStr = getString(activeUser.androidName)
            val cookiesStr = context.getCookies(activeUser)
            if (cookiesStr.isNotEmpty()) {
                val clonedCookies: ArrayList<ClonedCookie> = gson.fromJson(cookiesStr, type)
                val cookies = arrayListOf<Cookie>()
                for (clonedCookie in clonedCookies) {
                    cookies += clonedCookie.toCookie()
                }
                Timber.i("getCookies() returned $cookies")
                return cookies
            }
        }
        return arrayListOf()
    }

    fun setCookies(cookies: ArrayList<Cookie>) {
        Timber.i("setCookies() called with $cookies")
        val clonedCookies = arrayListOf<ClonedCookie>()
        for (cookie in cookies) {
            clonedCookies += ClonedCookie.fromCookie(cookie)
        }
        val cookiesStr = gson.toJson(clonedCookies, type)
        val activeUser = context.getActiveOdooUser()
        if (activeUser != null) {
            context.setCookies(activeUser, cookiesStr)
            // putString(activeUser.androidName, cookiesStr)
        }
    }
}
