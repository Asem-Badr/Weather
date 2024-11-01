package com.example.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var currentMarker: Marker? = null
    private lateinit var btnAction: Button

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myLocationManager = MyLocationManager(this)
        Configuration.getInstance()
            .load(this, androidx.preference.PreferenceManager.getDefaultSharedPreferences(this))
        val mapType = intent.extras?.getString("type")
        setContentView(R.layout.activity_map)
        btnAction = findViewById(R.id.btnAction)

        mapView = findViewById(R.id.map)
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        val initialLocation = GeoPoint(30.0, 30.0)
        mapView.controller.setZoom(6.0)
        mapView.controller.setCenter(initialLocation)


        mapView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val geoPoint = mapView.projection.fromPixels(event.x.toInt(), event.y.toInt())
                latitude = geoPoint.latitude
                longitude = geoPoint.longitude

                currentMarker?.let {
                    mapView.overlays.remove(it)
                }
                currentMarker = Marker(mapView).apply {
                    position = geoPoint as GeoPoint?
                }
                mapView.overlays.add(currentMarker!!)
                mapView.invalidate()
            }
            false
        }

        btnAction.setOnClickListener {
            if (mapType == "Favorite") {
                myLocationManager.setFavoriteLongitude(longitude)
                myLocationManager.setFavoriteLatitude(latitude)
                finish()
            } else if (mapType == "Map") {
                myLocationManager.setLongitude(longitude)
                myLocationManager.setLatitude(latitude)
                finish()
            }

        }
        if (mapType == "Favorite") {
            btnAction.setText("Add to Favorte")
        } else if (mapType == "Map") {
            btnAction.setText("show Location Weather")
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
