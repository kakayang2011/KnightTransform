package com.knight.transform

import com.android.build.api.transform.TransformInvocation
import transform.KnightTransform

class DoubleCheckTransform(context: Context, iPlugin: IPlugin) : KnightTransform(context, iPlugin) {

    override fun transform(transform: TransformInvocation) {
        super.transform(transform)
    }

}