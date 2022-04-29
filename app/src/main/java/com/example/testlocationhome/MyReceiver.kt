package com.example.testlocationhome

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.util.Log
import android.widget.Toast


class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent)
    {
        var anyLocationProv = false
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        anyLocationProv = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.i("vmdvkdsvksd", "Location service status$anyLocationProv")
        if (!anyLocationProv)
        {
            var ii=Intent(context,MainActivity2::class.java)
            context.startActivity(ii)
        }

    }
}