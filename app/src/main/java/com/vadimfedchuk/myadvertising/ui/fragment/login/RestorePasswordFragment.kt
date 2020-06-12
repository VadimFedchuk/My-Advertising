package com.vadimfedchuk.myadvertising.ui.fragment.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.ui.fragment.login.registration.RegistrationViewModel
import com.vadimfedchuk.myadvertising.utils.shortToast
import kotlinx.android.synthetic.main.fragment_restore_password.*
import kotlinx.android.synthetic.main.fragment_restore_password.view.*



class RestorePasswordFragment : Fragment() {


    private var listener: OnRestoreFragmentClickListener? = null
    private lateinit var viewModel: RegistrationViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        viewModel.let { lifecycle.addObserver(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restore_password, container, false)
        view.back_button_restore_code.setOnClickListener {
            onBackPressed()
        }
        view.confirm_button.setOnClickListener {

                viewModel.recoveryPassword(number_phone_et.text.toString().trim())?.observe(viewLifecycleOwner, Observer {
                    if(it.error == null) {
                        it.message?.shortToast(requireContext())
                        onBackConfirm(it.token!!)
                    } else {
                        it.message?.shortToast(requireContext())
                    }
                })

        }
        return view
    }

    private fun onBackPressed() {
        listener?.clickedBackFromRestore()
    }

    private fun onBackConfirm(temporarilyToken: String) {
        listener?.clickedConfirmFromRestore(temporarilyToken)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRestoreFragmentClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnRestoreFragmentClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnRestoreFragmentClickListener {
        fun clickedConfirmFromRestore(temporarilyToken: String)
        fun clickedBackFromRestore()
    }

    companion object {

        @JvmStatic
        fun newInstance() = RestorePasswordFragment()
    }
}
