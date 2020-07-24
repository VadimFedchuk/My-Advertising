package com.vadimfedchuk.myadvertising.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.vadimfedchuk.myadvertising.R
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter(private val items : ArrayList<String>, val context: Context, val onClickCategory:(item: String) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_category,
                parent,
                false
            )
        ).listen { position ->
            onClickCategory(items[position])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCategory?.text = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    val tvCategory: AppCompatTextView? = view.category_tv
}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition)
    }
    return this
}