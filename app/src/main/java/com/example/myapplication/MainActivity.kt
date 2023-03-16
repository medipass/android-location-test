package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Surface
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : androidx.activity.ComponentActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    private var latitude by mutableStateOf(0.0)
    private var longitude by mutableStateOf(0.0)
    private var lastClickedButton by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get a reference to the LocationManager system service
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LocationButton()
                }
            }
        }
    }

    @Composable
    fun LocationButton() {
        // Get a reference to the current context
        val context = LocalContext.current

        // Define a function to get the device's location using GPS
        fun getLocationUsingGPS() {
            lastClickedButton = "GPS"
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // If permission is not granted, request it
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    123,
                )
            } else {
                // If permission is granted, request location updates from the GPS provider
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0f,
                    this@MainActivity,
                )
            }
        }

        // Define a function to get the device's location using Wi-Fi network
        fun getLocationUsingWifi() {
            println("Getting location using wifi")

            lastClickedButton = "Wi-Fi"
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                println("FINE LOCATION permission not granted")

                // If permission is not granted, request it
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    123,
                )
            } else {
                println("requesting location updates")

                // If permission is granted, request location updates from the network provider
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0f,
                    this@MainActivity,
                )
            }
        }

        // Create two buttons that trigger the getLocationUsingGPS and getLocationUsingWifi functions when clicked
        Column {
            Button(onClick = { getLocationUsingGPS() }) {
                Text("Get Location using GPS")
            }
            Button(onClick = { getLocationUsingWifi() }) {
                Text("Get Location using Wi-Fi")
            }
            Text("Latitude: $latitude, Longitude: $longitude")
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // If the user has granted permission, start requesting location updates again
        if (requestCode == 123 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (lastClickedButton) {
                "GPS" -> locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0f,
                    this,
                )
                "Wi-Fi" -> locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0f,
                    this,
                )
            }
        } else {
            println("Error triggering request permission result. $requestCode - $grantResults")
        }
    }

    override fun onLocationChanged(location: Location) {
        println("LOCATIONAIOTNAIOTN: $location")

        // This method will be called when the user's location changes
        // Update the latitude and longitude values in the state variables
        latitude = location.latitude
        longitude = location.longitude
    }

//    override fun onProviderEnabled(provider: String?) {}
//
//    override fun onProviderDisabled(provider: String?) {}

//    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}
