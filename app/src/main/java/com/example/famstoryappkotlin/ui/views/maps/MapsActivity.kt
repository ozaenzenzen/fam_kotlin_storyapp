package com.example.famstoryappkotlin.ui.views.maps

import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.famgithubuser1.data.retrofit.ApiConfig
import com.example.famstoryappkotlin.R
import com.example.famstoryappkotlin.data.factory.ViewModelFactory
import com.example.famstoryappkotlin.data.local.preferences.UserDataPreferences
import com.example.famstoryappkotlin.data.local.preferences.dataStore
import com.example.famstoryappkotlin.data.repository.AuthRepository
import com.example.famstoryappkotlin.data.repository.StoryRepository
import com.example.famstoryappkotlin.data.response.DetailStoryResponseModel
import com.example.famstoryappkotlin.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var binding: ActivityMapsBinding

    private var dataDetailStory: DetailStoryResponseModel? = null

    private lateinit var viewModel: MapsViewModel

    private lateinit var authRepository: AuthRepository
    private lateinit var storyRepository: StoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataDetailStory = intent.getParcelableExtra<DetailStoryResponseModel>("dataDetail") ?: null

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupViewModel()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setToolbar("Maps Screen")
    }

    private fun setupViewModel() {
        var pref = UserDataPreferences.getInstance(application.dataStore)

        var apiService = ApiConfig.getApiService()

        authRepository = AuthRepository(apiService, pref)
        storyRepository = StoryRepository(apiService)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(authRepository, storyRepository)
        ).get(MapsViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }

    private fun setupToolbar() {
        // Create the Toolbar
        val linearLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val toolbar = Toolbar(this).apply {
            id = R.id.toolbar
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams = Toolbar.LayoutParams(
                Toolbar.LayoutParams.MATCH_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT
            )
            setBackgroundColor(ContextCompat.getColor(this@MapsActivity, R.color.purple_500))
            title = "My Toolbar"
            setTitleTextColor(ContextCompat.getColor(this@MapsActivity, R.color.white))
        }

        // Add the Toolbar to the LinearLayout
        linearLayout.addView(toolbar)

        // Set the LinearLayout as the content view
        setContentView(linearLayout)

        // Set the Toolbar as the ActionBar
        setSupportActionBar(toolbar)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6f))
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.please_activate_location_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.map_style
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun showMarkerLocation() {
        lifecycleScope.launch {
            launch {
                viewModel.getAuthenticationToken().collect() { token ->
                    viewModel.getAllStoryWithLoc(token ?: "").collect { response ->
                        response.onSuccess { data ->
                            data.listStory.forEach { storyItem ->
                                if (storyItem.lat != null && storyItem.lon != null) {
                                    val latLng = LatLng(
                                        storyItem.lat.toString().toDouble(),
                                        storyItem.lon.toString().toDouble()
                                    )
                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(latLng)
                                            .title(storyItem.name)
                                            .snippet("Lat: ${storyItem.lat}, Long: ${storyItem.lon}")
                                    )
                                }
                            }
                        }
                        response.onFailure { }
                    }
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setMapStyle()

        if (dataDetailStory != null) {
            val dicodingSpace = LatLng(
                dataDetailStory?.story?.lat.toString().toDouble(),
                dataDetailStory?.story?.lon.toString().toDouble()
            )
            mMap.addMarker(
                MarkerOptions()
                    .position(dicodingSpace)
                    .title("The Location")
                    .snippet("Lat: ${dataDetailStory?.story?.lat} Long: ${dataDetailStory?.story?.lon}")
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))
        } else {
            getMyLocation()
            showMarkerLocation()
        }
    }

    companion object {
        const val TAG = "MapsActivity"
    }
}