package com.knight.transform.byteK

import com.android.utils.Pair
import com.knight.transform.BaseContext
import com.knight.transform.byteK.extension.ByteKExtension
import org.gradle.api.Project
import java.util.HashMap
import java.util.HashSet
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Matcher
import java.util.regex.Pattern

class Context(project: Project,
              extension: ByteKExtension) : BaseContext<ByteKExtension>(project, extension) {
    val MATCH_ALL = ".+"
    val PATTERN_MATCH_ALL = Pattern.compile(MATCH_ALL)
    private val RClasses = ConcurrentHashMap.newKeySet<String>(1000)
    private val RFields = ConcurrentHashMap<String, Map<String, Any>>(3000)
    private val mWhiteList = HashMap<String, Set<Pair<Pattern, Pattern>>>()
    private val PATTERN_KEEP_LIST = "(?<package>([\\w]+\\.)*)R.(?<inner>[^.]+)(.(?<field>.+))?"
    private val keepListPattern = Pattern.compile(PATTERN_KEEP_LIST)
    private val rClassSimpleNamePattern = Pattern.compile("R(\\$.+)?")

    fun isRFile(relativePath: String): Boolean {
        val end = relativePath.lastIndexOf(".class")
        return end > 0 && isRClass(relativePath.substring(0, end))
    }

    fun isRClass(name: String?): Boolean {
        if (name == null || name.isEmpty()) return false
        val classNameStart = name.lastIndexOf("/")
        return rClassSimpleNamePattern.matcher(name.substring(classNameStart + 1)).matches()
    }

    fun isRClassName(name: String?): Boolean {
        if (name == null || name.isEmpty()) return false
        val classNameStart = name.lastIndexOf(".")
        return rClassSimpleNamePattern.matcher(name.substring(classNameStart + 1)).matches()
    }

    fun discardable(relatviePath: String): Boolean {
        val end = relatviePath.lastIndexOf(".class")
        return end > 0 && RClasses.contains(relatviePath.substring(0, end))
    }

    fun addRClass(className: String) {
        RClasses.add(className)
    }

    fun addRField(owner: String, name: String, value: Any) {
        (RFields.computeIfAbsent(owner) { HashMap(100) } as HashMap)[name] = value
    }

    fun containRField(owner: String, name: String): Boolean {
        val fields = RFields[owner]
        return fields != null && !fields.isEmpty() && fields.containsKey(name)
    }

    fun getRFieldValue(owner: String, name: String): Any? {
        val fields = RFields[owner]
        return if (fields == null || fields.isEmpty()) {
            null
        } else fields[name]
    }

    fun initWithWhiteList(whiteList: List<String>?) {
        if (whiteList == null) {
            return
        }
        for (item in whiteList) {
            addWhiteList(item)
        }
    }

    private fun getMatchByGroup(m: Matcher, name: String): String? {
        try {
            return m.group(name)
        } catch (e: Exception) {
            return ""
        }

    }

    private fun addWhiteList(item: String) {
        if (item.isEmpty()) {
            return
        }
        val m = keepListPattern.matcher(item)
        if (!m.find()) {
            return
        }
        var packageName = getMatchByGroup(m, "package")
        val className: String
        val innerClass = getMatchByGroup(m, "inner")
        var fieldName = getMatchByGroup(m, "field")
        if (packageName != null && !packageName.isEmpty()) {
            packageName = packageName.replace("\\.".toRegex(), "/")
        } else {
            packageName = "([\\w]+/)*"
        }
        if (innerClass != null && !innerClass.isEmpty()) {
            className = "R$$innerClass"
        } else {
            className = "R"
        }
        if (fieldName != null && !fieldName.isEmpty()) {
            fieldName = convertToPatternString(fieldName)
        }


        if (fieldName == null || fieldName.isEmpty()) {
            (mWhiteList.computeIfAbsent(innerClass
                    ?: "") { e -> HashSet() } as HashSet)
                    .add(Pair.of<Pattern, Pattern>(Pattern.compile(resolveDollarChar(packageName + className)), PATTERN_MATCH_ALL))
        } else {
            (mWhiteList.computeIfAbsent(innerClass
                    ?: "") { e -> HashSet() } as HashSet)
                    .add(Pair.of(Pattern.compile(resolveDollarChar(packageName + className)), Pattern.compile(resolveDollarChar(fieldName))))
        }
    }

    fun shouldKeep(className: String, fieldName: String): Boolean {
        var matched = false
        val whiteList = mWhiteList[getInnerRClass(className)]
        if (whiteList == null || whiteList.isEmpty()) {
            return false
        }
        for (pair in whiteList) {
            val classPat = pair.first
            val fieldPat = pair.second
            if (fieldPat.matcher(className).matches() && classPat.matcher(fieldName).matches()) {
                matched = true
                break
            }
        }
        return matched
    }

    private fun getInnerRClass(className: String?): String {
        if (className == null || className.isEmpty()) return ""
        val innerClassStart = className.lastIndexOf("$")
        return if (innerClassStart == -1) "" else className.substring(innerClassStart + 1)
    }

    fun convertToPatternString(input: String): String {
        // ?	Zero or one character
        // *	Zero or more of character
        // +	One or more of character
        val map = HashMap<Char, String>(4)
        map['.'] = "\\."
        map['?'] = ".?"
        map['*'] = ".*"
        map['+'] = ".+"
        val sb = StringBuilder(input.length * 2)
        for (i in 0 until input.length) {
            val ch = input[i]
            val replacement = map[ch]
            sb.append(replacement ?: ch)
        }
        return sb.toString()
    }

    fun resolveDollarChar(s: String): String {
        var s = s
        // 内部类的类名定义用的是$做分隔符
        s = s.replace("\\$".toRegex(), "\\\\\\$")
        return s
    }
}