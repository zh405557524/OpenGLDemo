package com.soul.opengldemo.opengl.body

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

/**
 * Description: TODO
 * Author: 祝明
 * CreateDate: 2024/6/13 22:52
 * UpdateUser:
 * UpdateDate: 2024/6/13 22:52
 * UpdateRemark:
 */

class CustomGLSurfaceView(context: Context) : GLSurfaceView(context) {

    var onTouchEventListener: ((event: MotionEvent) -> Boolean)? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return onTouchEventListener?.invoke(event) ?: super.onTouchEvent(event)
    }
}
