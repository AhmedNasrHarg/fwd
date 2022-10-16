package com.udacity.project4.locationreminders.savereminder.selectreminderlocation


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragmentDirections
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject
import android.location.Geocoder
import android.location.Location
import android.widget.Button
import java.util.*


class SelectLocationFragment : BaseFragment() , OnMapReadyCallback{

    //Use Koin to get the view model of the SaveReminder
    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSelectLocationBinding
     lateinit var map: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_select_location, container, false)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)

//        TODO: add the map setup implementation
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
//        TODO: zoom to the user location after taking his permission
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
//        TODO: add style to the map
          //DONE
//        TODO: put a marker to location that the user selected
          //DONE
//        TODO: call this function after the user confirms on the selected location
//        onLocationSelected()DONE

        return binding.root
    }

    private fun onLocationSelected() {
        //        TODO: When the user confirms on the selected location,
        //         send back the selected location details to the view model
        //         and navigate back to the previous fragment to save the reminder and add the geofence
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // TODO: Change the map type based on the user's selection.
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        Toast.makeText(requireContext(),"Please select reminder location or POI!",Toast.LENGTH_SHORT).show()

        map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.map_style
            )
        )
        enableMyLocation()


        mFusedLocationClient.lastLocation.addOnCompleteListener(activity!!) { task ->
            val location: Location? = task.result
            if (location != null) {
                val currentLocation = LatLng(location.latitude,location.longitude)
//                Toast.makeText(context,currentLocation.toString(), Toast.LENGTH_SHORT).show()
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13f))
//                Toast.makeText(context,currentLocation.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
            poiMarker.showInfoWindow()
            //todo: return latLng to save reminder fragment
            //call onLocationSelected()
            binding.saveLocationBtn.setOnClickListener {
                _viewModel.selectedPOI.value = poi
                _viewModel.reminderSelectedLocationStr.value = poi.name
                _viewModel.latitude.value = poi.latLng.latitude
                _viewModel.longitude.value = poi.latLng.longitude
                //todo: navigate back b2aaa
                _viewModel.navigationCommand.value =
                    NavigationCommand.To(SelectLocationFragmentDirections.actionSelectLocationFragmentToSaveReminderFragment())
            }
        }
        map.setOnMapLongClickListener { latLng ->
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            val cityName: String = addresses[0].getAddressLine(0)
//            val stateName: String = addresses[0].getAddressLine(1)
//            val countryName: String = addresses[0].getAddressLine(2)

            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("$cityName")
            )
            //todo: return latLng to save reminder fragment
            //call onLocationSelected()
            binding.saveLocationBtn.setOnClickListener {
                _viewModel.latitude.value = latLng.latitude
                _viewModel.longitude.value = latLng.longitude
                _viewModel.reminderSelectedLocationStr.value = "$cityName"
                //todo: navigate back b2aaa
                _viewModel.navigationCommand.value =
                    NavigationCommand.To(SelectLocationFragmentDirections.actionSelectLocationFragmentToSaveReminderFragment())
            }
        }
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED
    }
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                requestPermissions(
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map.setMyLocationEnabled(true)
        }
        else {
            Toast.makeText(context,"Permission required to proceed!",Toast.LENGTH_SHORT).show()
            this.requestPermissions(arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            _viewModel.navigationCommand.value =
                NavigationCommand.To(SelectLocationFragmentDirections.actionSelectLocationFragmentToSaveReminderFragment())

        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

}
