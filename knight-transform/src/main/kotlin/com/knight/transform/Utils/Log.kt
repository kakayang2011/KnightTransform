package com.knight.transform.Utils


import java.io.PrintWriter
import java.io.StringWriter

object Log {

    /**
     * Drawing toolbox
     */
    private val TOP_LEFT_CORNER = '╔'
    private val BOTTOM_LEFT_CORNER = '╚'
    private val MIDDLE_CORNER = '╟'
    private val HORIZONTAL_DOUBLE_LINE = '║'
    private val DOUBLE_DIVIDER = "═════════════════════════════════════════════════"
    private val SINGLE_DIVIDER = "─────────────────────────────────────────────────"
    val TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    val BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    val MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER
    val BR = System.getProperty("line.separator")     // 换行符


    private val debugLog: LogImp = object : LogImp {

        override fun i(tag: String, msg: String) {
            if (msg.contains("\n")) {
                println(String.format(getMethodNames(Log.LogLevel.INFO), tag, msg.replace("\n".toRegex(), "\n║ ")))
            } else {
                println(String.format(getMethodNames(Log.LogLevel.INFO), tag, msg))
            }
        }

        override fun d(tag: String, msg: String) {
            if (msg.contains("\n")) {
                println(String.format(getMethodNames(Log.LogLevel.DEBUG), tag, msg.replace("\n".toRegex(), "\n║ ")))
            } else {
                println(String.format(getMethodNames(Log.LogLevel.DEBUG), tag, msg))
            }
        }

        override fun w(tag: String, msg: String) {
            if (msg.contains("\n")) {
                println(String.format(getMethodNames(Log.LogLevel.WARN), tag, msg.replace("\n".toRegex(), "\n║ ")))
            } else {
                println(String.format(getMethodNames(Log.LogLevel.WARN), tag, msg))
            }
        }

        override fun e(tag: String, msg: String) {
            if (msg.contains("\n")) {
                println(String.format(getMethodNames(Log.LogLevel.ERROR), tag, msg.replace("\n".toRegex(), "\n║ ")))
            } else {
                println(String.format(getMethodNames(Log.LogLevel.ERROR), tag, msg))
            }
        }

        override fun printErrStackTrace(tag: String, tr: Throwable, format: String) {
            var log: String? = format
            if (log == null) {
                log = ""
            }
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            tr.printStackTrace(pw)
            log += "  $sw"
            println(String.format("[ERROR][%s]%s", tag, log))
        }
    }

    var impl: LogImp = debugLog

    var isOpenLog: Boolean = true
    var logLevel: LogLevel = LogLevel.INFO

    @JvmStatic
    fun e(tag: String, msg: String) {
        if (isOpenLog && LogLevel.ERROR.value <= logLevel.value) {
            impl.e(tag, msg)
        }
    }

    @JvmStatic
    fun w(tag: String, msg: String) {
        if (isOpenLog && LogLevel.WARN.value <= logLevel.value) {
            impl.w(tag, msg)
        }
    }

    @JvmStatic
    fun i(tag: String, msg: String) {
        if (isOpenLog && LogLevel.INFO.value <= logLevel.value) {
            impl.i(tag, msg)
        }
    }

    @JvmStatic
    fun d(tag: String, msg: String) {
        if (isOpenLog && LogLevel.DEBUG.value <= logLevel.value) {
            impl.d(tag, msg)
        }
    }

    @JvmStatic
    fun printErrStackTrace(tag: String, tr: Throwable, format: String) {
        impl.printErrStackTrace(tag, tr, format)
    }

    private fun getMethodNames(logLevel: LogLevel): String {
        val builder = StringBuilder()

        builder.append(TOP_BORDER).append(BR)
                .append("║ ").append("[${logLevel.name}][%s]%s").append(BR)
                .append(BOTTOM_BORDER)
        return builder.toString()
    }


    interface LogImp {


        fun i(tag: String, msg: String)

        fun w(tag: String, msg: String)

        fun d(tag: String, msg: String)

        fun e(tag: String, msg: String)

        fun printErrStackTrace(tag: String, tr: Throwable, format: String)

    }

    enum class LogLevel {
        ERROR {
            override val value: Int
                get() = 0
        },
        WARN {
            override val value: Int
                get() = 1
        },
        INFO {
            override val value: Int
                get() = 2
        },
        DEBUG {
            override val value: Int
                get() = 3
        };

        abstract val value: Int
    }
}
