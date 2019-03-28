package com.knight.transform.tinyImage.interceptor

class RealTaskInterceptorChain(val index: Int, val interceptors: ArrayList<ITaskInterceptor>, val taskParams: TaskParams) : TaskChain {


    override fun request(): TaskParams = taskParams

    override fun process() {
        if (index >= interceptors.size) {
            return
        }

        val chain = RealTaskInterceptorChain(index + 1, interceptors, taskParams)
        val interceptor = interceptors[index]
        interceptor.intercept(chain)
    }

}