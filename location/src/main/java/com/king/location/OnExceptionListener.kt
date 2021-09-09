package com.king.location


/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
interface OnExceptionListener {

    /**
     * 异常时回调此方法
     * [errorCode] -> [LocationErrorCode]
     * [e] -> [Exception]
     */
    fun onException(@LocationErrorCode errorCode: Int, e: Exception)
}