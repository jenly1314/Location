package com.king.location.app

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.king.location.*
import com.king.location.app.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val locationClient by lazy {
        LocationClient(this)
    }

    private val geocoder by lazy {
        Geocoder(this)
    }

    private val simpleDateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    }

    private var isPermissionGranted = false

    companion object{
        const val TAG = "MainActivity"
        const val REQ_LOCATION_PERMISSION = 0x01
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnStartLocation.setOnClickListener {
            isPermissionGranted = checkLocationPermission()
            if(isPermissionGranted){
                startLocation()
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQ_LOCATION_PERMISSION)
            }
        }

        binding.btnStopLocation.setOnClickListener {
            locationClient.stopLocation()
            binding.tvLocation.text = ""
        }

        binding.cbOnceLocation.setOnCheckedChangeListener { buttonView, isChecked ->
            locationClient.getLocationOption().setOnceLocation(isChecked)
        }

        binding.cbLastKnownLocation.setOnCheckedChangeListener { buttonView, isChecked ->
            locationClient.getLocationOption().setLastKnownLocation(isChecked)
        }

        //可根据具体需求设置定位配置参数（这里只列出一些主要的参数）
        val locationOption = locationClient.getLocationOption()
            .setAccuracy(Criteria.ACCURACY_FINE)//设置位置精度：高精度
            .setPowerRequirement(Criteria.POWER_LOW) //设置电量消耗：低电耗
            .setMinTime(10000)//设置位置更新最小时间间隔（单位：毫秒）； 默认间隔：10000毫秒，最小间隔：1000毫秒
            .setMinDistance(0)//设置位置更新最小距离（单位：米）；默认距离：0米
            .setOnceLocation(false)//设置是否只定位一次，默认为 false，当设置为 true 时，则只定位一次后，会自动停止定位
            .setLastKnownLocation(true)//设置是否获取最后一次缓存的已知位置，默认为 true
        //设置定位配置参数
        locationClient.setLocationOption(locationOption)

        //设置定位监听
        locationClient.setOnLocationListener(object: OnLocationListener(){
            override fun onLocationChanged(location: Location) {
                //位置信息
                Log.d(TAG,"onLocationChanged(location = ${location})")
                val builder = StringBuilder()
                builder.append("Longitude: \t${location.longitude}\n")
                    .append("Latitude: \t${location.latitude}\n")

                //根据坐标经纬度获取位置地址信息（WGS-84坐标系）
                val list = geocoder.getFromLocation(location.latitude,location.longitude,1)
                if(list.isNotEmpty()){
                    builder.append("Address: \t${list[0].getAddressLine(0)}\n")
                }

                builder.append("Time: \t${simpleDateFormat.format(Date(location.time))}\n")
                    .append("Provider: \t${location.provider}\n")

                binding.tvLocation.text = builder.toString()
            }

            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
                Log.d(TAG,"onProviderEnabled(provider = ${provider})")
            }

            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
                Log.d(TAG,"onProviderDisabled(provider = ${provider})")
            }

        })

        //设置异常监听
        locationClient.setOnExceptionListener(object : OnExceptionListener{
            override fun onException(@LocationErrorCode errorCode: Int, e: Exception) {
                //定位出现异常
                Log.w(TAG,"onException(errorCode = ${errorCode}, e = ${e})")
                binding.tvLocation.text = e.message
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if(!isPermissionGranted){
            if(checkLocationPermission()){
                isPermissionGranted = true
                binding.tvLocation.text = ""
            }
        }
    }

    /**
     * 开始定位
     */
    private fun startLocation(){
        if(locationClient.isStarted()){//如果已经开始定位，则先停止定位
            locationClient.stopLocation()
        }
        binding.tvLocation.text = "Start location..."
        locationClient.startLocation()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQ_LOCATION_PERMISSION){
            isPermissionGranted = verifyPermissions(grantResults)
            if(isPermissionGranted){//已授权
                startLocation()
            }else{//未授权
                binding.tvLocation.text = "Location permission has not been granted."
                showMissingPermissionDialog()
            }
        }
    }

    /**
     * 检测位置权限
     */
    private fun checkLocationPermission(): Boolean{
        return ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 校验授权结果
     */
    private fun verifyPermissions(grantResults: IntArray): Boolean{
        for(result in grantResults){
            if(result != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }

    /**
     * 显示未授权提示对话框
     */
    private fun showMissingPermissionDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Location permission has not been granted.")
            .setNegativeButton("Cancel"
            ) { dialog, which ->

            }
            .setPositiveButton("Setting"
            ) { dialog, which ->
                startAppSettings()
            }
        builder.show()
    }


    /**
     * 跳转到 App 的设置详情界面
     */
    private fun startAppSettings(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${packageName}")
        startActivity(intent)
    }


}