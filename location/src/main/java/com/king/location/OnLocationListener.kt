package com.king.location

import android.location.LocationListener
import android.os.Bundle

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
abstract class OnLocationListener : LocationListener {

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }


}