package com.vadimfedchuk.myadvertising


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vadimfedchuk.myadvertising.ui.fragment.login.registration.RegistrationViewModel
import com.vadimfedchuk.myadvertising.ui.fragment.main.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            val key = "RegistrationViewModel"
            return if(hashMapViewModel.containsKey(key)){
                getViewModel(key) as T
            } else {
                addViewModel(key, RegistrationViewModel())
                getViewModel(key) as T
            }
        } else if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val key = "MainViewModel"
            return if(hashMapViewModel.containsKey(key)) {

                getViewModel(key) as T
            } else {
                addViewModel(key, MainViewModel())
                getViewModel(key) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


    companion object {
        val hashMapViewModel = HashMap<String, ViewModel>()
        fun addViewModel(key: String, viewModel: ViewModel){
            hashMapViewModel[key] = viewModel
        }
        fun getViewModel(key: String): ViewModel? {
            return hashMapViewModel[key]
        }
    }
}