package com.example.testlocationhome

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {

    lateinit var receiver: MyReceiver
    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var anyLocationProv = false
        val locationManager =  getSystemService(Context.LOCATION_SERVICE) as LocationManager
        anyLocationProv = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        receiver = MyReceiver()


            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),2)


        if (!anyLocationProv)
        {
            var ii=Intent(this,MainActivity2::class.java)
           startActivity(ii)
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            != PackageManager.PERMISSION_GRANTED &&

            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(
                    this,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("dvdvdvddv","BB")
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }else{
            if (isMyServiceRunning(MyService::class.java))
            {
                Toast.makeText(this,"IS Running",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"IS Stoped",Toast.LENGTH_SHORT).show()
                var ss=Intent(this,MyService::class.java)
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
                {
                    startForegroundService(ss)
                }else{
                    startService(ss)
                }
            }
        }




    }
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}