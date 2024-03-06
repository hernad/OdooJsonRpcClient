package ba.out.bring.odoo.mc1.customer

import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ba.out.bring.odoo.mc1.R
import ba.out.bring.odoo.mc1.core.utils.recycler.RecyclerBaseAdapter
import ba.out.bring.odoo.mc1.core.utils.recycler.entities.ErrorViewHolder
import ba.out.bring.odoo.mc1.customer.entities.Customer
import ba.out.bring.odoo.mc1.databinding.FragmentCustomerBinding
import ba.out.bring.odoo.mc1.databinding.ItemViewCustomerBinding
import ba.out.bring.odoo.mc1.databinding.ItemViewRecyclerErrorBinding
import ba.out.bring.odoo.mc1.trimFalse
//import kotlinx.android.synthetic.main.fragment_customer.*
//import kotlinx.android.synthetic.main.item_view_customer.view.*

class CustomerAdapter(
    private val fragment: CustomerFragment,
    items: ArrayList<Any>
) : RecyclerBaseAdapter(items, fragment.bindingFragmentCustomer.rv) {

    companion object {
        const val TAG: String = "CustomerAdapter"

        private const val VIEW_TYPE_ITEM = 0
    }

    private val rowItems: ArrayList<Customer> = ArrayList(
        items.filterIsInstance<Customer>()
    )
    lateinit var bindingItemViewCustomer: ItemViewCustomerBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // https://stackoverflow.com/questions/60423596/how-to-use-viewbinding-in-a-recyclerview-adapter
        bindingItemViewCustomer = ItemViewCustomerBinding.inflate(inflater, parent, false)


        //when (viewType) {
       //     VIEW_TYPE_ITEM -> {
                /*
                val view = inflater.inflate(
                    R.layout.item_view_customer,
                    parent,
                    false
                )
                return CustomerViewHolder(view)
                 */
          //      val view = bindingItemViewCustomer.root
          //      return CustomerViewHolder(view)

        //  }
        //}
        //return super.onCreateViewHolder(parent, viewType)
        return CustomerViewHolder(bindingItemViewCustomer.root)
    }

    override fun onBindViewHolder(baseHolder: RecyclerView.ViewHolder, basePosition: Int) {
        super.onBindViewHolder(baseHolder, basePosition)
        val position = baseHolder.adapterPosition
        when (getItemViewType(basePosition)) {
            VIEW_TYPE_ITEM -> {
                val holder = baseHolder as CustomerViewHolder
                val item = items[position] as Customer

                val imageSmall = item.image512.trimFalse()
                val name = item.name.trimFalse()

                fragment.glideRequests.asBitmap().load(
                    if (imageSmall.isNotEmpty())
                        Base64.decode(imageSmall, Base64.DEFAULT)
                    else
                        fragment.activity.app.getLetterTile(if (name.isNotEmpty()) name else "X")
                ).dontAnimate().circleCrop().into(bindingItemViewCustomer.imageSmall)

                bindingItemViewCustomer.name.text = name
                bindingItemViewCustomer.parentName.text = item.parentName.trimFalse()
                bindingItemViewCustomer.email.text = item.email.trimFalse()

                if (!bindingItemViewCustomer.clRoot.hasOnClickListeners()) {
                    bindingItemViewCustomer.clRoot.setOnClickListener {
                        // val clickedPosition = holder.adapterPosition
                        // val clickedItem = items[clickedPosition] as Customer
                    }
                }
            }
        }
    }

    val rowItemCount: Int get() = rowItems.size

    override fun getItemViewType(position: Int): Int {
        val o = items[position]
        if (o is Customer) {
            return VIEW_TYPE_ITEM
        }
        return super.getItemViewType(position)
    }

    private fun updateRowItems() {
        updateSearchItems()
        rowItems.clear()
        rowItems.addAll(
            ArrayList(
                items.filterIsInstance<Customer>()
            )
        )
    }

    fun addRowItems(rowItems: ArrayList<Customer>) {
        this.rowItems.addAll(rowItems)
        addAll(rowItems.toMutableList<Any>() as ArrayList<Any>)
    }

    override fun clear() {
        rowItems.clear()
        super.clear()
    }
}
