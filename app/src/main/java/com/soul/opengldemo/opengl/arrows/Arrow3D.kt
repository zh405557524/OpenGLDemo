package com.soul.opengldemo.opengl.arrows

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import com.soul.opengldemo.R
import com.soul.opengldemo.opengl.IPart
import com.soul.opengldemo.opengl.body.OpenGlHelp
import de.javagl.obj.ObjData
import de.javagl.obj.ObjReader
import de.javagl.obj.ObjUtils
import java.io.IOException
import java.io.InputStream

/**
 * Description: 加载3d模式的箭头
 * Author: 祝明
 * CreateDate: 2024/6/17 10:29
 * UpdateUser:
 * UpdateDate: 2024/6/17 10:29
 * UpdateRemark:
 */
class Arrow3D(val context: Context) : IPart {

    //模型矩阵
    val modelMatrix = FloatArray(16)

    //投影矩阵
    val projectionMatrix = FloatArray(16)

    //混合矩阵
    val mixMatrix = FloatArray(16)

    private lateinit var vertices: FloatArray

    private lateinit var indices: IntArray

    private val attribute = IntArray(3)

    override fun init() {
        Matrix.setIdentityM(modelMatrix, 0)
        //1、加载模式数据
        val (f, i) = OpenGlHelp.load3DModel(context, "NewStepModel.obj")
        vertices = f
        indices = i

        //2、初始化shader
        val createProgram = OpenGlHelp.createProgram(context, R.raw.vertext_shaer_circle, R.raw.fragment_shader_circle)
        if (createProgram == 0) {
            return
        }
        attribute[0] = OpenGlHelp.getAttribLocation(createProgram, "a_Position")
        attribute[1] = OpenGlHelp.getUniformLocation(createProgram, "u_Color")
        attribute[2] = OpenGlHelp.getUniformLocation(createProgram, "u_Matrix")
//        setupBuffers()
        setupBuffers1()
    }




    override fun measure(width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)


        val a = if (width > height) {
            width.toFloat() / height.toFloat()
        } else {
            height.toFloat() / width.toFloat()
        }

        Matrix.orthoM(
            projectionMatrix, 0,
            if (width > height) -a else -1f,
            if (width > height) a else 1f,
            if (width > height) -1f else -a,
            if (width > height) 1f else a,
            -1f, 1f
        )


    }

    override fun draw() {

        Matrix.multiplyMM(mixMatrix, 0, projectionMatrix, 0, modelMatrix, 0)

        GLES20.glUniform4f(attribute[1], 1.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glUniformMatrix4fv(attribute[2], 1, false, mixMatrix, 0)
//
//
//        GLES20.glEnableVertexAttribArray(attribute[0])
//
//        //设置顶点数据
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId)
//        GLES20.glVertexAttribPointer(attribute[0], 3, GLES20.GL_FLOAT, false, 12, 0)
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
//
//        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, eboId)
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCount, GLES20.GL_UNSIGNED_INT, 0)
//        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0)
//
//        GLES20.glDisableVertexAttribArray(attribute[0])
        GLES20.glBindBuffer(34962, this.vboId)
        GLES20.glVertexAttribPointer(attribute.get(0), 3, 5126, false, 0, this.d)
//        GLES20.glVertexAttribPointer(attribute.get(1), 3, 5126, false, 0, this.f)
//        GLES20.glVertexAttribPointer(attribute.get(2), 2, 5126, false, 0, this.e)
        GLES20.glBindBuffer(34962, 0)
        GLES20.glEnableVertexAttribArray(attribute.get(0))
//        GLES20.glEnableVertexAttribArray(attribute.get(1))
//        GLES20.glEnableVertexAttribArray(attribute.get(2))
        GLES20.glBindBuffer(34963, this.eboId)
        GLES20.glDrawElements(4, this.indexCount, 5125, 0)
        GLES20.glBindBuffer(34963, 0)
        GLES20.glDisableVertexAttribArray(attribute.get(0))
        GLES20.glDisableVertexAttribArray(attribute.get(1))
        GLES20.glDisableVertexAttribArray(attribute.get(2))


    }

    private var d = 0
    private var e = 0
    private var f = 0
    private fun setupBuffers1() {
        try {
            val inputStream: InputStream = context.getAssets().open("NewStepModel.obj")
            val obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream))
            val c3 = ObjData.getFaceVertexIndices(obj, 3)
            val j = ObjData.getVertices(obj)
            val g = ObjData.getTexCoords(obj, 2)
            val e = ObjData.getNormals(obj)
            this.indexCount = c3.limit()
            val iArr = IntArray(2)
            GLES20.glGenBuffers(2, iArr, 0)
            this.vboId = iArr[0]
            this.eboId = iArr[1]
            this.d = 0
            val limit = (j.limit() * 4) + 0
            this.e = limit
            val limit2 = limit + (g.limit() * 4)
            this.f = limit2
            val limit3 = e.limit()
            GLES20.glBindBuffer(34962, this.vboId)
            GLES20.glBufferData(34962, limit2 + (limit3 * 4), null, 35044)
            GLES20.glBufferSubData(34962, this.d, j.limit() * 4, j)
            GLES20.glBufferSubData(34962, this.e, g.limit() * 4, g)
            GLES20.glBufferSubData(34962, this.f, e.limit() * 4, e)
            GLES20.glBindBuffer(34962, 0)
            GLES20.glBindBuffer(34963, this.eboId)
            GLES20.glBufferData(34963, this.indexCount * 4, c3, 35044)
            GLES20.glBindBuffer(34963, 0)
        } catch (e2: IOException) {
            e2.printStackTrace()
        }
    }


    var vboId: Int = 0
    var eboId: Int = 0
    var indexCount = 0


    private fun setupBuffers() {
        val vertexBuffer = OpenGlHelp.createBuffer(vertices)
        val indicesBuffer = OpenGlHelp.createBuffer(indices)

        val buffer = IntArray(2)
        GLES20.glGenBuffers(2, buffer, 0)
        vboId = buffer[0]
        eboId = buffer[1]

        //数据绑定中 GLES20.GL_ARRAY_BUFFER 该缓冲区将被用作存储顶点数组的数据
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId)
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.size * 4, vertexBuffer, GLES20.GL_STATIC_DRAW)

        //数据绑定中  GLES20.GL_ELEMENT_ARRAY_BUFFER 用于指定缓冲区类型的目标，专门用于存储索引数组
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, eboId)
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indices.size * 4, indicesBuffer, GLES20.GL_STATIC_DRAW)

        indexCount = indices.size
    }

    fun rotate(angle: Float, x: Float, y: Float, z: Float) {
        Matrix.rotateM(modelMatrix, 0, angle, x, y, z)
        //矩阵相乘，主要是融合视图矩阵与模型矩阵
        Matrix.multiplyMM(mixMatrix, 0, projectionMatrix, 0, modelMatrix, 0)
    }

}