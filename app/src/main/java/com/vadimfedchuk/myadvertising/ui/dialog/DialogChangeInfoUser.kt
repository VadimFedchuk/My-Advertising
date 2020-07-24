package com.vadimfedchuk.myadvertising.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.utils.ShPreferences
import kotlinx.android.synthetic.main.dialog_change_info.*

class DialogChangeInfoUser(private val callback:()-> Unit) : DialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.dialog_change_info, container, false)
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name_change_et.text = SpannableStringBuilder(ShPreferences.getNameUser(requireContext()) ?: "")
        password_change_et.text = SpannableStringBuilder(ShPreferences.getPasswordUser(requireContext()) ?: "")
        confirm_change_button.setOnClickListener {
            ShPreferences.setNameUser(name_change_et.text.toString(), requireContext())
            ShPreferences.setPasswordUser(password_change_et.text.toString(), requireContext())
            callback.invoke()
            dismiss()
        }
    }
}