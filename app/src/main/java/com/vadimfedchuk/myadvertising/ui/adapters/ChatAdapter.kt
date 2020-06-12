package com.vadimfedchuk.myadvertising.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.vadimfedchuk.myadvertising.utils.COMPANION_MESSAGE
import com.vadimfedchuk.myadvertising.utils.MY_MESSAGE
import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.ui.Message

class ChatAdapter(val context: Context, val messages: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when(viewType) {
            MY_MESSAGE -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_my_message, parent, false)
                ViewHolderMy(view)
            }
            else -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_message_to_me, parent, false)
                ViewHolderCompanion(view)
            }
        }
    }

    override fun getItemCount() = messages.size

    override fun getItemViewType(position: Int): Int {
        val item = messages[position]
        if (item.isMyMessage) {
            return MY_MESSAGE
        }
        return COMPANION_MESSAGE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = messages[position]
        when(holder.itemViewType) {
            MY_MESSAGE -> {
                (holder as ViewHolderMy).bind(item)
            }
            COMPANION_MESSAGE -> {
                (holder as ViewHolderCompanion).bind(item)
            }
        }
    }

    class ViewHolderCompanion(val view: View) : RecyclerView.ViewHolder(view) {

        private val message = view.findViewById<TextView>(R.id.msg_companion)
        private val name = view.findViewById<TextView>(R.id.name_message_companion)
        private val date = view.findViewById<TextView>(R.id.date_message_companion)
        private val photo = view.findViewById<ImageView>(R.id.companion_photo)

        fun bind(item: Message) {
            message.text = item.message
            name.text = item.nameCompanion
            date.text = item.date
            setPhoto(item.photoCompanion)
        }

        private fun setPhoto(url: String?) {
            if (url != "" && url != null)
                Glide.with(photo.context)
                    .load(url)
                    .apply(
                        RequestOptions()
                            .override(50, 50)
                            .encodeQuality(100)
                            .fitCenter()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    )
                    .into(photo)
        }
    }

    class ViewHolderMy(val view: View) : RecyclerView.ViewHolder(view) {

        private val message = view.findViewById<TextView>(R.id.msg_my)
        private val date = view.findViewById<TextView>(R.id.date_message_my)

        fun bind(item: Message) {
            message.text = item.message
            date.text = item.date
        }
    }
}