package com.vadimfedchuk.myadvertising.ui.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.ViewModelFactory
import com.vadimfedchuk.myadvertising.ui.Message
import com.vadimfedchuk.myadvertising.ui.adapters.ChatAdapter
import com.vadimfedchuk.myadvertising.utils.ShPreferences
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {

    private lateinit var views:View
    private val messages: ArrayList<Message> = ArrayList()
    private lateinit var viewModel: MainViewModel

    private var fb_token:String = ""
    private var formatDate = SimpleDateFormat("h:mm MMMMM.dd")

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            MainViewModel::class.java)
        viewModel.let { lifecycle.addObserver(it) }
        getFirebaseToken()
    }

    private fun observeData() {
        viewModel.getMessages(ShPreferences.getTokenFirebase(requireContext())?: "").observe(viewLifecycleOwner, Observer {
            if(it != null) {
                it.messages.forEach{message ->
                    messages.add(Message(
                        it.user_id == message.sender_id,
                        message.message,
                        formatDate.format(Date(message.dt.toString())),
                        "Служба поддержки",
                        null))
                }
                recycler_chat.adapter?.notifyDataSetChanged()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_chat, container, false)
        initChatList()

        views.send_message_ib.setOnClickListener {
            if(!et_message.text.isNullOrEmpty()) {
                sendMessage()
            }
        }

        return views
    }

    private fun sendMessage() {
        viewModel.sendMessage(et_message.text.toString(), ShPreferences.getNameUser(requireContext())?: "no name", ShPreferences.getTokenFirebase(requireContext())?: "").observe(viewLifecycleOwner, Observer {
            if(it != null && it.status.equals("OK")) {
                messages.add(Message(true, et_message.text.toString(), "14.09.94", null, null))
                recycler_chat.adapter?.notifyDataSetChanged()
            }
        })
    }

    private fun initChatList() {
        val layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        layoutManager.stackFromEnd  = true
        views.recycler_chat.layoutManager = layoutManager
        views.recycler_chat.adapter =
            ChatAdapter(requireContext(), messages)
    }

    fun getFirebaseToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                fb_token = task.result?.token!!
                observeData()

            })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatFragment()
    }
}
