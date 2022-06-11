package fr.min.formation.projetandroiddamour_mouquet

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import fr.min.formation.projetandroiddamour_mouquet.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import fr.min.formation.projetandroiddamour_mouquet.PermissionUtils.isPermissionGranted
import fr.min.formation.projetandroiddamour_mouquet.model.StationModel
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class ProjetVelib : AppCompatActivity(),
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private var permissionDenied = false
    private lateinit var map: GoogleMap
    var context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu_favori -> {val intent = Intent(this, Favoris::class.java)
                startActivity(intent)}
            R.id.menu_ajout -> {
                val intent = Intent(this, Ajout_Favoris::class.java)
                startActivity(intent)
}

        R.id.menu_accueil -> {val intent = Intent(this, ProjetVelib::class.java)
            startActivity(intent)}
        }
        return super.onOptionsItemSelected(item)
    }




    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.setOnMyLocationClickListener(this)
        enableMyLocation()
        if (isOnline(context)) {
            synchroAPI(true)
        } else {
            synchroAPI(false)
        }


    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            PermissionUtils.RationaleDialog.newInstance(
                LOCATION_PERMISSION_REQUEST_CODE, true
            ).show(supportFragmentManager, "dialog")
            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
            .show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
            return
        }

        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        newInstance(true).show(supportFragmentManager, "dialog")
    }

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    var Liste_station_globale_nom = ArrayList<String>()
    var Liste_station_globale_capacite = ArrayList<Int>()
    var Liste_station_globale_velDispo = ArrayList<Int>()
    var Liste_station_globale_emplacement = ArrayList<Int>()

    private fun synchroAPI(Internet: Boolean) {
        var db = DataBaseSQLite(context)
        db.creation_table()
        db.delete()

        if (Internet) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://velib-metropole-opendata.smoove.pro/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()

            val service = retrofit.create(InterfaceAPI::class.java)
            val servicestatus = retrofit.create(InterfaceInfoAPI::class.java)

            runBlocking {

                val stations = service.getStations(0)
                val stationsStatus = servicestatus.getStationsStatus(0)

            stations.data.stations.map {
                val (station_id, name, lat, lon, capacity) = it
                val stationid1 = station_id
                val position = LatLng(it.lat, it.lon)
                val latitude = it.lat
                val longitude = it.lon
                val nom = it.name
                val capacite = it.capacity

                stationsStatus.data.stations.map {
                    val (station_id, num_bikes_available, num_docks_available) = it

                    if (it.station_id == stationid1) {
                        map.addMarker(
                            MarkerOptions().position(position).title(nom)
                                .snippet( "Capacité: ${capacite}" + "\n" + "Vélo Disponible : ${it.num_bikes_available}" + "\n" + "Emplacements disponibles : ${it.num_docks_available}")
                                .icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                                )

                        )

                        map.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                            override fun getInfoWindow(arg0: Marker): View? {
                                return null
                            }

                            override fun getInfoContents(marker: Marker): View {
                                val context: Context = applicationContext
                                val info = LinearLayout(context)
                                info.orientation = LinearLayout.VERTICAL
                                val title = TextView(context)
                                title.setTextColor(Color.BLACK)
                                title.gravity = Gravity.CENTER
                                title.setTypeface(null, Typeface.BOLD)
                                title.text = marker.title
                                val snippet = TextView(context)
                                snippet.setTextColor(Color.GRAY)
                                snippet.text = marker.snippet
                                info.addView(title)
                                info.addView(snippet)
                                return info
                            }
                        })

                        var Ajoutstation = StationModel(
                            station_id,
                            nom,
                            capacite,
                            it.num_bikes_available,
                            it.num_docks_available,
                            latitude,
                            longitude
                        )


                        db.insertionDonnee(Ajoutstation)

                        //Partie uptade des favoris qui ne marche pas.
                        /*var stationListRechercheFavoris = db.recupNomStation()
                        if (stationListRechercheFavoris.isNotEmpty())
                            for (index in 0 until stationListRechercheFavoris.size) {

                                if (stationListRechercheFavoris[index]==nom){
                                    val station=StationModel( station_id,
                                     name,
                                     capacity,
                                     num_bikes_available,
                                     num_docks_available,
                                     latitude,
                                     longitude)

                                    db.update(station)
                                }
                            }*/
                    }

                }

            }

            }
        } else {


            var stationList = db.recupFavoris()

            if (stationList.isNotEmpty())
                for (index in 0 until stationList.size) {

                    map.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                stationList[index].latitude,
                                stationList[index].longitude
                            )
                        ).title(stationList[index].nom)
                            .snippet("Capacité: ${stationList[index].capacity}" + "\n" + "Vélo Disponible : ${stationList[index].num_bikes_available}" + "\n" + "Emplacements disponibles : ${stationList[index].num_docks_available}")
                            .icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)
                            )
                    )
                    map.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                        override fun getInfoWindow(arg0: Marker): View? {
                            return null
                        }

                        override fun getInfoContents(marker: Marker): View {
                            val context: Context = applicationContext
                            val info = LinearLayout(context)
                            info.orientation = LinearLayout.VERTICAL
                            val title = TextView(context)
                            title.setTextColor(Color.BLACK)
                            title.gravity = Gravity.CENTER
                            title.setTypeface(null, Typeface.BOLD)
                            title.text = marker.title
                            val snippet = TextView(context)
                            snippet.setTextColor(Color.GRAY)
                            snippet.text = marker.snippet
                            info.addView(title)
                            info.addView(snippet)
                            return info
                        }
                    })

                }


        }








    }
    }
    @SuppressLint("ServiceCast", "MissingPermission")
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }


