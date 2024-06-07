package com.soul.opengldemo.opengl.texture.helper

import android.opengl.GLES20
import com.soul.opengldemo.opengl.texture.program.ColorShaderProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Description: TODO
 * Author: 祝明
 * CreateDate: 2024/6/7 16:40
 * UpdateUser:
 * UpdateDate: 2024/6/7 16:40
 * UpdateRemark:
 */
class Mallet {

    private val BYTES_PER_FLOAT = 4
    private val floatBuffer: FloatBuffer
    private val POSITION_COMPONENT_COUNT = 2
    private val COLOR_COMPONENT_COUNT = 3
    private val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

    private val malletVertices = floatArrayOf(
        // 顶点
        0f, -0.4f,
        // RGB
        1f, 0f, 0f,

        0f, 0.4f,
        0f, 0f, 1f
    )

    init {
        floatBuffer = ByteBuffer
            .allocateDirect(malletVertices.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(malletVertices)
    }

    fun bindData(colorShaderProgram: ColorShaderProgram?) {
        if (colorShaderProgram == null) {
            return
        }
        setAttributeLocation(0, colorShaderProgram.a_position, POSITION_COMPONENT_COUNT, STRIDE)
        setAttributeLocation(POSITION_COMPONENT_COUNT, colorShaderProgram.a_color, COLOR_COMPONENT_COUNT, STRIDE)
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 2)
    }

    private fun setAttributeLocation(dataOffset: Int, attributeLocation: Int, componentCount: Int, stride: Int) {
        floatBuffer.position(dataOffset)
        GLES20.glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT, false, stride, floatBuffer)
        GLES20.glEnableVertexAttribArray(attributeLocation)
        floatBuffer.position(0)
    }
}