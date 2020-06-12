package com.vadimfedchuk.myadvertising.ui.fragment.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.ViewModelFactory
import com.vadimfedchuk.myadvertising.ui.dialog.DialogChangeInfoUser
import com.vadimfedchuk.myadvertising.ui.dialog.OnConfirmChangeInfoUserCallback
import com.vadimfedchuk.myadvertising.utils.ShPreferences
import com.vadimfedchuk.myadvertising.utils.shortToast
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


const val REQUEST_GALLERY = 100
const val PASSWORD_HINT = "**********"
class ProfileFragment : Fragment(), OnConfirmChangeInfoUserCallback {

    private lateinit var views:View
    private var isFirstClickPassword = false
    //val cards: ArrayList<Cards> = ArrayList()

    private lateinit var viewModel: MainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            MainViewModel::class.java)
        viewModel.let { lifecycle.addObserver(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_profile, container, false)
        initUI()
        //initHistoryList()
        return views
    }

    private fun initUI() {
        views.tv_name_user.text = ShPreferences.getNameUser(requireContext())
        views.tv_phone_user.text = ShPreferences.getPhoneUser(requireContext())

        views.tv_password_user.setOnClickListener {
            if (isFirstClickPassword) {
                isFirstClickPassword = false
                tv_password_user.text = PASSWORD_HINT
            } else {
                isFirstClickPassword = true
                tv_password_user.text = ShPreferences.getPasswordUser(requireContext())
            }
        }

        views.exit_button.setOnClickListener {
            ShPreferences.setToken(null, requireContext())
            requireActivity().finish()
        }

        if(ShPreferences.getAvatarUser(requireContext()) != null) {
            Glide
                .with(this)
                .load(ShPreferences.getAvatarUser(requireContext()))
                .error(R.drawable.user_avatar_holder)
                .into(views.user_photo)
        }
        views.user_photo.setOnClickListener {
            openGallery()
        }

        views.textView_change.setOnClickListener {
            openDialog()
        }
    }

    private fun openDialog() {
        val fragmentExpectedWage = DialogChangeInfoUser.newInstance()
        fragmentExpectedWage.setOnClickCallback(this)
        fragmentExpectedWage.show(requireActivity().supportFragmentManager, "dialog_change_info")
    }

    override fun confirmChange() {
        initUI()
        viewModel.updateUserInfo(
            ShPreferences.getNameUser(requireContext())?:"",
            ShPreferences.getPasswordUser(requireContext())?:"",
            ShPreferences.getToken(requireContext())?:"")?.observe(viewLifecycleOwner, Observer {
            it.message?.shortToast(requireContext())
        })
    }

    private fun openGallery() {
        val takeImageIntent = Intent(Intent.ACTION_PICK)
        takeImageIntent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        takeImageIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(takeImageIntent, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GALLERY -> if (resultCode == AppCompatActivity.RESULT_OK) {
                val imageUri = data!!.data
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                views.user_photo.setImageBitmap(bitmap)
                viewModel.addUserAvatar(ShPreferences.getToken(requireContext())!!, getPath(imageUri!!))!!.observe(viewLifecycleOwner, Observer {
                    it.message?.shortToast(requireContext())
                })
            } else {
                getString(R.string.error_pick_image).shortToast(requireContext())
            }
        }
    }

    private fun getPath(uri: Uri): String {
        val data = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(requireContext(), uri, data, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor!!.getString(column_index!!)
    }

//    private fun initHistoryList() {
//        initCards()
//        views.recycler_cards.layoutManager = LinearLayoutManager(requireContext())
//
//        views.recycler_cards.adapter =
//            CardsAdapter(cards, requireContext()) {
//                //onClickItem(it.price)
//
//            }
//        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val adapter = views.recycler_cards.adapter as CardsAdapter
//                adapter.removeAt(viewHolder.adapterPosition)
//            }
//        }
//        val itemTouchHelper = ItemTouchHelper(swipeHandler)
//        itemTouchHelper.attachToRecyclerView(views.recycler_cards)
//    }

//    private fun initCards() {
//        cards.add(Cards("MasterCard", "Срок действия 02/20", "**** **** **** 4098"))
//        cards.add(Cards("Visa", "Срок действия 02/20", "**** **** **** 4098"))
//    }

    companion object {

        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
