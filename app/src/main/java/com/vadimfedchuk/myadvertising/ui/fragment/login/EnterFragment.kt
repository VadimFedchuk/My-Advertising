package com.vadimfedchuk.myadvertising.ui.fragment.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vadimfedchuk.myadvertising.ui.dialog.BottomPasswordDialog
import com.vadimfedchuk.myadvertising.ViewModelFactory
import com.vadimfedchuk.myadvertising.ui.fragment.login.registration.RegistrationViewModel
import com.vadimfedchuk.myadvertising.utils.ShPreferences
import com.vadimfedchuk.myadvertising.utils.shortToast
import kotlinx.android.synthetic.main.fragment_enter.number_phone_et
import kotlinx.android.synthetic.main.fragment_enter.password_et
import kotlinx.android.synthetic.main.fragment_enter.view.*


private const val ARG_PARAM_PASSWORD = "password"
class EnterFragment : Fragment() {

    private var listener: OnFragmentClickListener? = null
    private lateinit var viewModel: RegistrationViewModel

    companion object {
        @JvmStatic
        fun newInstance(password:String):EnterFragment {
            return EnterFragment().apply { 
                arguments = Bundle().apply { 
                    putString(ARG_PARAM_PASSWORD, password)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        val viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            RegistrationViewModel::class.java)
        viewModel.let { lifecycle.addObserver(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.vadimfedchuk.myadvertising.R.layout.fragment_enter, container, false)
        view.findViewById<Button>(com.vadimfedchuk.myadvertising.R.id.enter_button).setOnClickListener {
            if(validateView()) {
                var phone = number_phone_et.text.toString()
                if(phone.contains('+')) {
                    phone = phone.replace("+", "")
                }
                viewModel.requestLogin(phone, password_et.text.toString())!!
                .observe(viewLifecycleOwner, Observer {
                    if(it.error == null || it.error == false) {
                        onEnterPressed()
                        ShPreferences.setToken(it.token!!, requireContext())
                        ShPreferences.setNameUser(it.userInfo!!.name, requireContext())
                        ShPreferences.setPhoneUser(number_phone_et.text.toString(), requireContext())
                        ShPreferences.setAvatarUser("https://shopreklama.ru${it.userInfo.image!!}", requireContext())
                    } else {
                        it.message?.shortToast(requireContext())
                    }
                })

            }
        }
        view.registration_tv.setOnClickListener {
            onRegistrationPressed()
        }
        view.forget_password_tv.setOnClickListener {
            onForgetPasswordPressed()
        }
        if(arguments?.get(ARG_PARAM_PASSWORD) != null && (requireArguments().get(ARG_PARAM_PASSWORD) as String).length > 1) {
            showBottomSheet(requireArguments()[ARG_PARAM_PASSWORD] as String)
        }
        return view
    }

    private fun showBottomSheet(password: String) {
        val addPhotoBottomDialogFragment = BottomPasswordDialog.newInstance(password)
        addPhotoBottomDialogFragment.show(
           requireActivity().supportFragmentManager,
            "password_dialog_fragment"
        )
    }

    private fun onEnterPressed() {
        listener?.clickedEnter()
    }

    private fun onRegistrationPressed() {
        listener?.clickRegistration()
    }

    private fun onForgetPasswordPressed() {
        listener?.clickForgetPassword()
    }

    private fun validateView(): Boolean {
        if(number_phone_et.text.isNullOrEmpty()) {
            getString(com.vadimfedchuk.myadvertising.R.string.error_validate_et, number_phone_et.hint).shortToast(requireContext())
            return false
        }
        if(password_et.text.isNullOrEmpty()) {
            getString(com.vadimfedchuk.myadvertising.R.string.error_validate_et, password_et.hint).shortToast(requireContext())
            return false
        }

        return true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentClickListener {
        fun clickedEnter()
        fun clickRegistration()
        fun clickForgetPassword()
    }
}
