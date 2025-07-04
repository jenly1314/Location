package com.king.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
class LocationClient(context: Context) : ILocationClient {

    private val mContext: Context = context.applicationContext

    private val mLocationManager by lazy {
        mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private var mLocationOption = LocationOption()

    private var mOnLocationListener: OnLocationListener? = null

    private var mOnExceptionListener: OnExceptionListener? = null

    private var mErrorCode = LocationErrorCode.UNKNOWN_EXCEPTION

    /**
     * 位置监听器
     */
    private val mLocationListener by lazy {
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                mOnLocationListener?.onLocationChanged(location)
                if(mLocationOption.isOnceLocation){//如果只定位一次，则自动停止定位
                    stopLocation()
                }
            }

            override fun onProviderEnabled(provider: String) {
                mOnLocationListener?.onProviderEnabled(provider)
            }

            override fun onProviderDisabled(provider: String) {
                mOnLocationListener?.onProviderDisabled(provider)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                mOnLocationListener?.onStatusChanged(provider,status,extras)
            }
        }
    }

    /**
     * 是否已经开始定位
     */
    private var isStarted = false

    companion object{
        internal const val TAG = "LocationClient"
    }


    override fun setLocationOption(locationOption: LocationOption) {
       this.mLocationOption = locationOption
    }

    override fun getLocationOption(): LocationOption {
        return mLocationOption
    }

    override fun startLocation() {
        try{
            if(isStarted){//如果已经开始定位，则直接拦截
                Log.d(TAG,"isStarted = $isStarted")
                return
            }
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mErrorCode = LocationErrorCode.PERMISSION_EXCEPTION
                //没有获取位置信息的权限
                throw SecurityException("Requires ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission.")
            }

            //如果提供者为空未指定，则获取最佳的提供者
            val provider = mLocationOption.mProvider ?: mLocationManager.getBestProvider(mLocationOption.criteria,true)

            Log.d(TAG,"provider: $provider")

            if(provider == null){
                mErrorCode = LocationErrorCode.PROVIDER_EXCEPTION
                throw IllegalArgumentException("Provider is null or doesn't exist.")
            }

            if(mLocationOption.isLastKnownLocation){
                Log.d(TAG,"Get last known location.")
                val location = mLocationManager.getLastKnownLocation(provider)
                if(location != null){
                    mOnLocationListener?.onLocationChanged(location)
                    if(mLocationOption.isOnceLocation){//如果只定位一次，则直接拦截
                        return
                    }
                }
            }

            //监听位置更新
            mLocationManager.requestLocationUpdates(provider, mLocationOption.minTime, mLocationOption.minDistance, mLocationListener)
            isStarted = true
            Log.d(TAG,"Start location")
        }catch (e: Exception){
            mOnExceptionListener?.onException(mErrorCode, e)
            e.printStackTrace()
        }

    }

    override fun stopLocation() {
        try{
            if(!isStarted){//如果未开始定位，则直接拦截
                Log.d(TAG,"isStarted = $isStarted")
                return
            }
            mLocationManager.removeUpdates(mLocationListener)
            isStarted = false
            Log.d(TAG,"Stop location")
        }catch (e: Exception){
            mErrorCode = LocationErrorCode.UNKNOWN_EXCEPTION
            mOnExceptionListener?.onException(mErrorCode, e)
            e.printStackTrace()
        }
    }

    override fun isStarted(): Boolean {
       return isStarted
    }

    override fun setOnLocationListener(listener: OnLocationListener?) {
        mOnLocationListener = listener
    }

    override fun setOnExceptionListener(listener: OnExceptionListener?) {
        mOnExceptionListener = listener
    }

    /**
     * 对外提供获取 [LocationManager]
     */
    fun getLocationManager(): LocationManager {
        return mLocationManager
    }
}
