package com.knight.transform.Utils


import java.io.PrintWriter
import java.io.StringWriter

object Log {

    private val debugLog: LogImp = object : LogImp {

        override fun v(tag: String, msg: String, vararg obj: Any) {
            val log = if (obj == null) msg else String.format(msg, *obj)
            println(String.format("[VERBOSE][%s]%s", tag, log))
        }

        override fun i(tag: String, msg: String, vararg obj: Any) {
            val log = if (obj == null) msg else String.format(msg, *obj)
            println(String.format("[INFO][%s]%s", tag, log))
        }

        override fun d(tag: String, msg: String, vararg obj: Any) {
            val log = if (obj == null) msg else String.format(msg, *obj)
            println(String.format("[DEBUG][%s]%s", tag, log))
        }

        override fun w(tag: String, msg: String, vararg obj: Any) {
            val log = if (obj == null) msg else String.format(msg, *obj)
            println(String.format("[WARN][%s]%s", tag, log))
        }

        override fun e(tag: String, msg: String, vararg obj: Any) {
            val log = if (obj == null) msg else String.format(msg, *obj)
            println(String.format("[ERROR][%s]%s", tag, log))
        }

        override fun printErrStackTrace(tag: String, tr: Throwable, format: String, vararg obj: Any) {
            var log: String? = if (obj == null) format else String.format(format, *obj)
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

    var impl: LogImp? = debugLog
        private set

    fun setLogImp(imp: LogImp) {
        impl = imp
    }

    var isOpenLog: Boolean = true

    fun v(tag: String, msg: String, vararg obj: Any) {
        if (impl != null && isOpenLog) {
            impl!!.v(tag, msg, *obj)
        }
    }

    fun e(tag: String, msg: String, vararg obj: Any) {
        if (impl != null && isOpenLog) {
            impl!!.e(tag, msg, *obj)
        }
    }

    fun w(tag: String, msg: String, vararg obj: Any) {
        if (impl != null && isOpenLog) {
            impl!!.w(tag, msg, *obj)
        }
    }

    fun i(tag: String, msg: String, vararg obj: Any) {
        if (impl != null && isOpenLog) {
            impl!!.i(tag, msg, *obj)
        }
    }

    fun d(tag: String, msg: String, vararg obj: Any) {
        if (impl != null && isOpenLog) {
            impl!!.d(tag, msg, *obj)
        }
    }

    fun printErrStackTrace(tag: String, tr: Throwable, format: String, vararg obj: Any) {
        if (impl != null) {
            impl!!.printErrStackTrace(tag, tr, format, *obj)
        }
    }

    interface LogImp {

        fun v(tag: String, msg: String, vararg obj: Any)

        fun i(tag: String, msg: String, vararg obj: Any)

        fun w(tag: String, msg: String, vararg obj: Any)

        fun d(tag: String, msg: String, vararg obj: Any)

        fun e(tag: String, msg: String, vararg obj: Any)

        fun printErrStackTrace(tag: String, tr: Throwable, format: String, vararg obj: Any)

    }
}
