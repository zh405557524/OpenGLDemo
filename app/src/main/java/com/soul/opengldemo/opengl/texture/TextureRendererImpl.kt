package com.soul.opengldemo.opengl.texture

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.soul.lib.utils.LogUtil
import com.soul.opengldemo.R
import com.soul.opengldemo.opengl.MatrixHelper
import com.soul.opengldemo.opengl.texture.helper.Mallet
import com.soul.opengldemo.opengl.texture.helper.Table
import com.soul.opengldemo.opengl.texture.helper.TextureHelper
import com.soul.opengldemo.opengl.texture.program.ColorShaderProgram
import com.soul.opengldemo.opengl.texture.program.TextureSharderProgram
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TextureRendererImpl(val context: Context) : GLSurfaceView.Renderer {

    val TAG = "TextureRendererImpl"

    // 投影矩阵
    private val mProjectionMatrix = FloatArray(16)

    // 模型矩阵
    private val mModelMatrix = FloatArray(16)
    private var table: Table? = null
    private var mallet: Mallet? = null
    private var textureSharderProgram: TextureSharderProgram? = null
    private var colorShaderProgram: ColorShaderProgram? = null
    private var textureid = 0
    private var textureid1 = 0


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //清空屏幕，并显示蓝色
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        table = Table();
        mallet = Mallet();

        textureSharderProgram = TextureSharderProgram(context);
        colorShaderProgram = ColorShaderProgram(context);

        textureid = TextureHelper.loadTexture(context, R.mipmap.moon);
        textureid1 = TextureHelper.loadTexture(context, R.mipmap.airship);

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
// 设置屏幕的大小
        GLES20.glViewport(0, 0, width, height)
// 45度视野角创建一个透视投影，这个视椎体从z轴-1开始，-10结束
        MatrixHelper.perspetiveM(mProjectionMatrix, 45f, width.toFloat() / height.toFloat(), 1f, 10f)

// 设置为单位矩阵
        Matrix.setIdentityM(mModelMatrix, 0)
// 向z轴平移-2f
        Matrix.translateM(mModelMatrix, 0, 0f, 0f, -3f)

// 绕着x轴旋转-60度
        Matrix.rotateM(mModelMatrix, 0, -60f, 1.0f, 0f, 0f)

        val temp = FloatArray(16)
// 矩阵相乘
        Matrix.multiplyMM(temp, 0, mProjectionMatrix, 0, mModelMatrix, 0)
// 把矩阵重复赋值到投影矩阵
        System.arraycopy(temp, 0, mProjectionMatrix, 0, temp.size)


    }

    override fun onDrawFrame(gl: GL10?) {
        LogUtil.i(TAG, "onDrawFrame")
// 清除屏幕所有颜色，然后重设glClearColor的颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

// 画桌子
        textureSharderProgram?.useProgram()
        textureSharderProgram?.setUniforms(mProjectionMatrix, textureid)
        textureSharderProgram?.setUniforms1( textureid1)
        table?.bindData(textureSharderProgram)
        table?.draw()

// 画木槌
        colorShaderProgram?.useProgram()
        colorShaderProgram?.setUniforms(mProjectionMatrix)
        mallet?.bindData(colorShaderProgram)
        mallet?.draw()

    }

}
