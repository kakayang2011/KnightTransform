package com.knight.module1

import android.content.Context
import android.content.Intent
import com.knight.modularization.library.ServiceImpl

@ServiceImpl
class Module1ServiceImpl : Module1Service {
    override fun startModule1Activity(context: Context) {
        context.startActivity(Intent(context, Module1Activity::class.java))
    }

}