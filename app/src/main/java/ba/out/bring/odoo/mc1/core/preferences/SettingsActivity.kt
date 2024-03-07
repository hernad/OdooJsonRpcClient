package ba.out.bring.odoo.mc1.core.preferences

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import ba.out.bring.odoo.mc1.App
import ba.out.bring.odoo.mc1.MainActivity
import ba.out.bring.odoo.mc1.R
import ba.out.bring.odoo.mc1.core.utils.BaseActivity
import ba.out.bring.odoo.mc1.databinding.ActivityProfileBinding
import ba.out.bring.odoo.mc1.databinding.ActivitySettingsBinding

//import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    private lateinit var app: ba.out.bring.odoo.mc1.App
    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        app = application as ba.out.bring.odoo.mc1.App

        title = getString(R.string.action_settings)

        binding.toolbar.setOnClickListener {
            startActivity(Intent(it.context, ba.out.bring.odoo.mc1.MainActivity::class.java))
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
