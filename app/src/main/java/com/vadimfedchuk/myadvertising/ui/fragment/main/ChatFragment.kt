package com.vadimfedchuk.myadvertising.ui.fragment.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.ui.adapters.ChatAdapter
import com.vadimfedchuk.myadvertising.ui.adapters.HistoryAdapter
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.android.synthetic.main.fragment_history.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ChatFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var views:View
    private val messages: ArrayList<com.vadimfedchuk.myadvertising.ui.Message> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_chat, container, false)
        initChatList()
        return views
    }

    private fun initChatList() {
        initMessages()
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

    private fun initMessages() {
        messages.add(com.vadimfedchuk.myadvertising.ui.Message(0, false, "Здравствуйте, мы можем Вам помочь?", "07.01 19:38", "Александра", "https://c.radikal.ru/c19/2002/3a/b238b167f608.png"))
        messages.add(com.vadimfedchuk.myadvertising.ui.Message(1, true, "Я бы хотела уточнить, есть ли ещё свободные места под рекламу на даты которые я оплатила.", "07.01 19:40", "Александра", "https://c.radikal.ru/c19/2002/3a/b238b167f608.png"))
        messages.add(com.vadimfedchuk.myadvertising.ui.Message(2, false, "Одну минутку, мы сейчас предложим вам подходящие варианты.", "07.01 19:41", "Александра", "https://c.radikal.ru/c19/2002/3a/b238b167f608.png"))
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            //throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
