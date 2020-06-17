package com.vadimfedchuk.myadvertising.ui.fragment.main

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog
import com.vadimfedchuk.myadvertising.R
import com.vadimfedchuk.myadvertising.ViewModelFactory
import com.vadimfedchuk.myadvertising.remote.request.CreateOrderRequest
import com.vadimfedchuk.myadvertising.remote.response.MarkersItem
import com.vadimfedchuk.myadvertising.ui.activity.WebviewActivity
import com.vadimfedchuk.myadvertising.ui.adapters.CategoryAdapter
import com.vadimfedchuk.myadvertising.utils.*
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import kotlinx.android.synthetic.main.layout_detail_order.*
import kotlinx.android.synthetic.main.layout_detail_order.view.*
import kotlinx.android.synthetic.main.layout_detail_order.view.order_tv
import kotlinx.android.synthetic.main.layout_pay_order.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val categories: ArrayList<String> = ArrayList()
    private var isCollapsed = false
    private var isOpenDetailOrder = false
    lateinit var  currentDate: Date
    lateinit var fromDate: String
    lateinit var toDate: String
    private val sdf = SimpleDateFormat("yyyy-MM-dd")
    private lateinit var mMapView: MapView
    private lateinit var mapGoogle: GoogleMap
    private lateinit var viewModel: MainViewModel
    private lateinit var markers: List<MarkersItem>
    private lateinit var markerInfo: MarkersItem
    private lateinit var views:View

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            MainViewModel::class.java)
        viewModel.let { lifecycle.addObserver(it) }
        sendFirebaseToken()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapGoogle = googleMap
        mapGoogle.setOnMarkerClickListener(this)
        getMarkers()

    }

    override fun onMarkerClick(marker: Marker?): Boolean {

        markers.forEach {
            if(it.id == marker?.tag) {
                markerInfo = it
                isOpenDetailOrder = true
                recycler_category.setGone()
                layout_show_all.setGone()
                dialog_detail_order.setVisible()
                addInfoMarker(it)
            }
        }
        return true
    }

    private fun addInfoMarker(it: MarkersItem) {
        addImage("https://shopreklama.ru/${it.image}")
        views.title_adv_tv.text = "${it.type} ${it.sizeBillboard}"
        views.title_adv_address_tv.text = "${it.address}"
        views.price_month_tv.text = "${it.priceMonth}\u20bd"
        views.price_year_tv.text = "${it.priceYear}\u20bd"
        if(it.endDate != null) {
            views.date_tv.text = "${it.endDate.substring(0, it.endDate.indexOf(" "))}"
        }
    }

    private fun addImage(imageUrl: String?) {
        Glide
            .with(this)
            .load(imageUrl)
            .error(R.drawable.placeholder)
            .into(views.choose_photo_order_iv)
    }

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_map, container, false)
        mMapView = views.findViewById(R.id.mapView) as MapView

        mMapView.onCreate(savedInstanceState)
        try {
            MapsInitializer.initialize(requireContext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMapView.getMapAsync(this)
        initListCategory()
        initAction()
        return views
    }

    private fun sendFirebaseToken() {
        Log.i("TEST", "1 " + ShPreferences.getTokenFirebase(requireContext())?: "")
        Log.i("TEST", "2 " + ShPreferences.getToken(requireContext()))
        viewModel.sendFirebaseToken(ShPreferences.getToken(requireContext()) ?: "",
            ShPreferences.getTokenFirebase(requireContext())?: "")?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it.error == null || it.error == false) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

            } else {
                it.message?.shortToast(requireContext())
            }
        })
    }

    private fun getMarkers() {
        viewModel.getAllMarkers(ShPreferences.getToken(requireContext())!!)?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    views.progressBar_map.visibility = View.GONE
                    if(it?.data?.markers != null) {
                        markers = it.data.markers
                        addAllMarkers(markers)
                    }
                }
                Status.LOADING -> {
                    views.progressBar_map.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    //Handle Error
                    views.progressBar_map.visibility = View.GONE
                    it.message?.shortToast(requireContext())
                }
            }
        })
    }

    private fun addAllMarkers(info:List<MarkersItem>) {
        val russia = LatLng(52.4000000, 54.9833300)
        val camPos = CameraPosition.Builder().target(russia).zoom(3.0f).build()
        val cam = CameraUpdateFactory.newCameraPosition(camPos)
        mapGoogle.animateCamera(cam)
        info.forEach { item ->
            val marker = mapGoogle.addMarker(
                MarkerOptions().position(LatLng(item.latitude.toDouble(), item.longitude.toDouble()))
                    .title(item.type)
                    .icon(if(item.typePrice=="normal"){ bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_marker_normal) }
                    else {bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_marker_special)}))

            marker.tag = item.id

        }
    }


    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            //throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    private fun initListCategory() {

        addCategories()

        views.recycler_category?.layoutManager = GridLayoutManager(requireContext(), 3)

        views.recycler_category?.adapter =
            CategoryAdapter(categories, requireContext()) {categorySelected ->
                views.show_all_tv.text = categorySelected
                isCollapsed = true
                val animHide = AnimationUtils.loadAnimation(requireContext(), R.anim.show_view)
                views.recycler_category?.startAnimation(animHide)
                views.recycler_category?.visibility = View.GONE
                views.imageView_arrow?.setImageResource(R.drawable.ic_arrow_up_yellow)
                mapGoogle.clear()
                val filterList:List<MarkersItem>  = when(categorySelected) {
                    "Билборд" -> markers.filter { it.type == "billboard" }
                    "Транспорт" -> markers.filter { it.type == "transport" }
                    "Остановки" -> markers.filter { it.type == "stops" }
                    "Кафе" -> markers.filter { it.type == "cafes" }
                    "Спорт Залы" -> markers.filter { it.type == "gyms" }
                    "Показать всё" -> markers
                    else -> arrayListOf()
                }

                addAllMarkers(filterList)
            }
    }

    private fun initAction() {
        views.close_detail_order_tv?.setOnClickListener {
            isOpenDetailOrder = false
            views.dialog_detail_order?.setGone()
            views.recycler_category?.setVisible()
            views.layout_show_all?.setVisible()
        }

        views.date_from_button?.setOnClickListener {
            openCalendar(true)
        }

        views.date_to_button?.setOnClickListener {
            openCalendar(false)

        }

        views.order_tv?.setOnClickListener {
            //if(validator()) {
                isOpenDetailOrder = false

                val body = CreateOrderRequest("${markerInfo.id}", fromDate, toDate)
                viewModel.createOrder(ShPreferences.getToken(requireContext())!!, body)?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    if(it.error == null) {
                        startActivity(
                            Intent(
                                requireActivity(),
                                WebviewActivity::class.java
                            ).putExtra("url", it.url))
                    } else {
                        when {
                            it.messagesError?.start_date != null -> it.messagesError.start_date.toString().shortToast(requireContext())
                            it.messagesError?.end_date != null -> it.messagesError.end_date.toString().shortToast(requireContext())
                            it.messagesError?.marker_id != null -> it.messagesError.marker_id.toString().shortToast(requireContext())
                        }
                    }
                })
                views.dialog_detail_order?.setGone()

                //views.period_tv?.text = "${date_from_button.text} - ${date_to_button.text}"
            //}
        }

        views.cancel_pay_order_tv?.setOnClickListener {
            views.dialog_pay_order?.setGone()
            views.recycler_category?.setVisible()
            views.layout_show_all?.setVisible()
        }

        views.show_all_tv?.setOnClickListener {
            if(isCollapsed) {
                views.imageView_arrow?.setImageResource(R.drawable.ic_arrow_down_yellow)
                val animShow = AnimationUtils.loadAnimation( requireContext(), R.anim.hide_view)
                views.recycler_category?.setVisible()
                views.recycler_category?.startAnimation(animShow)

                isCollapsed = false
            }
        }
    }

    private fun openCalendar(isFromDate: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        currentDate = sdf.parse("$year-$month-$day")

        val dialog = DatePickerFragmentDialog.newInstance({ view, year1, month1, dayOfMonth1 ->
            if(isFromDate) {

                fromDate = "$year1-${month1+1}-$dayOfMonth1"
                views.date_from_button?.text = fromDate
            } else {
                toDate = "$year1-${month1+1}-$dayOfMonth1"
                views.date_to_button?.text = toDate
            }

        }, year, month, day)

        dialog.show(requireActivity().supportFragmentManager, "tag")

    }

    private fun validator(): Boolean {
        if(views.choose_photo_order_iv?.drawable == null) {
            getString(com.vadimfedchuk.myadvertising.R.string.error_pick_image).shortToast(requireContext())
            return false
        }

        try {
            Log.i("TEST", "$fromDate | $toDate | $currentDate")
//            if (currentDate.after(fromDate)) {
//                getString(com.vadimfedchuk.myadvertising.R.string.error_date).shortToast(requireContext())
//                return false
//            }

//            if(fromDate.after(toDate)) {
//                getString(com.vadimfedchuk.myadvertising.R.string.error_date_2).shortToast(requireContext())
//                return false
//            }
        } catch (e: ParseException) {
            getString(com.vadimfedchuk.myadvertising.R.string.error_choose_date).shortToast(requireContext())
            return false
        }

        return true
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun addCategories() {

        categories.add("Билборд")
        categories.add("Транспорт")
        categories.add("Остановки")
        categories.add("Кафе")
        categories.add("Спорт Залы")
        categories.add("Показать всё")
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = MapFragment()
    }
}
