package com.example.testlocationhome

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import java.io.File
import java.io.FileWriter


class MyService : Service()  , GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks,
    LocationListener {
    private var mGoogleApiClient: GoogleApiClient? = null
    private val mCurrentLocation: Location? = null
    private var mLocationRequest: LocationRequest? = null



    var activity: Activity?=null
    private val UPDATE_INTERVAL = (1 * 1000 /* 10 secs */).toLong()
    private val FASTEST_INTERVAL: Long = 1000 /* 2 sec */

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
      return  null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        CreateNotif()

        var II=Intent(this,MainActivity::class.java)
        var Ped=PendingIntent.getActivity(this,0,II,0)


        val notification: Notification = NotificationCompat.Builder(this, "ChannelId")
            .setContentTitle("سرویس رهیاب")
            .setContentText("").build()



//        startForeground(3, notification)
//        var notiff=NotificationCompat.Builder(
//            this,
//            "ChannelId",
//        ).setContentTitle("My App Tuturial")
//            .setContentText("Our App Is Running")
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentIntent(Ped).build()
        startForeground(1,notification)


        startLocationUpdates()


        Toast.makeText(this@MyService,"locationResult.lastLocation.latitude.toString()",Toast.LENGTH_SHORT).show()






        return START_REDELIVER_INTENT
    }


    fun generateNoteOnSD(context: Context?, sBody: String?) {
        val dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Nimaaaa"
        );


        if (!dir.exists()) {
            dir.mkdirs();
            Log.i("ddvdvdvdvdvdv","AAA")
        }else{
            Log.i("ddvdvdvdvdvdv","BBBB")
        }





        try {
            var fff=File(dir,"text.txt")
            var fe=FileWriter(fff)
            fe.append(sBody)
            fe.flush()
            fe.close()
            Toast.makeText(context,"Saved",Toast.LENGTH_SHORT).show()
        }catch (E:Exception)
        {
           Log.i("wetwey",E.message.toString())
        }


    }


    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient?.connect()
    }
    protected fun startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest()
        mLocationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest?.setInterval(UPDATE_INTERVAL)
        mLocationRequest?.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)
      var ff=  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }



        if (mGoogleApiClient == null) {
            //The method that will initialize and return LocationRequest:
            buildGoogleApiClient()
            getLocationRequest(mGoogleApiClient!!)

        }
//        getFusedLocationProviderClient(this).requestLocationUpdates(
//            mLocationRequest!!, object : LocationCallback() {
//                override fun onLocationResult(locationResult: LocationResult) {
//                    var loc=locationResult.lastLocation
//                    val locationA = Location("point A")
//                    locationA.setLatitude(31.3170938)
//                    locationA.setLongitude(48.644835)
//                    val locationB = Location("point B")
//                    locationB.latitude = loc.latitude
//                    locationB.longitude = loc.longitude
//                    Log.i("scacacaca",locationB.latitude.toString())
//                    Log.i("scacacaca",locationB.longitude.toString())
//                    val result = FloatArray(1)
//                    Location.distanceBetween(
//                        locationA.latitude,
//                        locationA.longitude,
//                        locationB.latitude, locationB.longitude,result
//                    )
//                 var   distance = locationA.distanceTo(locationB)
//                    Toast.makeText(this@MyService,distance.toString(),Toast.LENGTH_SHORT).show()
////                    generateNoteOnSD(this@MyService,distance.toString()+"\n")
////                 var   distance = calculateDistance(locationA.latitude,locationA.longitude,locationB.latitude,locationB.longitude)
//                    Log.i("scacacaca",distance.toString())
//                }
//            },
//            Looper.myLooper()!!
//        )
    }
    private fun getLocationRequest(
        mGoogleApiClient: GoogleApiClient
    ): LocationRequest? {

        //  public LocationRequest getLocationRequest(GoogleApiClient mGoogleApiClient, final Activity activity) {
        mLocationRequest = LocationRequest()
        // mLocationRequest.setInterval(SyncStateContract.Constants.GPS_UPDATE_TIME);
        mLocationRequest!!.smallestDisplacement = 5f
        mLocationRequest!!.fastestInterval = 3000
//        mLocationRequest!!.interval = (ferequencyTime * 1000).toLong()
        mLocationRequest!!.interval = 2000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        // mLocationRequest.setSmallestDisplacement(SyncStateContract.Constants.SMALLEST_DISTANCE);
        mLocationRequest!!.maxWaitTime = 10000

        // ezafeee baraye permission
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest!!)
        builder.setAlwaysShow(true)
        val result =
            LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build())
        result.setResultCallback { result ->
            Log.i("hjxcfgxfghxdfhxdh", "herer")
            val status = result.status
            val state = result.locationSettingsStates
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@setResultCallback
            }
            when (status.statusCode) {
                LocationSettingsStatusCodes.DEVELOPER_ERROR, LocationSettingsStatusCodes.SUCCESS -> LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest!!,
                    (this as LocationListener?)!!
                )
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                         // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
//                        // Show the dialog by calling startResolutionForResult(),
//                        // and check the result in onActivityResult().
                        if (activity != null) status.startResolutionForResult(activity!!, 8325)
                    } catch (e: SendIntentException) {
                        // Ignore the error.
                        e.printStackTrace()
                    }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        }
        //
        return mLocationRequest
    }












    override fun onDestroy() {
        stopForeground(true)
        stopSelf()
        super.onDestroy()
    }
    private fun CreateNotif() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            var notifchannel=NotificationChannel(
                "ChannelId","ForgroundNotif",NotificationManager.IMPORTANCE_DEFAULT
            )

            var manger=getSystemService(NotificationManager::class.java)
            manger.createNotificationChannel(notifchannel)
        }
    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onLocationChanged(p0: Location) {
        Toast.makeText(this,p0.toString(),Toast.LENGTH_SHORT).show()

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }


}