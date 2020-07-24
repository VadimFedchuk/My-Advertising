package com.vadimfedchuk.myadvertising.ui.adapters

import com.vadimfedchuk.myadvertising.R


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.vadimfedchuk.myadvertising.remote.response.OrdersItem
import com.vadimfedchuk.myadvertising.utils.CONFIRM_ORDER
import com.vadimfedchuk.myadvertising.utils.NOT_CONFIRM_ORDER
import kotlinx.android.synthetic.main.item_confirm_order.view.*

class HistoryAdapter(private val items : ArrayList<OrdersItem>, val context: Context, val onClick:(item: OrdersItem) -> Unit) : RecyclerView.Adapter<ViewHolderHistory>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHistory {

        val layout = if(viewType == CONFIRM_ORDER) {
            R.layout.item_confirm_order
        } else {
            R.layout.item_not_confirm_order
        }
        return ViewHolderHistory(LayoutInflater.from(context).inflate(layout, parent, false)).listenHistory { position ->
            if(items[position].status == "success") {
                onClick(items[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        if(item.status == "success") {
            return CONFIRM_ORDER
        }
        return NOT_CONFIRM_ORDER
    }

    override fun onBindViewHolder(holder: ViewHolderHistory, position: Int) {
        holder.type.text = items[position].type
        holder.address.text = items[position].address
        holder.period.text = items[position].endDate
        holder.price.text = context.getString(R.string.cost_adv, items[position].amount)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolderHistory (view: View) : RecyclerView.ViewHolder(view) {

    val type: AppCompatTextView = view.title_adv_tv
    val address: AppCompatTextView = view.title_address_tv
    val period: AppCompatTextView = view.period_tv
    val price: AppCompatTextView = view.price_tv
}

fun <T : RecyclerView.ViewHolder> T.listenHistory(event: (position: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition)
    }
    return this
}
