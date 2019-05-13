package com.nsv.interestingplaces.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nsv.interestingplaces.model.Item
import com.nsv.interestingplaces.viewModel.MainVM
import com.nsv.interestingplaces.viewModel.MainVmFactory
import android.support.v4.view.ViewPager
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.nsv.interestingplaces.pagerAdapter.ScreenSlidePagerAdapter
import com.google.android.gms.maps.model.Marker
import com.nsv.interestingplaces.utils.HelpingUtils
import com.nsv.interestingplaces.R
import com.nsv.interestingplaces.constant.Constant.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.nsv.interestingplaces.interfaces.MakeRoute
import com.nsv.interestingplaces.pagerAdapter.WrapContentViewPager
import java.text.SimpleDateFormat
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MakeRoute, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var model : MainVM
    private lateinit var mPager: WrapContentViewPager
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mHashMap: HashMap<Int, Marker> = HashMap()
    private val helper: HelpingUtils = HelpingUtils()
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(com.nsv.interestingplaces.R.layout.main_activity)
        requestPermission()
        initUI()
    }

    private fun initUI(){
        mPager = findViewById(com.nsv.interestingplaces.R.id.pager)
        btnBack = findViewById(R.id.btn_back)
        btnBack.setOnClickListener { finish() }
//        mPager.setPageTransformer(true, DepthPageTransformer())
    }

    private fun initViewModel(lat: Double, lng: Double){
        val latLon = "$lat,$lng"
        val date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        Log.i("Date+LatLon", "$date $latLon")
        model = ViewModelProviders.of(this, MainVmFactory(latLon, date)).get(MainVM::class.java)
        observeForPlaces()
    }

    private fun observeForPlaces(){
        model.getPlacesMap().observe(this, Observer<List<Item>> { places ->
            if (places != null){
                initAdapter(places)
                setAllPlaces(places)
            } else {
                Log.i("OBSERVER", "places empty or error")
            }
        })
    }

    private fun initAdapter(places: List<Item>){
        val fm = supportFragmentManager
        val pagerAdapter = ScreenSlidePagerAdapter(fm, places)
        mPager.adapter = pagerAdapter
        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                val marker = mHashMap[p0]
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker?.position))
                marker?.showInfoWindow()
            }
        })
    }

    private fun setAllPlaces(places: List<Item>){
        var i = 0
        for (place in places){
            val location = LatLng(place.venue.location.lat, place.venue.location.lng)
            val title = place.venue.name

            val marker = mMap.addMarker(MarkerOptions().position(location).title(title).icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_BLUE)))
            mHashMap[i] = marker
            i += 1
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(currentLatLng).title(getString(com.nsv.interestingplaces.R.string.map_activity_your_placement))).showInfoWindow()
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                initViewModel(location.latitude, location.longitude)
            } else{
                Toast.makeText(this, getString(R.string.toast_no_location), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestPermission(){
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            val mapFragment = supportFragmentManager
                .findFragmentById(com.nsv.interestingplaces.R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            val mapFragment = supportFragmentManager
                .findFragmentById(com.nsv.interestingplaces.R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }

    override fun onBackPressed() {
        if (mPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            mPager.currentItem = mPager.currentItem - 1
        }
    }

    override fun makeRoute() {
        val marker = mHashMap[mPager.currentItem]
        val lat = marker?.position?.latitude
        val lon = marker?.position?.longitude

        val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lon")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
        startActivity(mapIntent)
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        p0?.showInfoWindow()
        val index = helper.getKey(mHashMap, p0)
        if (index != null){
        mPager.currentItem = index}
        return false
    }
}

