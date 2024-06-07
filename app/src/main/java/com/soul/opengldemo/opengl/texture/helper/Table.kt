package com.soul.opengldemo.opengl.texture.helper

import android.opengl.GLES20
import com.soul.opengldemo.opengl.texture.program.TextureSharderProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Description: 桌子数据
 * Author: 祝明
 * CreateDate: 2024/6/7 16:26
 * UpdateUser:
 * UpdateDate: 2024/6/7 16:26
 * UpdateRemark:
 */
class Table {
    private val BYTES_PER_FLOAT = 4
    private val floatBuffer: FloatBuffer
    private val POSITION_COMPONENT_COUNT = 2
    private val TEXTURE_COMPONENT_COUNT = 2
    private val STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COMPONENT_COUNT) * BYTES_PER_FLOAT

    private val tableVertices = floatArrayOf(
        // 顶点坐标 xy
        0f, 0f,
        // ST 纹理坐标
        0.5f, 0.5f,

        -0.5f, -0.8f,
        0f, 0.9f,

        0.5f, -0.8f,
        1f, 0.9f,

        0.5f, 0.8f,
        1f, 0.1f,

        -0.5f, 0.8f,
        0f, 0.1f,

        -0.5f, -0.8f,
        0f, 0.9f
    )

    init {
        floatBuffer = ByteBuffer
            .allocateDirect(tableVertices.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(tableVertices)
    }

    fun bindData(textureShaderProgram: TextureSharderProgram?) {
        if (textureShaderProgram == null) {
            return
        }
        setAttributeLocation(0, textureShaderProgram.a_position, POSITION_COMPONENT_COUNT, STRIDE)
        setAttributeLocation(POSITION_COMPONENT_COUNT, textureShaderProgram.a_TextureCoordinates, TEXTURE_COMPONENT_COUNT, STRIDE)
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)
    }

    private fun setAttributeLocation(dataOffset: Int, attributeLocation: Int, componentCount: Int, stride: Int) {
        floatBuffer.position(dataOffset)
        GLES20.glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT, false, stride, floatBuffer)
        GLES20.glEnableVertexAttribArray(attributeLocation)
        floatBuffer.position(0)
    }

}