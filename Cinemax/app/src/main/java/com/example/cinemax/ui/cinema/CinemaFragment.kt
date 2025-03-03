package com.example.cinemax.ui.cinema

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.cinemax.databinding.FragmentCinemaBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CinemaFragment : Fragment(), OnMapReadyCallback {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var _binding: FragmentCinemaBinding? = null

    private val binding get() = _binding!!
    private lateinit var googleMap: GoogleMap
    private lateinit var mapView: MapView
    private val markersMap = mutableMapOf<String, Marker?>()

    private val cinemaLocations = arrayListOf(
        "ATRIUM MALL, Arad, 46.19141, 21.32034",
        "ARENA MALL, Bacau, 46.57810, 26.91527",
        "VIVO!, Baia Mare, 47.66112, 23.54729",
        "BRAILA MALL, Braila, 45.22811, 27.94015",
        "AFI BRASOV, Brasov, 45.65369, 25.60831",
        "AFI COTROCENI, Bucuresti, 44.43085, 26.05250",
        "MEGA MALL, Bucuresti, 44.44192, 26.15227",
        "PARKLAKE MALL, Bucuresti, 44.42084, 26.15010",
        "SUN PLAZA, Bucuresti, 44.39531, 26.12266",
        "SHOPPING CITY BUZAU, Buzau, 45.16320, 26.81381",
        "IULIUS MALL, Cluj, 46.78080, 23.62271",
        "VIVO!, Cluj, 46.73810, 23.51668",
        "CITY PARK MALL, Constanta, 44.20425, 28.63368",
        "VIVO!, Constanta, 44.20982, 28.60780",
        "SHOPPING CITY DEVA, Deva, 45.88157, 22.93149",
        "SEVERIN SHOPPING CENTER, Drobeta-Turnu Severin, 44.64024, 22.65403",
        "SHOPPING CITY GALATI, Galati, 45.45540, 28.01943",
        "IULIUS MALL, Iasi, 47.16009, 27.61834",
        "SHOPPING CITY PIATRA NEAMT, Piatra Neamt, 46.93646, 26.34149",
        "MALL VIVO! PITESTI, Pitesti, 44.87671, 24.88928",
        "AFI PLOIESTI, Ploiesti, 44.94582, 26.03406",
        "PLOIESTI SHOPPING CITY, Ploiesti, 44.97322, 25.97191",
        "SHOPPING CITY RAMNICU VALCEA, Ramnicu Valcea, 45.12111, 24.38883",
        "IULIUS MALL SUCEAVA, Suceava, 47.66696, 26.26264",
        "SHOPPING CITY TARGU-JIU, Targu Jiu, 45.03002, 23.25664",
        "PLAZZA M, Targu Mures, 46.52391, 24.50091",
        "IULIUS TOWN, Timisoara, 45.81144, 21.25266",
        "SHOPPING CITY TIMISOARA, Timisoara, 45.75014, 21.23954"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCinemaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.btnGetLocation.setOnClickListener { fetchLocation() }

        return root
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                googleMap.isMyLocationEnabled = true
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 12.0f))
                displayNearbyCinemas(it.latitude, it.longitude)
            }
        }
    }

    private fun displayNearbyCinemas(latitude: Double, longitude: Double) {
        val cinemaDistances = cinemaLocations.map {
            val details = it.split(", ")
            val cinemaLat = details[2].toDouble()
            val cinemaLon = details[3].toDouble()
            val distance = calculateDistance(latitude, longitude, cinemaLat, cinemaLon)
            Triple(it, distance, LatLng(cinemaLat, cinemaLon))
        }.sortedBy { it.second }.take(4)

        val nearbyCinemas = cinemaDistances.map { it.first }
        val cinemaAdapter = CinemaAdapter(requireContext(), nearbyCinemas)
        binding.cinemaListView.adapter = cinemaAdapter

        binding.cinemaListView.setOnItemClickListener { _, _, position, _ ->
            val selectedCinema = cinemaDistances[position]
            toggleMarker(selectedCinema.third, selectedCinema.first.split(", ")[0])
        }
    }

    private fun toggleMarker(latLng: LatLng, title: String) {
        val existingMarker = markersMap[title]
        if (existingMarker != null) {
            existingMarker.remove()
            markersMap.remove(title)
        } else {
            val marker = googleMap.addMarker(MarkerOptions().position(latLng).title(title))
            markersMap[title] = marker
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLocation()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_LONG).show()
        }
    }
}