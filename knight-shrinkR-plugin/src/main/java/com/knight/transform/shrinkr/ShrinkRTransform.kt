package com.knight.transform.shrinkr

import com.android.build.api.transform.TransformInvocation
import com.knight.transform.IPlugin
import transform.KnightTransform

class ShrinkRTransform(val context: Context, iPlugin: IPlugin) : KnightTransform(context, iPlugin) {


    override fun transform(invocation: TransformInvocation) {
        super.transform(invocation)

    }
}