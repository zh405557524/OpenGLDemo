package com.soul.opengldemo.opengl.arrows

import android.content.Context
import android.opengl.EGLConfig
import android.opengl.GLES10.GL_COLOR_BUFFER_BIT
import android.opengl.GLES10.glClear
import android.opengl.GLES10.glClearColor
import android.opengl.GLES20
import android.opengl.GLES20.GL_ARRAY_BUFFER
import android.opengl.GLES20.GL_FRAGMENT_SHADER
import android.opengl.GLES20.GL_VERTEX_SHADER
import android.opengl.GLES20.glAttachShader
import android.opengl.GLES20.glBindBuffer
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glLinkProgram
import android.opengl.GLES20.glUniform4f
import android.opengl.GLES20.glUniformMatrix4fv
import android.opengl.GLES20.glUseProgram
import android.opengl.GLES20.glVertexAttribPointer
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.soul.opengldemo.opengl.arrows.obj.loadOBJFile
import com.soul.opengldemo.opengl.arrows.obj.parseOBJ
import com.soul.opengldemo.opengl.body.OpenGlHelp
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.opengles.GL10

/**
 * Description: TODO
 * Author: 祝明
 * CreateDate: 2024/6/14 16:21
 * UpdateUser:
 * UpdateDate: 2024/6/14 16:21
 * UpdateRemark:
 */
class MyGLRenderer(val context: Context) : GLSurfaceView.Renderer {
    private var vboId = 0
    private var eboId = 0
    private var programId = 0
    private var vertexCount = 0
    private var indexCount = 0

    // 假设 vertices 和 indices 已经从 OBJ 文件中解析出来
    private lateinit var vertices: FloatArray
    private lateinit var indices: IntArray
    var u_Matrix = 0


    // 模型矩阵
    private val mModelMatrix = FloatArray(16)

    override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Matrix.setIdentityM(mModelMatrix, 0) // 初始化为单位矩阵
        val fileContent = loadOBJFile(context, "PPTStepModel.obj")
        val (v, i) = parseOBJ(fileContent)
        vertices = v
        indices = i

        val vertexShaderCode = """
    uniform mat4 u_Matrix;
    attribute vec4 a_Position;
    void main() {
        gl_Position = u_Matrix * a_Position;
    }
""".trimIndent()


        val fragmentShaderCode = """
            precision mediump float;
            uniform vec4 uColor;
            void main() {
                gl_FragColor = uColor;
            }
        """.trimIndent()

        val vertexShader = loadShader(GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GL_FRAGMENT_SHADER, fragmentShaderCode)

        programId = GLES20.glCreateProgram().also {
            glAttachShader(it, vertexShader)
            glAttachShader(it, fragmentShader)
            glLinkProgram(it)
        }

        setupBuffers()

    }

    /**
     * 投影矩阵
     */
    private val mProjectionMatrix = FloatArray(16)
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
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
    }

    fun setMatrix(matrix: FloatArray) {
        val matrixHandle = glGetUniformLocation(programId, "u_Matrix")
        val mMVPMatrix = FloatArray(16)
        // 计算模型视图投影矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, matrix, 0, mModelMatrix, 0)//把投影矩阵和模型矩阵相乘
        glUniformMatrix4fv(matrixHandle, 1, false, mMVPMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        glUseProgram(programId)
        setMatrix(mProjectionMatrix)
        val colorHandle = glGetUniformLocation(programId, "uColor")
        glUniform4f(colorHandle, 1.0f, 0.0f, 0.0f, 1.0f)

        glEnableVertexAttribArray(0)

        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 12, 0)

        glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, eboId)
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCount, 5125, 0)

        GLES20.glDisableVertexAttribArray(0)
    }


    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

    private fun setupBuffers() {
        val vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertices)
                position(0)
            }
        }

        val indexBuffer = ByteBuffer.allocateDirect(indices.size * 4).run {
            order(ByteOrder.nativeOrder())
            asIntBuffer().apply {
                put(indices)
                position(0)
            }
        }

        val buffers = IntArray(2)
        GLES20.glGenBuffers(2, buffers, 0)
        vboId = buffers[0]
        eboId = buffers[1]

        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        GLES20.glBufferData(GL_ARRAY_BUFFER, vertices.size * 4, vertexBuffer, GLES20.GL_STATIC_DRAW)

        glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, eboId)
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indices.size * 4, indexBuffer, GLES20.GL_STATIC_DRAW)

        indexCount = indices.size
    }


    // 旋转变换
    fun rotate(angle: Float, x: Float, y: Float, z: Float) {
        Matrix.rotateM(mModelMatrix, 0, angle, x, y, z)
    }
}
