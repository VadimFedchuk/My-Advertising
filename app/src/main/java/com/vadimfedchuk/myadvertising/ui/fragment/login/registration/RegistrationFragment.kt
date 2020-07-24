package com.vadimfedchuk.myadvertising.ui.fragment.login.registration

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.ViewModelFactory
import com.vadimfedchuk.myadvertising.remote.request.RegistrationRequest
import com.vadimfedchuk.myadvertising.utils.ShPreferences
import com.vadimfedchuk.myadvertising.utils.shortToast
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.view.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class RegistrationFragment : Fragment() {

    private var listener: OnRegistrationFragmentClickListener? = null
    private lateinit var viewModel: RegistrationViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(
            RegistrationViewModel::class.java)
        viewModel.let { lifecycle.addObserver(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)
        view.enter_tv.setOnClickListener {
            onEnterPressed()
        }
        view.registration_button.setOnClickListener {
            onRegistrationPressed()
        }
        return view
    }


    private fun onEnterPressed() {
        listener?.clickedEnterFromRegistration()
    }

    private fun onRegistrationPressed() {

        if(validateView()) {
            var phone = number_phone_et.text.toString()
            if(phone.contains('+')) {
                phone = phone.replace("+", "")
            }
            val body = RegistrationRequest(
                name = name_et.text.toString().trim(),
                phone = phone,
                password = password_et.text.toString().trim(),
                password_confirmation = password_et.text.toString().trim()
            )

            viewModel.registrationRequest(body)?.observe(viewLifecycleOwner, Observer {
                if(it.error == null || it.error == false) {
                    ShPreferences.setPasswordUser(password_et.text.toString().trim(), requireContext())
                    it.message?.shortToast(requireContext())
                    listener?.clickConfirmFromRegistration(it.token!!)
                } else {
                    getString(R.string.error_phone_registration).shortToast(requireContext())
                }
            })
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRegistrationFragmentClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnRegistrationFragmentClickListener")
        }
    }

    private fun validateView(): Boolean {
        if(name_et.text.isNullOrEmpty()) {
            getString(R.string.error_validate_et, name_et.hint).shortToast(requireContext())
            return false
        }
        if(number_phone_et.text.isNullOrEmpty()) {
            getString(R.string.error_validate_et, number_phone_et.hint).shortToast(requireContext())
            return false
        }
        if(password_et.text.isNullOrEmpty()) {
            getString(R.string.error_validate_et, password_et.hint).shortToast(requireContext())
            return false
        }
        if(!switch_condition.isChecked) {
            getString(R.string.error_validate_switch).shortToast(requireContext())
            return false
        }
        return true
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    interface OnRegistrationFragmentClickListener {
        fun clickedEnterFromRegistration()
        fun clickConfirmFromRegistration(temporarilyToken: String)
    }
}
