package ba.out.bring.odoo.mc1

import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDexApplication
import ba.out.bring.odoo.mc1.core.Odoo
import ba.out.bring.odoo.mc1.core.OdooDatabase
import ba.out.bring.odoo.mc1.core.utils.CookiePrefs
import ba.out.bring.odoo.mc1.core.utils.LetterTileProvider
import ba.out.bring.odoo.mc1.core.utils.LocaleHelper
import ba.out.bring.odoo.mc1.core.utils.Retrofit2Helper
import timber.log.Timber

class App : MultiDexApplication() {

    companion object {
        var KEY_ACCOUNT_TYPE = "${BuildConfig.APPLICATION_ID}.auth"
        //var KEY_ACCOUNT_TYPE = "ba.out.bring.odoo.mc1.auth"

    }

    private val letterTileProvider: LetterTileProvider by lazy {
        LetterTileProvider(this)
    }

    val cookiePrefs: CookiePrefs by lazy {
        CookiePrefs(this)
    }

    override fun attachBaseContext(base: Context?) {
        if (base != null) {
            super.attachBaseContext(LocaleHelper.setLocale(base))
        } else {
            super.attachBaseContext(base)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        LocaleHelper.setLocale(this)
    }

    override fun onCreate() {
        super.onCreate()
        Retrofit2Helper.app = this
        Odoo.app = this
        OdooDatabase.app = this

        //if (BuildConfig.DEBUG) {
        //    Timber.plant(Timber.DebugTree())
        //}
    }

    fun getLetterTile(displayName: String): ByteArray =
        letterTileProvider.getLetterTile(displayName)
}