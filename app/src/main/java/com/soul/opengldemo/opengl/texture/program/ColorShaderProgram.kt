package com.soul.opengldemo.opengl.texture.program

import android.content.Context
import android.opengl.GLES20
import com.soul.opengldemo.R
import com.soul.opengldemo.opengl.ReadResouceText
import com.soul.opengldemo.opengl.ShaderHelper

/**
 * Description: 颜色着色器
 * Author: 祝明
 * CreateDate: 2024/6/7 16:42
 * UpdateUser:
 * UpdateDate: 2024/6/7 16:42
 * UpdateRemark:
 */
class ColorShaderProgram(context: Context) {

    var a_color: Int =0
    var a_position: Int=0
    private val u_matrix: Int
    private val program: Int

    init {
        // 读取着色器源码
        val fragment_shader_source = ReadResouceText.readResourceText(context, R.raw.fragment_shader1)
        val vertex_shader_source = ReadResouceText.readResourceText(context, R.raw.vertex_shader2)
        program = ShaderHelper.buildProgram(vertex_shader_source, fragment_shader_source)

        a_color = GLES20.glGetAttribLocation(program, "a_Color")
        a_position = GLES20.glGetAttribLocation(program, "a_Position")
        u_matrix = GLES20.glGetUniformLocation(program, "u_Matrix")
    }

    fun setUniforms(matrix: FloatArray) {
        GLES20.glUniformMatrix4fv(u_matrix, 1, false, matrix, 0)
    }

    fun useProgram() {
        // 使用程序
        GLES20.glUseProgram(program)
    }
}