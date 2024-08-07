package com.soul.opengldemo.opengl.arrows

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import com.soul.lib.utils.LogUtil
import com.soul.opengldemo.R
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
    private val arrow = Arrow3D(context)
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //清空屏幕
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        arrow.init()

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        arrow.measure(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        arrow.setColor(R.color.teal_700)
        arrow.drawPrepare()

        for (i in 0..10) {
            arrow.translate(0.0f, 1.50f, 0.0f)
            arrow.draw()
        }

    }

    fun rotate(fl: Float, fl1: Float, fl2: Float, fl3: Float) {
        arrow.rotate(fl, fl1, fl2, fl3)
    }

    fun updateCameraRotation(fl: Float, fl1: Float, fl2: Float) {

        arrow.updateCameraRotation(fl, fl1, fl2)
    }

}