package com.example.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale

class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun fetchCurrentLocation(onLocationReceived: (String) -> Unit, onError: (String) -> Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val formatted = formatCoordinates(location.latitude, location.longitude)
                    onLocationReceived(formatted)
                } else {
                    // Request a single fresh update
                    requestFreshLocation(onLocationReceived, onError)
                }
            }.addOnFailureListener {
                fallbackToLocationManager(onLocationReceived, onError)
            }
        } catch (e: Exception) {
            fallbackToLocationManager(onLocationReceived, onError)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestFreshLocation(onLocationReceived: (String) -> Unit, onError: (String) -> Unit) {
        try {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMaxUpdates(1)
                .build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val location = result.lastLocation
                    if (location != null) {
                        onLocationReceived(formatCoordinates(location.latitude, location.longitude))
                    } else {
                        fallbackToLocationManager(onLocationReceived, onError)
                    }
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                callback,
                Looper.getMainLooper()
            )
        } catch (e: Exception) {
            fallbackToLocationManager(onLocationReceived, onError)
        }
    }

    @SuppressLint("MissingPermission")
    private fun fallbackToLocationManager(onLocationReceived: (String) -> Unit, onError: (String) -> Unit) {
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            if (locationManager == null) {
                onError("Location service unavailable")
                return
            }

            val gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val netLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val bestLoc = gpsLoc ?: netLoc

            if (bestLoc != null) {
                onLocationReceived(formatCoordinates(bestLoc.latitude, bestLoc.longitude))
            } else {
                // Try requesting single update
                val listener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        onLocationReceived(formatCoordinates(location.latitude, location.longitude))
                        locationManager.removeUpdates(this)
                    }
                    @Deprecated("Deprecated in Java")
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, Looper.getMainLooper())
                } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, Looper.getMainLooper())
                } else {
                    onError("Please turn on GPS / Location in device settings")
                }
            }
        } catch (e: Exception) {
            onError("Could not get GPS location: ${e.message}")
        }
    }

    private fun formatCoordinates(lat: Double, lng: Double): String {
        val latDir = if (lat >= 0) "N" else "S"
        val lngDir = if (lng >= 0) "E" else "W"
        return String.format(Locale.US, "%.5f° %s, %.5f° %s", Math.abs(lat), latDir, Math.abs(lng), lngDir)
    }
}
