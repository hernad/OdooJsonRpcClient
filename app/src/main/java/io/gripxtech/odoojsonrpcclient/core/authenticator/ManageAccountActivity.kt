package io.gripxtech.odoojsonrpcclient.core.authenticator

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.gripxtech.odoojsonrpcclient.*
import io.gripxtech.odoojsonrpcclient.core.utils.BaseActivity
import io.gripxtech.odoojsonrpcclient.core.utils.recycler.decorators.VerticalLinearItemDecorator
import io.reactivex.disposables.CompositeDisposable
//import kotlinx.android.synthetic.main.activity_manage_account.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.Glide
import io.gripxtech.odoojsonrpcclient.databinding.ActivityManageAccountBinding

class ManageAccountActivity : BaseActivity() {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    lateinit var app: App private set
    lateinit var glideRequests: RequestManager
    lateinit var binding: ActivityManageAccountBinding // activity_manage_account.xml
    var compositeDisposable: CompositeDisposable? = null
        private set
    lateinit var adapter: ManageAccountAdapter private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Manage Account Activity"

        binding = ActivityManageAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        app = application as App
        glideRequests = Glide.with(this)

        compositeDisposable?.dispose()
        compositeDisposable = CompositeDisposable()
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)

        //setContentView(R.layout.activity_manage_account)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val users = getOdooUsersFromAccountManager()
        val layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        binding.rv.layoutManager = layoutManager
        binding.rv.addItemDecoration(
            VerticalLinearItemDecorator(
                resources.getDimensionPixelOffset(R.dimen.default_8dp)
            )
        )

        adapter = ManageAccountAdapter(this, ArrayList(users))
        binding.rv.adapter = adapter
    }

    override fun onDestroy() {
        compositeDisposable?.dispose()
        super.onDestroy()
    }
}
