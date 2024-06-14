package com.soul.opengldemo.opengl.arrows

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import com.soul.lib.utils.LogUtil
import com.soul.opengldemo.R
import com.soul.opengldemo.opengl.IPart
import com.soul.opengldemo.opengl.body.OpenGlHelp

/**
 * Description: 方向箭
 * Author: 祝明
 * CreateDate: 2024/6/14 14:56
 * UpdateUser:
 * UpdateDate: 2024/6/14 14:56
 * UpdateRemark:
 */
class Arrow(val context: Context) : IPart {

    val TAG = "Arrow"

    var arrowArray = floatArrayOf(
        // 箭头的头部（一个大三角形）
        0f, 0.5f,   // 上顶点
        -0.25f, 0f, // 左下顶点
        0.25f, 0f,  // 右下顶点

        // 箭头的身体（一个矩形，这里用两个三角形绘制）
        -0.1f, 0f,  // 左上顶点
        0.1f, 0f,   // 右上顶点
        -0.1f, -0.5f, // 左下顶点

        -0.1f, -0.5f, // 左下顶点
        0.1f, 0f,   // 右上顶点
        0.1f, -0.5f  // 右下顶点
    )



    val BYTES_PER_FLOAT = 4

    private val mProjectionMatrix = FloatArray(16)
    var a_Position = 0
    var u_Color = 0
    var u_Matrix = 0

    init {


    }

    override fun init() {
        //1、初始化顶点数据
        val verticeData = OpenGlHelp.initOpenGlData(arrowArray, BYTES_PER_FLOAT)
        //2、加载着色器
        val vertextShaer = OpenGlHelp.readResourceShader(context, R.raw.vertext_shaer_circle)
        val fragmentShaer = OpenGlHelp.readResourceShader(context, R.raw.fragment_shader_circle)
        //3、编译着色器
        val vertextShaerId = OpenGlHelp.compileShader(GLES20.GL_VERTEX_SHADER, vertextShaer)
        val fragmentShaerID = OpenGlHelp.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaer)
        //4、链接着色器
        val linkProgram = OpenGlHelp.linkProgram(vertextShaerId, fragmentShaerID)
        //5、验证程序
        val validateProgram = OpenGlHelp.validateProgram(linkProgram)
        if (!validateProgram) {
            LogUtil.i(TAG, "openGl 验证程序失败 请检查")
            return
        }
        //6、使用程序
        OpenGlHelp.useProgram(linkProgram)

        //获取指定uniform的位置，并保存在返回值u_color变量中，方便之后使用
        a_Position = OpenGlHelp.getAttribLocation(linkProgram, "a_Position")
        u_Color = OpenGlHelp.getUniformLocation(linkProgram, "u_Color")
        u_Matrix = OpenGlHelp.getUniformLocation(linkProgram, "u_Matrix")

        //绑定属性
        OpenGlHelp.bindVertexAttribPointer(a_Position, 2, 0, verticeData)
        LogUtil.i(TAG, "openGl init 成功")
    }

    override fun measure(width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val a = if (width > height) {
            width.toFloat() / height.toFloat()
        } else {
            height.toFloat() / width.toFloat()
        }

        Matrix.orthoM(
            mProjectionMatrix, 0,
            if (width > height) -a else -1f,
            if (width > height) a else 1f,
            if (width > height) -1f else -a,
            if (width > height) 1f else a,
            -1f, 1f
        )

        LogUtil.i(TAG, "openGl 测量成功")
    }

    override fun draw() {
        LogUtil.i(TAG, "drawOpenGl")
        //更新着色器u_color的值，后面四个参数分别为，红，绿，蓝，透明度
        //指定着色器u_color的颜色为白色
        GLES20.glUniform4f(u_Color, 0.0f, 1.0f, 1.0f, 1.0f)

        //设置矩阵数据
        GLES20.glUniformMatrix4fv(u_Matrix, 1, false, mProjectionMatrix, 0)
        /**
         * 第一个参数：绘制绘制三角形
         * 第二个参数：从顶点数组0索引开始读
         * 第三个参数：读入6个顶点
         */
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 9)

    }

}