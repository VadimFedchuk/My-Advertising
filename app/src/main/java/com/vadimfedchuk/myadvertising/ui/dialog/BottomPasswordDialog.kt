package com.vadimfedchuk.myadvertising.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vadimfedchuk.myadvertising.R

private const val ARG_PARAM_PASSWORD = "password"
class BottomPasswordDialog : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_layout, container, false)
        val password = requireArguments()[ARG_PARAM_PASSWORD] as String
        view.findViewById<TextView>(R.id.tv_password).text = password
        view.findViewById<Button>(R.id.close_password_dialog).setOnClickListener {
            dismiss()
        }
        return view
    }

    companion object {

        fun newInstance(password:String): BottomPasswordDialog {
            return BottomPasswordDialog()
                .apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_PASSWORD, password)
                }
            }
        }
    }
}