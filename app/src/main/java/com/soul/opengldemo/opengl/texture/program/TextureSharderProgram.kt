package com.soul.opengldemo.opengl.texture.program

import android.content.Context
import android.opengl.GLES20
import com.soul.opengldemo.R
import com.soul.opengldemo.opengl.ReadResouceText
import com.soul.opengldemo.opengl.ShaderHelper

/**
 * Description: 纹理着色器
 * Author: 祝明
 * CreateDate: 2024/6/7 16:28
 * UpdateUser:
 * UpdateDate: 2024/6/7 16:28
 * UpdateRemark:
 */
class TextureSharderProgram(val context: Context) {
    private val u_matrix: Int
    private val u_TextureUnit: Int
    private val u_TextureUnit1: Int
    val a_position: Int
    val a_TextureCoordinates: Int
    private val program: Int

    init {
        // 读取着色器源码
        val fragment_shader_source = ReadResouceText.readResourceText(context, R.raw.texture_fragment_shader1)
        val vertex_shader_source = ReadResouceText.readResourceText(context, R.raw.texture_vertex_sharder)
        program = ShaderHelper.buildProgram(vertex_shader_source, fragment_shader_source)

        a_position = GLES20.glGetAttribLocation(program, "a_Position")
        a_TextureCoordinates = GLES20.glGetAttribLocation(program, "a_TextureCoordinates")

        u_matrix = GLES20.glGetUniformLocation(program, "u_Matrix")
        u_TextureUnit = GLES20.glGetUniformLocation(program, "u_TextureUnit")
        u_TextureUnit1 = GLES20.glGetUniformLocation(program, "u_TextureUnit1")
    }

    fun setUniforms(matrix: FloatArray, textureId: Int) {
        GLES20.glUniformMatrix4fv(u_matrix, 1, false, matrix, 0)

        // 把活动的纹理单元设置为纹理单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)

        // 把纹理绑定到这个单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)

        // 把选定的着色器，传递到到片段着色器的u_TextureUnit属性
        GLES20.glUniform1i(u_TextureUnit, 0)
    }

    fun setUniforms1(textureId: Int) {

        //把活动的纹理单元设置为纹理单元1
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);

        //把纹理绑定到这个单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        //把选定的着色器，传递到到片段着色器的u_TextureUnit属性
        GLES20.glUniform1i(u_TextureUnit1, 1);
    }

    fun useProgram() {
        // 使用程序
        GLES20.glUseProgram(program)
    }
}