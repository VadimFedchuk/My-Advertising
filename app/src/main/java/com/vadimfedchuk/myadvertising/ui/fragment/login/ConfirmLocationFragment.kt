package com.vadimfedchuk.myadvertising.ui.fragment.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.utils.REQUEST_GALLERY
import com.vadimfedchuk.myadvertising.utils.shortToast
import kotlinx.android.synthetic.main.fragment_confirm_location.view.*
import org.json.JSONObject
import java.io.File

class ConfirmLocationFragment : Fragment() {

    private val REQUEST_LOCATION: Int = 100
    private var listener: OnConfirmLocFragmentClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_confirm_location, container, false)
        view.confirm_location_button.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        }
        view.remind_later_tv.setOnClickListener {
            onRemindLaterPressed()
        }
        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (permissions[0] == Manifest.permission.ACCESS_COARSE_LOCATION
            && grantResults[0] == PackageManager.PERMISSION_GRANTED || permissions[1] == Manifest.permission.ACCESS_FINE_LOCATION
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onConfirmLocPressed()
            } else {
                getString(R.string.error_receive_permission).shortToast(requireContext())
            }
        }

    private fun onConfirmLocPressed() {
        listener?.clickedConfirmLocationFromConfirmLoc()
    }

    private fun onRemindLaterPressed() {
        listener?.clickedRemindLaterFromConfirmLoc()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnConfirmLocFragmentClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnConfirmLocFragmentClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnConfirmLocFragmentClickListener {
        fun clickedConfirmLocationFromConfirmLoc()
        fun clickedRemindLaterFromConfirmLoc()
    }

    companion object {

        @JvmStatic
        fun newInstance() = ConfirmLocationFragment()
    }
}
