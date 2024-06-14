package com.soul.opengldemo.opengl.arrows

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Description: 方向箭 渲染器
 * Author: 祝明
 * CreateDate: 2024/6/14 14:35
 * UpdateUser:
 * UpdateDate: 2024/6/14 14:35
 * UpdateRemark:
 */
class DirectionArrowRendererImpl(context: Context) : Renderer {
    private val arrow = Arrow(context)
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //清空屏幕
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        arrow.init()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        arrow.measure(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        arrow.draw()
    }

}