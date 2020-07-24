package com.vadimfedchuk.myadvertising.ui.fragment.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.ViewModelFactory
import com.vadimfedchuk.myadvertising.utils.ShPreferences
import com.vadimfedchuk.myadvertising.utils.shortToast
import kotlinx.android.synthetic.main.fragment_cancel_order.view.*

private const val ARG_PARAM1 = "param1"

class CancelOrderFragment : Fragment() {

    private var order_id: Int? = null

    private var listener: OnFragmentConfirmCancelListener? = null

    private lateinit var viewModel: MainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            MainViewModel::class.java)
        viewModel.let { lifecycle.addObserver(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            order_id = it.getInt(ARG_PARAM1)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cancel_order, container, false)
        view.cancel_order_confirm.setOnClickListener {
            viewModel.cancelOrder(ShPreferences.getToken(requireContext())!!, order_id!!)?.observe(viewLifecycleOwner, Observer {
                if(it.error == false) {
                    it.message?.shortToast(requireContext())
                    onConfirmPressed()
                } else {
                    it.message?.shortToast(requireContext())
                }
            })
        }
        return view
    }


    private fun onConfirmPressed() {
        listener?.onConfirmPressed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentConfirmCancelListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentConfirmCancelListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentConfirmCancelListener {
        fun onConfirmPressed()
    }

    companion object {

        @JvmStatic
        fun newInstance(order_id: Int) =
            CancelOrderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, order_id)
                }
            }
    }
}
