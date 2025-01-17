package ba.out.bring.odoo.mc1.core.authenticator

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.core.app.ActivityCompat
import ba.out.bring.odoo.mc1.*
import ba.out.bring.odoo.mc1.core.Odoo
import ba.out.bring.odoo.mc1.core.OdooUser
import ba.out.bring.odoo.mc1.core.entities.session.authenticate.AuthenticateResult
import ba.out.bring.odoo.mc1.core.utils.BaseActivity
import ba.out.bring.odoo.mc1.core.utils.android.ktx.subscribeEx
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SplashActivity : BaseActivity() {

    private lateinit var app: ba.out.bring.odoo.mc1.App
    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null
            && intent.action == Intent.ACTION_MAIN
        ) {
            finish()
            return
        }

        app = application as ba.out.bring.odoo.mc1.App
        compositeDisposable?.dispose()
        compositeDisposable = CompositeDisposable()
    }

    override fun onPostResume() {
        super.onPostResume()
        checkUser()
    }

    private fun checkUser() {

        val user = getActiveOdooUser()
        if (user != null) {
            Odoo.user = user
            Odoo.check {
                onSubscribe { disposable ->
                    compositeDisposable?.add(disposable)
                }

                onNext { response ->
                    if (response.isSuccessful) {
                        val check = response.body()!!
                        if (check.isSuccessful) {
                            app.cookiePrefs.setCookies(Odoo.pendingAuthenticateCookies)
                            startMainActivity()
                        } else {
                            val odooError = check.odooError
                            showMessage(
                                title = getString(R.string.server_request_error, response.code(), response.message()),
                                message = check.errorMessage,
                                positiveButton = getString(R.string.login_again),
                                positiveButtonListener = DialogInterface.OnClickListener { _, _ ->
                                    authenticate(user)
                                },
                                showNegativeButton = true,
                                negativeButton = getString(R.string.report_feedback),
                                negativeButtonListener = DialogInterface.OnClickListener { _, _ ->
                                    val intent = emailIntent(
                                        address = arrayOf(getString(R.string.preference_contact_summary)),
                                        cc = arrayOf(),
                                        subject = "${getString(R.string.app_name)} ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE}) " +
                                                getString(R.string.report_feedback),
                                        body = "Name: ${odooError.data.name}\n\n" +
                                                "Message: ${odooError.data.message}\n\n" +
                                                "Exception Type: ${odooError.data.exceptionType}\n\n" +
                                                "Arguments: ${odooError.data.arguments}\n\n" +
                                                "Debug: ${odooError.data.debug}\n\n"
                                    )
                                    try {
                                        startActivity(intent)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        showMessage(message = getString(R.string.preference_error_email_intent))
                                    }
                                },
                                showNeutralButton = true,
                                neutralButton = getString(R.string.preference_logout_title),
                                neutralButtonListener = DialogInterface.OnClickListener { _, _ ->
                                    logoutApp()
                                }
                            )
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                                ?: getString(R.string.generic_error)
                        @Suppress("DEPRECATION")
                        val message: CharSequence = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            Html.fromHtml(errorBody, Html.FROM_HTML_MODE_COMPACT)
                        else
                            Html.fromHtml(errorBody)

                        showMessage(
                                title = getString(R.string.server_request_error, response.code(), response.message()),
                                message = message,
                            positiveButton = getString(R.string.login_again),
                                positiveButtonListener = DialogInterface.OnClickListener { _, _ ->
                                    authenticate(user)
                                },
                                showNegativeButton = true,
                                negativeButton = getString(R.string.quit),
                                negativeButtonListener = DialogInterface.OnClickListener { _, _ ->
                                    ActivityCompat.finishAffinity(this@SplashActivity)
                                },
                            showNeutralButton = true,
                            neutralButton = getString(R.string.preference_logout_title),
                            neutralButtonListener = DialogInterface.OnClickListener { _, _ ->
                                logoutApp()
                            }
                        )
                    }
                }

                onError { error ->
                    showMessage(title = getString(R.string.operation_failed),
                            message = error.message,
                            positiveButtonListener = DialogInterface.OnClickListener { _, _ ->
                                ActivityCompat.finishAffinity(this@SplashActivity)
                            })
                }
            }
        } else {
            startLoginActivity()
        }
    }

    private fun logoutApp() {
        Single.fromCallable {
            for (odooUser in getOdooUsersFromAccountManager()) {
                deleteOdooUserFromAndroidAccount(odooUser)
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeEx {
            onSuccess {
                restartApp()
            }

            onError { error ->
                error.printStackTrace()
            }
        }
    }

    private fun authenticate(user: OdooUser) {
        Odoo.authenticate(login = user.login, password = user.password, database = user.database) {
            onSubscribe { disposable ->
                compositeDisposable?.add(disposable)
            }

            onNext { response ->
                if (response.isSuccessful) {
                    val authenticate = response.body()!!
                    if (authenticate.isSuccessful) {
                        //createAndroidAccountAfterAuthentication(authenticateResult = authenticate.result, user = user)
                    } else {
                        // logoutOdooUser(user)
                        logoutApp()
                    }
                } else {
                    showServerErrorMessage(response, positiveButtonListener = DialogInterface.OnClickListener { _, _ ->
                        ActivityCompat.finishAffinity(this@SplashActivity)
                    })
                }
            }

            onError { error ->
                showMessage(title = getString(R.string.operation_failed),
                        message = error.message,
                        positiveButtonListener = DialogInterface.OnClickListener { _, _ ->
                            ActivityCompat.finishAffinity(this@SplashActivity)
                        })
            }
        }
    }

    /*
    private fun createAndroidAccountAfterAuthentication(authenticateResult: AuthenticateResult, user: OdooUser) {
        Observable.fromCallable {
            //deleteOdooUserFromAndroidAccount(user)
            if (createOdooUserInAndroidAccount(authenticateResult)) {
                val odooUser = odooUserByAndroidName(authenticateResult.androidName)
                if (odooUser != null) {
                    saveAutheticatedUserInAccountManager(odooUser)
                    Odoo.user = odooUser
                    app.cookiePrefs.setCookies(Odoo.pendingAuthenticateCookies)
                }
                //Odoo.pendingAuthenticateCookies.clear()
                true
            } else {
                false
            }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx {
                    onSubscribe {
                        // Must be complete, not dispose in between
                        // compositeDisposable.add(d)
                    }

                    onNext { t: Boolean ->
                        if (t) {
                            restartApp()
                        } else {
                            closeApp()
                        }
                    }

                    onError { error: Throwable ->
                        error.printStackTrace()
                        closeApp(message = error.message ?: getString(R.string.generic_error))
                    }
                }
    }
    */

    private fun startLoginActivity() {
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
    }

    private fun startMainActivity() {
        startActivity(Intent(this@SplashActivity, ba.out.bring.odoo.mc1.MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        compositeDisposable?.dispose()
        super.onDestroy()
    }
}
