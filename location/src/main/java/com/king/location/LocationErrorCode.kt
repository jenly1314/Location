package com.king.location

import androidx.annotation.IntDef

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@IntDef(LocationErrorCode.UNKNOWN_EXCEPTION, LocationErrorCode.PERMISSION_EXCEPTION, LocationErrorCode.PROVIDER_EXCEPTION)
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class LocationErrorCode {

    companion object{

        /**
         * 未知异常
         */
        const val UNKNOWN_EXCEPTION = 1001
        /**
         * 权限异常（没有获取位置信息的权限）
         */
        const val PERMISSION_EXCEPTION = 1002

        /**
         * Provider异常 (出现此问题一般是：位置信息总开关是关闭状态)
         */
        const val PROVIDER_EXCEPTION = 1003

    }
}
