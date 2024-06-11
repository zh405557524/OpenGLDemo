package com.soul.opengldemo.opengl.body

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.soul.opengldemo.opengl.IPart
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CreateBodyRendererImpl(val createBodyActivity: CreateBodyActivity) :
    GLSurfaceView.Renderer {

        var circle: IPart = Circle( createBodyActivity)
//    var circle: IPart = Rectangle(createBodyActivity)
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //清空屏幕
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        circle.init()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        circle.measure(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        circle.draw()
    }

}
