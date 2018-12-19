package com.knight.doublecheck.library

object DoubleCheckTool {
    private const val CLICK_DURING_MILLIS = 500L * 1000000L
    private var lastClickTime = 0L

    @JvmStatic
    fun isClickable(): Boolean {
        val now = System.nanoTime()
        val last = lastClickTime
        if (last == 0L || ((now - last) > CLICK_DURING_MILLIS)) {
            lastClickTime = System.nanoTime()

            return true
        } else {
            return false
        }
    }
}