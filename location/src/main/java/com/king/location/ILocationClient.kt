package com.king.location


/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
interface ILocationClient {

    /**
     * 设置定位配置参数
     */
    fun setLocationOption(locationOption: LocationOption)

    /**
     * 获取定位配置参数
     */
    fun getLocationOption(): LocationOption

    /**
     * 开始定位
     */
    fun startLocation()

    /**
     * 停止定位
     */
    fun stopLocation()

    /**
     * 是否已经开始定位
     */
    fun isStarted(): Boolean

    /**
     * 设置定位监听器
     */
    fun setOnLocationListener(listener: OnLocationListener?)

    /**
     * 设置异常监听
     */
    fun setOnExceptionListener(listener: OnExceptionListener?)


}