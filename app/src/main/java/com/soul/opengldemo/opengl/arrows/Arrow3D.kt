package com.soul.opengldemo.opengl.arrows

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_ARRAY_BUFFER
import android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER
import android.opengl.Matrix
import android.util.Log
import androidx.core.content.ContextCompat
import com.soul.lib.utils.LogUtil
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

    //视图矩阵
    val viewMatrix = FloatArray(16)

    //混合矩阵
    val mixMatrix = FloatArray(16)

    private val attribute = IntArray(16)

    override fun init() {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.setIdentityM(projectionMatrix, 0)
        Matrix.setIdentityM(viewMatrix, 0)


//        // 旋转参数
//        val angle = 45f // 旋转45度
//        val x = 1f      // 绕x轴旋转
//        val y = 0f      // 绕y轴旋转
//        val z = 0f      // 绕z轴旋转
//
//// 应用旋转
//        Matrix.rotateM(modelMatrix, 0, angle, x, y, z)
//


        //2、初始化shader
        val createProgram =
            OpenGlHelp.createProgram(context, R.raw.vertex_shader_project, R.raw.fragment_shader_project)
        if (createProgram == 0) {
            return
        }
        attribute[0] = OpenGlHelp.getAttribLocation(createProgram, "a_Position")
        attribute[1] = OpenGlHelp.getUniformLocation(createProgram, "a_normal")
        attribute[2] = OpenGlHelp.getUniformLocation(createProgram, "a_texCoord0")

        attribute[3] = OpenGlHelp.getUniformLocation(createProgram, "modelMatrix")
        attribute[4] = OpenGlHelp.getUniformLocation(createProgram, "modelviewMatrix")
        attribute[5] = OpenGlHelp.getUniformLocation(createProgram, "mvpMatrix")
        attribute[6] = OpenGlHelp.getUniformLocation(createProgram, "light_positionEye")
        attribute[7] = OpenGlHelp.getUniformLocation(createProgram, "light_diffuseColor")
        attribute[8] = OpenGlHelp.getUniformLocation(createProgram, "unit2d")
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

//        val aspect = width.toFloat() / height
//
//        // 尼玛,f0和f完全不一样，坑爹的玩意
//        Matrix.perspectiveM(this.i, 0, 65.0f, aspect, 0.1f, 200.0f)
    }


    fun setColor(res: Int) {
        val colorInt = ContextCompat.getColor(context, res)
        val alpha = (colorInt shr 24) and 0xff  // 右移24位，与0xff进行AND操作获取Alpha
        val red = (colorInt shr 16) and 0xff  // 右移16位获取Red
        val green = (colorInt shr 8) and 0xff  // 右移8位获取Green
        val blue = colorInt and 0xff  // 直接与0xff获取Blue
        val a = alpha / 255.0f
        val r = red / 255.0f
        val g = green / 255.0f
        val b = blue / 255.0f
        colorArray[0] = r
        colorArray[1] = g
        colorArray[2] = b
        colorArray[3] = a
    }

    val colorArray = floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f)

    fun drawPrepare() {
        Matrix.setIdentityM(modelMatrix, 0)
        // 缩放参数
        val sx = 0.3f      // 缩小到原来的0.5倍
        val sy = 0.3f      // 缩小到原来的0.5倍
        val sz = 0.3f      // 缩小到原来的0.5倍
// 应用缩放
        Matrix.scaleM(modelMatrix, 0, sx, sy, sz)

    }

    override fun draw() {

        val viewModelMatrix = FloatArray(16)
        Matrix.multiplyMM(viewModelMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mixMatrix, 0, projectionMatrix, 0, viewModelMatrix, 0)

        GLES20.glUniform4f(attribute[1], colorArray[0], colorArray[1], colorArray[2], colorArray[3])
        GLES20.glUniformMatrix4fv(attribute[2], 1, false, mixMatrix, 0)

        GLES20.glBindBuffer(GL_ARRAY_BUFFER, this.vboId)
        GLES20.glVertexAttribPointer(attribute.get(0), 3, GLES20.GL_FLOAT, false, 0, indiceLimit)
//        GLES20.glVertexAttribPointer(attribute.get(1), 3, 5126, false, 0, this.f)
//        GLES20.glVertexAttribPointer(attribute.get(2), 2, 5126, false, 0, this.e)
        GLES20.glBindBuffer(GL_ARRAY_BUFFER, 0)
        GLES20.glEnableVertexAttribArray(attribute.get(0))
//        GLES20.glEnableVertexAttribArray(attribute.get(1))
//        GLES20.glEnableVertexAttribArray(attribute.get(2))
        GLES20.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.eboId)
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, this.indexCount, GLES20.GL_UNSIGNED_INT, 0)
        GLES20.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
        GLES20.glDisableVertexAttribArray(attribute.get(0))
        GLES20.glDisableVertexAttribArray(attribute.get(1))
        GLES20.glDisableVertexAttribArray(attribute.get(2))

    }

    private var indiceLimit = 0
    private var verticesLimit = 0
    private var texCoordsLimt = 0
    private fun setupBuffers1() {
        try {
            val inputStream: InputStream = context.getAssets().open("PPTStepModel.obj")
            val obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream))
            val indices = ObjData.getFaceVertexIndices(obj, 3)
            val vertices = ObjData.getVertices(obj)
            val texCoords = ObjData.getTexCoords(obj, 2)
            val normal = ObjData.getNormals(obj)
            this.indexCount = indices.limit()
            val iArr = IntArray(2)
            GLES20.glGenBuffers(2, iArr, 0)
            this.vboId = iArr[0]
            this.eboId = iArr[1]
            this.indiceLimit = 0
            val limit = (vertices.limit() * 4) + 0
            this.verticesLimit = limit
            val limit2 = limit + (texCoords.limit() * 4)
            this.texCoordsLimt = limit2
            val limit3 = normal.limit()
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.vboId)
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, limit2 + (limit3 * 4), null, 35044)
            GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, this.indiceLimit, vertices.limit() * 4, vertices)
            GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, this.verticesLimit, texCoords.limit() * 4, texCoords)
            GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, this.texCoordsLimt, normal.limit() * 4, normal)
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
            GLES20.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.eboId)
            GLES20.glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indexCount * 4, indices, 35044)
            GLES20.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
        } catch (e2: IOException) {
            e2.printStackTrace()
        }
    }


    var vboId: Int = 0
    var eboId: Int = 0
    var indexCount = 0

    fun rotate(angle: Float, x: Float, y: Float, z: Float) {
        Matrix.rotateM(modelMatrix, 0, angle, x, y, z)
        //矩阵相乘，主要是融合视图矩阵与模型矩阵
        Matrix.multiplyMM(mixMatrix, 0, projectionMatrix, 0, modelMatrix, 0)
    }


    fun translate(x: Float, y: Float, z: Float) //设置沿xyz轴移动
    {
        Matrix.translateM(modelMatrix, 0, x, y, z)
    }


    //缩放变换
    fun scale(x: Float, y: Float, z: Float) {
        Matrix.scaleM(modelMatrix, 0, x, y, z)
        Matrix.multiplyMM(mixMatrix, 0, projectionMatrix, 0, modelMatrix, 0)
    }


    private var lastAngleX: Float = 0F
    private var lastAngleY: Float = 0F
    private var lastAngleZ: Float = 0F

    private var sameCount = 0
    private var maxCount = 10
    fun updateCameraRotation(deltaX: Float, deltaY: Float, deltaZ: Float) {
        // 旋转角度可能需要根据陀螺仪的灵敏度和传感器返回的具体值进行调整
        val angleX = Math.toDegrees(deltaX.toDouble()).toFloat()
        val angleY = Math.toDegrees(deltaY.toDouble()).toFloat()
        val angleZ = Math.toDegrees(deltaZ.toDouble()).toFloat()
        LogUtil.i("DirectionArrowRendererImpl", "angleX:$angleX angleY:$angleY angleZ:$angleZ")
        lastAngleX = 0f
        lastAngleY = 0f
        lastAngleZ = 0f

        Matrix.rotateM(viewMatrix, 0, angleX / 60, 1f, 0f, 0f)
        Matrix.rotateM(viewMatrix, 0, angleY / 60, 0f, 1f, 0f)
        Matrix.rotateM(viewMatrix, 0, angleZ / 60, 0f, 0f, 1f)

    }

}


