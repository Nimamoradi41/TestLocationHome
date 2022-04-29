package com.example.testlocationhome

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity



fun  RunDia(builder: AlertDialog.Builder,c:Activity)
{
    builder!!.setMessage("برای ادامه کار با اپلیکیشن لطفا موقعیت مکانی خود را روشن کنید")
        .setCancelable(false)
        .setPositiveButton("بله") { dialog, id ->
            var anyLocationProv = false
            val locationManager =  c.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            anyLocationProv = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!anyLocationProv)
            {
             RunDia(builder,c)
            }else{
                c.finish()
            }
        }
        .setNegativeButton("خیر") { dialog, id -> //  Action for 'NO' Button
            dialog.cancel()
                c.finish()
                c.finishAffinity()
        }

    val alert = builder!!.create()

    alert.setTitle("مجوز دسترسی")
    alert.show()
}
class MainActivity2 : AppCompatActivity() {
    var builder: AlertDialog.Builder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        builder = AlertDialog.Builder(this,R.style.AlertDialogCustom)

        RunDia(builder!!,this)
    }
}