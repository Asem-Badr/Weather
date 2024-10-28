package com.example.weather
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var currentMarker: Marker? = null // To keep track of the current marker

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myLocationManager = MyLocationManager(this)
        // Set up the OSMDroid configuration
        Configuration.getInstance().load(this, androidx.preference.PreferenceManager.getDefaultSharedPreferences(this))

        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.map)
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Set initial zoom level and center point
        val initialLocation = GeoPoint(30.0, 30.0) // Example: San Francisco
        mapView.controller.setZoom(6.0) // Set your desired zoom level
        mapView.controller.setCenter(initialLocation) // Center the map on the initial location

        // Set a touch listener for the map
        mapView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val geoPoint = mapView.projection.fromPixels(event.x.toInt(), event.y.toInt())
                latitude = geoPoint.latitude
                longitude = geoPoint.longitude

                // Remove the old marker if it exists
                currentMarker?.let {
                    mapView.overlays.remove(it) // Remove the old marker from the map
                }

                // Create and add a new marker
                currentMarker = Marker(mapView).apply {
                    position = geoPoint as GeoPoint?
                    //title = "Pinned Location"
                }
                mapView.overlays.add(currentMarker!!)
                currentMarker?.showInfoWindow() // Show the info window for the new marker

                // Refresh the map to show the new marker
                mapView.invalidate()
            }
            false
        }

        // Button to show coordinates
        findViewById<Button>(R.id.show_coordinates_button).setOnClickListener {
            Toast.makeText(this, "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_SHORT).show()
            myLocationManager.setLongitude(longitude)
            myLocationManager.setLatitude(latitude)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
