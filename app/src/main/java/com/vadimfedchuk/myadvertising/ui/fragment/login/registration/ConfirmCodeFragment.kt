package com.vadimfedchuk.myadvertising.ui.fragment.login.registration

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.utils.ShPreferences
import com.vadimfedchuk.myadvertising.utils.shortToast
import kotlinx.android.synthetic.main.fragment_confirm_code.*
import kotlinx.android.synthetic.main.fragment_confirm_code.view.*

private const val ARG_PARAM_TOKEN = "temporarily_token"
private const val ARG_PARAM_FLAG = "flag"

class ConfirmCodeFragment : Fragment() {

    private var temporarilyToken: String = ""
    private var listenerCode: OnConfirmCodeFragmentClickListener? = null
    private lateinit var viewModel: RegistrationViewModel
    private lateinit var timer: CountDownTimer
    private var secondsRemaining: Long = 5*60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            temporarilyToken = it.getString(ARG_PARAM_TOKEN)!!

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        viewModel.let { lifecycle.addObserver(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_confirm_code, container, false)
        startTimer()
        view.back_button_confirm_code.setOnClickListener {
            onBackPressed()
        }
        view.confirm_code_button.setOnClickListener {
                if (pinView.text!!.length < 4) {
                    getString(R.string.error_validate_code).shortToast(requireContext())
                } else {
                    if(requireArguments()[ARG_PARAM_FLAG] as Boolean) {
                    viewModel.requestCode(temporarilyToken, pinView.text.toString())
                        ?.observe(viewLifecycleOwner, Observer {
                            if (it.error == null || it.error == false) {
                                it.message?.shortToast(requireContext())
                                ShPreferences.setToken(it.token ?: "null token", requireContext())
                                ShPreferences.setNameUser(it.userInfo?.name ?: "", requireContext())
                                ShPreferences.setPhoneUser(
                                    it.userInfo?.phone ?: "",
                                    requireContext()
                                )
                                sendFirebaseToken(it.token!!)
                            } else {
                                getString(R.string.error_confirm_code).shortToast(requireContext())
                            }
                        })
                }else {
                    viewModel.recoveryPassword(temporarilyToken, pinView.text.toString())?.observe(viewLifecycleOwner, Observer {
                        if (it.error == null || it.error == false) {
                            it.message?.shortToast(requireContext())
                            onConfirmPressed(it.password!!)
                            ShPreferences.setPasswordUser(it.password, requireContext())
                        } else {
                            getString(R.string.error_confirm_code).shortToast(requireContext())
                        }
                    })
                }
            }
        }

        return view
    }

    private fun sendFirebaseToken(token: String) {
        viewModel.sendFirebaseToken(token, ShPreferences.getTokenFirebase(requireContext())?: "")?.observe(viewLifecycleOwner, Observer {
            if(it.error == null || it.error == false) {
                onConfirmPressed()
            } else {
                it.message?.shortToast(requireContext())
            }
        })
    }

    private fun onBackPressed() {
        listenerCode?.clickedBackFromConfirmCode()
    }

    private fun onConfirmPressed() {
        listenerCode?.clickedConfirmFromConfirmCode()
    }

    private fun onConfirmPressed(password:String) {
        listenerCode?.clickedConfirmRecoveryPassword(password)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnConfirmCodeFragmentClickListener) {
            listenerCode = context
        } else {
            throw RuntimeException("$context must implement OnConfirmCodeFragmentClickListener")
        }
    }

    private fun startTimer(){

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() {}

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        timer_tv.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
    }

    override fun onDetach() {
        super.onDetach()
        timer.cancel()
        listenerCode = null
    }

    interface OnConfirmCodeFragmentClickListener {
        fun clickedConfirmFromConfirmCode()
        fun clickedBackFromConfirmCode()
        fun clickedConfirmRecoveryPassword(password:String)
    }

    companion object {

        @JvmStatic
        fun newInstance(token: String, isFromRegistration: Boolean) =
            ConfirmCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_TOKEN, token)
                    putBoolean(ARG_PARAM_FLAG, isFromRegistration)
                }
            }
    }
}
