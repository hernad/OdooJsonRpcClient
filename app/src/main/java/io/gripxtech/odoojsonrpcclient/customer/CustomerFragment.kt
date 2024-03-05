package io.gripxtech.odoojsonrpcclient.customer

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import io.gripxtech.odoojsonrpcclient.*
import io.gripxtech.odoojsonrpcclient.core.Odoo
import io.gripxtech.odoojsonrpcclient.core.OdooDatabase
import io.gripxtech.odoojsonrpcclient.core.utils.android.ktx.subscribeEx
import io.gripxtech.odoojsonrpcclient.customer.entities.Customer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.fragment_customer.*
//import kotlinx.android.synthetic.main.fragment_customer.tb
import timber.log.Timber
import com.bumptech.glide.RequestManager
import com.bumptech.glide.Glide
import io.gripxtech.odoojsonrpcclient.databinding.FragmentCustomerBinding

class CustomerFragment : Fragment() {

    companion object {

        enum class CustomerType {
            Customer,
            Supplier,
            Company
        }

        private const val TYPE = "type"

        fun newInstance(customerType: CustomerType) =
            CustomerFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE, customerType.name)
                }
            }
    }

    lateinit var activity: MainActivity private set
    lateinit var glideRequests: RequestManager private set
    private var compositeDisposable: CompositeDisposable? = null

    private var customerType: CustomerType = CustomerType.Customer
    private lateinit var drawerToggle: ActionBarDrawerToggle

    val adapter: CustomerAdapter by lazy {
        CustomerAdapter(this, arrayListOf())
    }

    private val customerListType = object : TypeToken<ArrayList<Customer>>() {}.type
    private val limit = RECORD_LIMIT
    lateinit var bindingFragmentCustomer: FragmentCustomerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        compositeDisposable?.dispose()
        compositeDisposable = CompositeDisposable()

        bindingFragmentCustomer = FragmentCustomerBinding.inflate(inflater, container, false)
        //val view = bindingFragmentCustomer.root

        //init()

        //return super.onCreateView(inflater, container, savedInstanceState)

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_customer, container, false)

       // https://stackoverflow.com/questions/57117338/how-to-use-view-binding-in-android
       return bindingFragmentCustomer.getRoot()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as MainActivity
        glideRequests = Glide.with(this)
        arguments?.let {
            customerType = CustomerType.valueOf(it.getString(TYPE) ?: "")
        }

        // Hiding MainActivity's AppBarLayout as well as NestedScrollView first
        activity.binding.abl.visibility = View.GONE
        activity.binding.nsv.visibility = View.GONE

        when (customerType) {
            CustomerType.Supplier -> {
                activity.binding.nv.menu.findItem(R.id.nav_supplier).isChecked = true
                activity.setTitle(R.string.action_supplier)
            }
            CustomerType.Company -> {
                activity.binding.nv.menu.findItem(R.id.nav_company).isChecked = true
                activity.setTitle(R.string.action_company)
            }
            else -> {
                activity.binding.nv.menu.findItem(R.id.nav_customer).isChecked = true
                activity.setTitle(R.string.action_customer)
            }
        }
        activity.setSupportActionBar(bindingFragmentCustomer.tb)
        val actionBar = activity.supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        drawerToggle = ActionBarDrawerToggle(
            activity, activity.binding.dl,
            bindingFragmentCustomer.tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        activity.binding.dl.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            activity, RecyclerView.VERTICAL, false
        )
        bindingFragmentCustomer.rv.layoutManager = layoutManager
        bindingFragmentCustomer.rv.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                activity,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
            )
        )

        adapter.setupScrollListener(bindingFragmentCustomer.rv)

        if (!adapter.hasRetryListener()) {
            adapter.retryListener {
                fetchCustomer()
            }
        }


        bindingFragmentCustomer.srl.setOnRefreshListener {
            adapter.clear()
            if (!adapter.hasMoreListener()) {
                adapter.showMore()
                fetchCustomer()
            }
            bindingFragmentCustomer.srl.post {
                bindingFragmentCustomer.srl.isRefreshing = false
            }
        }

        if (adapter.rowItemCount == 0) {
            adapter.showMore()
            fetchCustomer()
        }

        bindingFragmentCustomer.rv.adapter = adapter
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (::drawerToggle.isInitialized) {
            drawerToggle.onConfigurationChanged(newConfig)
        }
    }

    override fun onDestroyView() {
        compositeDisposable?.dispose()
        super.onDestroyView()
    }

    private fun fetchCustomer() {
        Odoo.searchRead(
            model = "res.partner", fields = Customer.fields,
            /*
            when (customerType) {
                CustomerType.Customer -> {
                    listOf(listOf("customer_rank", ">", 0))
                }
                CustomerType.Supplier -> {
                    listOf(listOf("supplier_rank", ">", 0))
                }
                CustomerType.Company -> {
                    listOf(listOf("is_company", "=", true))
                }
            }
            */
            domain = listOf(listOf("is_company", "=", true)), offset = adapter.rowItemCount, limit = limit, sort ="name ASC"
        ) {

            onSubscribe { disposable ->
                compositeDisposable?.add(disposable)
            }

            onNext { response ->
                if (response.isSuccessful) {
                    val searchRead = response.body()!!
                    if (searchRead.isSuccessful) {
                        adapter.hideEmpty()
                        adapter.hideError()
                        adapter.hideMore()
                        val items: ArrayList<Customer> = gson.fromJson(searchRead.result.records, customerListType)
                        // insertCustomers(items)
                        if (items.size < limit) {
                            adapter.removeMoreListener()
                            if (items.size == 0 && adapter.rowItemCount == 0) {
                                adapter.showEmpty()
                            }
                        } else {
                            if (!adapter.hasMoreListener()) {
                                adapter.moreListener {
                                    fetchCustomer()
                                }
                            }
                        }
                        adapter.addRowItems(items)
                    } else {
                        adapter.showError(searchRead.errorMessage)
                        activity.promptReport(searchRead.odooError)
                    }
                } else {
                    adapter.showError(response.errorBodySpanned)
                }
                adapter.finishedMoreLoading()
            }

            onError { error ->
                error.printStackTrace()
                adapter.showError(error.message ?: getString(R.string.generic_error))
                adapter.finishedMoreLoading()
            }
        }
    }

    private fun insertCustomers(items: ArrayList<Customer>) {
        Single.fromCallable<List<Long>> {
            OdooDatabase.database?.customerDao()?.insertCustomers(items)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeEx {
            onSubscribe { disposable ->
                compositeDisposable?.add(disposable)
            }

            onSuccess { response ->
                Timber.d("insertCustomers() > ...subscribeEx{...} > onSuccess{...} response: $response")
                retrieveData()
            }

            onError { error ->
                error.printStackTrace()
                activity.showMessage(message = error.message)
            }
        }
    }

    private fun retrieveData() {
        Single.fromCallable<List<Customer>> {
            OdooDatabase.database?.customerDao()?.getCustomers()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeEx {
            onSubscribe { disposable ->
                compositeDisposable?.add(disposable)
            }

            onSuccess { response ->
                Timber.d("retrieveData() > ...subscribeEx{...} > onSuccess{...} response:")
                val items = ArrayList(response)
                for (item in items) {
                    Timber.i("Item is $item")
                }
            }

            onError { error ->
                error.printStackTrace()
                activity.showMessage(message = error.message)
            }
        }
    }
}
