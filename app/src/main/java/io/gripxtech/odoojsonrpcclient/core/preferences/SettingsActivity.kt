package io.gripxtech.odoojsonrpcclient.core.preferences

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import io.gripxtech.odoojsonrpcclient.App
import io.gripxtech.odoojsonrpcclient.MainActivity
import io.gripxtech.odoojsonrpcclient.R
import io.gripxtech.odoojsonrpcclient.core.utils.BaseActivity
import io.gripxtech.odoojsonrpcclient.databinding.ActivityProfileBinding
import io.gripxtech.odoojsonrpcclient.databinding.ActivitySettingsBinding

//import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    private lateinit var app: App
    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        app = application as App

        title = getString(R.string.action_settings)

        binding.toolbar.setOnClickListener {
            startActivity(Intent(it.context, MainActivity::class.java))
            finish()
            return@setOnClickListener
        }


        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
}
