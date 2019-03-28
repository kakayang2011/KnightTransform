package com.knight.transform.tinyImage.interceptor

import com.knight.transform.tinyImage.Context
import org.gradle.api.DefaultTask
import org.gradle.api.Task

interface ITaskInterceptor {
    fun intercept(chain: TaskChain)
}

interface TaskChain {
    fun process()
    fun request(): TaskParams
}

data class TaskParams(val context: Context, var task: Task? = null)