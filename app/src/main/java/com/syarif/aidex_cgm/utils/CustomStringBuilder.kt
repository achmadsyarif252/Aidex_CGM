package com.syarif.aidex_cgm.utils


class CustomStringBuilder {
    var stringBuffer = StringBuilder()
    fun append(content: CharSequence?): StringBuilder {
        stringBuffer.append(TimeUtils.getNowTime()).append("  ").append(content).append("\n")
        return stringBuffer
    }

    override fun toString(): String {
        return stringBuffer.toString()
    }
}

