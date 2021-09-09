package com.king.location

import android.location.Criteria
import android.location.LocationManager

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
class LocationOption {

    internal val criteria by lazy {
        Criteria().apply {
            isCostAllowed = true
        }
    }

    /**
     *
     */
    internal var mProvider: String? = null

    /**
     * 位置更新最小时间间隔（单位：毫秒）
     */
    internal var minTime = 10000L

    /**
     * 位置更新最小距离（单位：米）
     */
    internal var minDistance = 0F

    /**
     * 是否只定位一次
     */
    internal var isOnceLocation = false

    /**
     * 是否获取最后一次已知位置
     */
    internal var isLastKnownLocation = true

    /**
     * 设置提供位置的精度需求
     * [accuracy] 支持的值有如下几种：
     * [Criteria.NO_REQUIREMENT]
     * [Criteria.ACCURACY_COARSE]
     * [Criteria.ACCURACY_FINE]
     *
     * 详情请查看：[Criteria.setAccuracy]
     */
    fun setAccuracy(accuracy: Int): LocationOption{
        criteria.accuracy = accuracy
        return this
    }

    /**
     * 设置提供速度的精度需求
     * [speedAccuracy] 支持的值有如下几种：
     * [Criteria.NO_REQUIREMENT]
     * [Criteria.ACCURACY_LOW]
     * [Criteria.ACCURACY_MEDIUM]
     * [Criteria.ACCURACY_HIGH]
     *
     * 详情请查看：[Criteria.setSpeedAccuracy]
     */
    fun setSpeedAccuracy(speedAccuracy: Int): LocationOption{
        criteria.speedAccuracy = speedAccuracy
        return this
    }


    /**
     * 设置提供方位的精度需求
     * [bearingAccuracy] 支持的值有如下几种：
     * [Criteria.NO_REQUIREMENT]
     * [Criteria.ACCURACY_LOW]
     * [Criteria.ACCURACY_MEDIUM]
     * [Criteria.ACCURACY_HIGH]
     *
     * 详情请查看：[Criteria.setBearingAccuracy]
     */
    fun setBearingAccuracy(bearingAccuracy: Int): LocationOption{
        criteria.bearingAccuracy = bearingAccuracy
        return this
    }

    /**
     * 设置电量消耗需求
     * [powerRequirement] 支持的值有如下几种：
     * [Criteria.NO_REQUIREMENT]
     * [Criteria.POWER_LOW]
     * [Criteria.POWER_MEDIUM]
     * [Criteria.POWER_HIGH]
     *
     * 详情请查看：[Criteria.setPowerRequirement]
     */
    fun setPowerRequirement(powerRequirement: Int): LocationOption{
        criteria.powerRequirement = powerRequirement
        return this
    }

    /**
     * 设置是否提供海拔高度信息；默认为 false
     *
     * [Criteria.setAltitudeRequired]
     */
    fun setAltitudeRequired(altitudeRequired: Boolean): LocationOption{
        criteria.isAltitudeRequired = altitudeRequired
        return this
    }

    /**
     * 设置是否提供方位信息；默认为 false
     *
     * 详情请查看：[Criteria.setBearingRequired]
     */
    fun setBearingRequired(bearingRequired: Boolean): LocationOption{
        criteria.isBearingRequired = bearingRequired
        return this
    }

    /**
     * 设置是否提供虚度信息：默认为 false
     *
     * 详情请查看：[Criteria.setSpeedRequired]
     */
    fun setSpeedRequired(speedRequired: Boolean): LocationOption{
        criteria.isSpeedRequired = speedRequired
        return this
    }

    /**
     * 设置是否允许产生费用（使用手机网络来定位，势必会产生流量费用）；默认为 true
     *
     * 详情请查看：[Criteria.setCostAllowed]
     */
    fun setCostAllowed(costAllowed: Boolean): LocationOption{
        criteria.isCostAllowed = costAllowed
        return this
    }

    /**
     * 设置位置信息提供者，如：gps，network等；如果未设置 [provider] 或 [provider] 空的情况下，默认会取当前设备最佳的 provider
     * [LocationManager.GPS_PROVIDER]
     * [LocationManager.NETWORK_PROVIDER]
     *
     * 详情请查看：[LocationManager.getBestProvider]
     */
    fun setProvider(provider: String?): LocationOption{
        mProvider = provider
        return this
    }

    /**
     * 设置位置更新最小时间间隔（单位：毫秒）； 默认间隔：10000毫秒，最小间隔：1000毫秒
     */
    fun setMinTime(minTimeMs : Long): LocationOption{
        minTime = minTimeMs.coerceAtLeast(1000)
        return this
    }

    /**
     * 设置位置更新最小距离（单位：米）；默认距离：0米
     */
    fun setMinDistance(minDistanceM: Int): LocationOption{
        minDistance = minDistanceM.toFloat().coerceAtLeast(0F)
        return this
    }

    /**
     * 设置是否只定位一次，默认为 false，当设置为 true 时，则只定位一次后，会自动停止定位
     */
    fun setOnceLocation(onceLocation: Boolean): LocationOption{
        isOnceLocation = onceLocation
        return this
    }

    /**
     * 设置是否获取最后一次缓存的已知位置，默认为 true
     *
     * 详情请查看：[LocationManager.getLastKnownLocation]
     */
    fun setLastKnownLocation(lastKnownLocation: Boolean): LocationOption{
        isLastKnownLocation = lastKnownLocation
        return this
    }

}