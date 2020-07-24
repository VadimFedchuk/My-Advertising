package com.vadimfedchuk.myadvertising.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.ui.fragment.login.*
import com.vadimfedchuk.myadvertising.ui.fragment.login.registration.ConfirmCodeFragment
import com.vadimfedchuk.myadvertising.ui.fragment.login.registration.RegistrationFragment
import com.vadimfedchuk.myadvertising.utils.ShPreferences

class LoginActivity : AppCompatActivity(),
    EnterFragment.OnFragmentClickListener,
    RegistrationFragment.OnRegistrationFragmentClickListener,
    RestorePasswordFragment.OnRestoreFragmentClickListener,
    ConfirmCodeFragment.OnConfirmCodeFragmentClickListener,
    ConfirmLocationFragment.OnConfirmLocFragmentClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(ShPreferences.getToken(this) != null) {
            openMainActivity()
        } else {
            setContentView(R.layout.activity_login)
            initEnterFragment()
        }
    }

    private fun initEnterFragment(password:String = "") {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerViewLogin, EnterFragment.newInstance(password))
            .setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            .commit()
    }

    private fun addFragment(fragment:Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerViewLogin, fragment)
            .setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            .addToBackStack(null)
            .commit()
    }

    override fun clickedEnter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                addFragment(ConfirmLocationFragment.newInstance())
            } else {
                openMainActivity()
            }
        } else {
            openMainActivity()
        }
    }

    override fun clickRegistration() {
        addFragment(RegistrationFragment.newInstance("", ""))
    }

    override fun clickForgetPassword() {
        addFragment(RestorePasswordFragment.newInstance())
    }

    override fun clickConfirmFromRegistration(temporarilyToken: String) {
        addFragment(ConfirmCodeFragment.newInstance(temporarilyToken, true))
    }

    override fun clickedEnterFromRegistration() {
        supportFragmentManager.popBackStack()
    }

    override fun clickedConfirmFromRestore(temporarilyToken: String) {
        addFragment(ConfirmCodeFragment.newInstance(temporarilyToken, false))
    }

    override fun clickedBackFromRestore() {
        initEnterFragment()
    }

    override fun clickedConfirmFromConfirmCode() {
        clickedEnter()
    }

    override fun clickedBackFromConfirmCode() {
        supportFragmentManager.popBackStack()
    }
    override fun clickedConfirmRecoveryPassword(password:String) {
        initEnterFragment(password)
    }

    override fun clickedConfirmLocationFromConfirmLoc() {
        openMainActivity()
    }

    override fun clickedRemindLaterFromConfirmLoc() {
        openMainActivity()
    }

    private fun openMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }
}
