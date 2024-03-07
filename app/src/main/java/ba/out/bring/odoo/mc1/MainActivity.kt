package ba.out.bring.odoo.mc1

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import ba.out.bring.odoo.mc1.core.preferences.SettingsActivity
import ba.out.bring.odoo.mc1.core.utils.BaseActivity
import ba.out.bring.odoo.mc1.core.utils.NavHeaderViewHolder
import ba.out.bring.odoo.mc1.core.utils.android.ktx.postEx
import ba.out.bring.odoo.mc1.customer.CustomerFragment
import com.bumptech.glide.Glide
import ba.out.bring.odoo.mc1.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

        private const val ACTION_CUSTOMER = 1
        private const val ACTION_SUPPLIER = 2
        private const val ACTION_COMPANY = 3
    }

    lateinit var app: ba.out.bring.odoo.mc1.App private set
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navHeader: NavHeaderViewHolder
    // https://developer.android.com/topic/libraries/view-binding
    lateinit var binding: ActivityMainBinding // activity_main.xml

    private var currentDrawerItemID: Int = 0

    private val customerFragment: CustomerFragment by lazy {
        CustomerFragment.newInstance(CustomerFragment.Companion.CustomerType.Customer)
    }

    private val supplierFragment: CustomerFragment by lazy {
        CustomerFragment.newInstance(CustomerFragment.Companion.CustomerType.Supplier)
    }

    private val companyFragment: CustomerFragment by lazy {
        CustomerFragment.newInstance(CustomerFragment.Companion.CustomerType.Company)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as ba.out.bring.odoo.mc1.App

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setContentView(R.layout.activity_main)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        //val tb = view.tb
        //val dl = view.dl

        setSupportActionBar(binding.tb)

        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.tb.setNavigationOnClickListener {
            binding.dl.openDrawer(GravityCompat.START)
        }
        setTitle(R.string.app_name)

        drawerToggle = ActionBarDrawerToggle(
            this, binding.dl, binding.tb,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.dl.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val view = binding.nv.getHeaderView(0)
        if (view != null) {
            navHeader = NavHeaderViewHolder(view)
            val user = getActiveOdooUser()
            if (user != null) {
                navHeader.setUser(user, Glide.with(this@MainActivity))
            }
        }

        binding.nv.setNavigationItemSelectedListener { item ->
            binding.dl.postEx { closeDrawer(GravityCompat.START) }
            when (item.itemId) {
                R.id.nav_customer -> {
                    if (currentDrawerItemID != ba.out.bring.odoo.mc1.MainActivity.Companion.ACTION_CUSTOMER) {
                        loadFragment(ba.out.bring.odoo.mc1.MainActivity.Companion.ACTION_CUSTOMER)
                    }
                    true
                }
                R.id.nav_supplier -> {
                    if (currentDrawerItemID != ba.out.bring.odoo.mc1.MainActivity.Companion.ACTION_SUPPLIER) {
                        loadFragment(ba.out.bring.odoo.mc1.MainActivity.Companion.ACTION_SUPPLIER)
                    }
                    true
                }
                R.id.nav_company -> {
                    if (currentDrawerItemID != ba.out.bring.odoo.mc1.MainActivity.Companion.ACTION_COMPANY) {
                        loadFragment(ba.out.bring.odoo.mc1.MainActivity.Companion.ACTION_COMPANY)
                    }
                    true
                }

                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> {
                    true
                }
            }
        }

        /*
        if (savedInstanceState == null) {
            loadFragment(ACTION_CUSTOMER)
        }
        */
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    private fun loadFragment(currentDrawerItemID: Int) {
        clearBackStack()
        this.currentDrawerItemID = currentDrawerItemID
        when (currentDrawerItemID) {
            ba.out.bring.odoo.mc1.MainActivity.Companion.ACTION_CUSTOMER -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.clMain, customerFragment, getString(R.string.action_customer))
                    .commit()
            }
            ba.out.bring.odoo.mc1.MainActivity.Companion.ACTION_SUPPLIER -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.clMain, supplierFragment, getString(R.string.action_supplier))
                    .commit()
            }
            ba.out.bring.odoo.mc1.MainActivity.Companion.ACTION_COMPANY -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.clMain, companyFragment, getString(R.string.action_company))
                    .commit()
            }
        }
    }

    private fun clearBackStack() {
        val fragmentManager = supportFragmentManager
        for (i in 0 until fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStackImmediate()
        }
    }
}
