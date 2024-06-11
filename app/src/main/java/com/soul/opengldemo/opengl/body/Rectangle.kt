package com.soul.opengldemo.opengl.body

import android.content.Context
import android.opengl.GLES20
import com.soul.lib.utils.LogUtil
import com.soul.opengldemo.R
import com.soul.opengldemo.opengl.IPart
import com.soul.opengldemo.opengl.ShaderHelper

/**
 * Description: 矩形
 * Author: 祝明
 * CreateDate: 2024/6/11 16:51
 * UpdateUser:
 * UpdateDate: 2024/6/11 16:51
 * UpdateRemark:
 */
class Rectangle(val context: Context) : IPart {

    val TAG = "Rectangle"
    var tableVertices: FloatArray = floatArrayOf( //第一个三角
        //第一个三角
        -0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,
        //第二个三角
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f,
        //线
        -0.5f, 0f,
        0.5f, 0f,
        //点
        0f, -0.25f,
        0f, 0.25f

    )
    private val BYTES_PER_FLOAT = 4
    var u_color: Int = 0
    override fun init() {
        val verticeData = OpenGlHelp.initOpenGlData(tableVertices, BYTES_PER_FLOAT)
        val fragment_shader_source = OpenGlHelp.readResourceShader(context, R.raw.fragment_shader0)
        val vertex_shader_source = OpenGlHelp.readResourceShader(context, R.raw.vertex_shader)

        val mVertexshader = OpenGlHelp.compileShader(GLES20.GL_VERTEX_SHADER, vertex_shader_source);
        val mFragmentshader = OpenGlHelp.compileShader(GLES20.GL_FRAGMENT_SHADER, fragment_shader_source);

        //链接
        val program = OpenGlHelp.linkProgram(mVertexshader, mFragmentshader);

        //验证opengl对象
        OpenGlHelp.validateProgram(program);

        OpenGlHelp.useProgram(program)

        //获取指定uniform的位置，并保存在返回值u_color变量中，方便之后使用
        u_color = OpenGlHelp.getUniformLocation(program, "u_Color")

        //获取属性位置
        var a_position = OpenGlHelp.getAttribLocation(program, "a_Position")

        OpenGlHelp.bindVertexAttribPointer(a_position, 2, 0, verticeData)


    }

    override fun measure(width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height);
    }

    override fun draw() {

        LogUtil.i(TAG, "drawOpenGl")
        //更新着色器u_color的值，后面四个参数分别为，红，绿，蓝，透明度
        //指定着色器u_color的颜色为白色
        GLES20.glUniform4f(u_color, 1.0f, 1.0f, 1.0f, 1.0f);
        /**
         * 第一个参数：绘制绘制三角形
         * 第二个参数：从顶点数组0索引开始读
         * 第三个参数：读入6个顶点
         *
         * 最终绘制俩个三角形，组成矩形
         */
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        //绘制分割线

        GLES20.glUniform4f(u_color, 1.0f, 0.0f, 0.0f, 1.0f);

        //第一个参数：你想画什么，有三种模式GLES20.GL_TRIANGLES三角形，GLES20.GL_LINES线，GLES20.GL_POINTS点，
        // 第二个参数：从数组那个位置开始读，第三个参数：一共读取几个顶点
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        //绘制点
        GLES20.glUniform4f(u_color, 0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        GLES20.glUniform4f(u_color, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
    }
}