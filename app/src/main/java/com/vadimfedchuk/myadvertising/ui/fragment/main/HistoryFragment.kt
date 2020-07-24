package com.vadimfedchuk.myadvertising.ui.fragment.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.ViewModelFactory
import com.vadimfedchuk.myadvertising.remote.response.OrdersItem
import com.vadimfedchuk.myadvertising.ui.adapters.HistoryAdapter
import com.vadimfedchuk.myadvertising.utils.ShPreferences
import kotlinx.android.synthetic.main.fragment_history.view.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HistoryFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentHistoryInteractionListener? = null
    private lateinit var views: View
    var histories: ArrayList<OrdersItem> = ArrayList()

    private lateinit var viewModel: MainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            MainViewModel::class.java)
        viewModel.let { lifecycle.addObserver(it) }
        getInfo()
    }

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
        views = inflater.inflate(R.layout.fragment_history, container, false)
            //initHistoryList()
        return views
    }

    private fun getInfo() {
        viewModel.getHistoryOrders(ShPreferences.getToken(requireContext()) as String)?.observe(viewLifecycleOwner, Observer {
            histories = ArrayList(it.orders!!)
            initHistoryList()
        })
    }

    fun onClickItem(id: Int) {
        listener?.onRemoveOrder(id)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentHistoryInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentHistoryInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun initHistoryList() {
        views.recycler_history.layoutManager = LinearLayoutManager(requireContext())

        views.recycler_history.adapter =
            HistoryAdapter(histories, requireContext()) {
                onClickItem(it.orderId!!)

            }
    }

    interface OnFragmentHistoryInteractionListener {
        fun onRemoveOrder(id: Int)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
