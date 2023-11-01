package com.syarif.aidex_cgm

import android.content.Context
import com.microtechmd.cgmlib.constants.ErrorCode

object ErrorUtils {
    fun getErrorMsg(context: Context, code: Int): String {
        when (code) {
            ErrorCode.ERROR_OPERATION -> return context.resources.getString(R.string.error_1)
            ErrorCode.ERROR_DEVICE_MISS -> return context.resources.getString(R.string.error_2)
            ErrorCode.ERROR_CONNECT_ERROR -> return context.resources.getString(R.string.error_3)
            ErrorCode.ERROR_MESSAGE_TIMEOUT -> return context.resources.getString(R.string.error_4)
            ErrorCode.ERROR_BLUETOOTH_CLOSE -> return context.resources.getString(R.string.error_5)
            ErrorCode.ERROR_BLE_NOPEMISSION -> return context.resources.getString(R.string.error_6)
            ErrorCode.ERROR_CALIBRATION_TIME -> return context.resources.getString(R.string.error_7)
            ErrorCode.ERROR_NETWORK_UNAVAILABLE -> return context.resources.getString(R.string.error_9)
            ErrorCode.ERROR_USER_PAIRING -> return context.resources.getString(R.string.error_10)
            ErrorCode.ERROR_USER_UNPAIR -> return context.resources.getString(R.string.error_11)
            ErrorCode.ERROR_DEVICE_SN -> return context.resources.getString(R.string.error_14)
            ErrorCode.ERROR_TASK_CLOSE -> return context.resources.getString(R.string.error_12)
            ErrorCode.ERROR_INIT -> return context.resources.getString(R.string.error_13)
            ErrorCode.ERROR_PAIR_INVALID -> return context.resources.getString(R.string.error_18) //配对已失效，请强制删除后重新配对
            ErrorCode.ERROR_SN -> return context.resources.getString(R.string.error_17)
            ErrorCode.ERROR_LOCATION -> return context.resources.getString(R.string.error_16)
            ErrorCode.ERROR_SECRET -> return context.resources.getString(R.string.error_8)
            ErrorCode.ERROR_OTA_ERROR, ErrorCode.ERROR_OTA_DFU_ERROR -> return context.resources.getString(
                R.string.content_ota_error
            )

            else -> {}
        }
        return ""
    }
}
