package com.vadimfedchuk.myadvertising.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.ui.fragment.main.*
import com.vadimfedchuk.myadvertising.utils.setGone
import com.vadimfedchuk.myadvertising.utils.setVisible
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileInputStream


class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    HistoryFragment.OnFragmentHistoryInteractionListener,
    CancelOrderFragment.OnFragmentConfirmCancelListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this);
        initBottomBar()
    }

    private fun initBottomBar() {
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        bottom_navigation.selectedItemId = R.id.action_map
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.action_profile -> addFragment(ProfileFragment.newInstance())
            R.id.action_message -> addFragment(ChatFragment.newInstance("", ""))
            R.id.action_history -> addFragment(HistoryFragment.newInstance("", ""))
            R.id.action_map -> addFragment(MapFragment.newInstance("", ""))
        }
        return true
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerViewMain, fragment)
            .setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        bottom_navigation.setVisible()
    }

    override fun onRemoveOrder(id: Int) {
        bottom_navigation.setGone()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerViewMain, CancelOrderFragment.newInstance(id))
            .addToBackStack(null)
            .setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            .commit()
    }

    override fun onConfirmPressed() {
        bottom_navigation.setVisible()
        supportFragmentManager.popBackStack()
    }
}
