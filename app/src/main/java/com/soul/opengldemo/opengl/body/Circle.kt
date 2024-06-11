package com.soul.opengldemo.opengl.body

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import com.soul.lib.utils.LogUtil
import com.soul.opengldemo.R
import com.soul.opengldemo.opengl.IPart

/**
 * Description: 圆
 * Author: 祝明
 * CreateDate: 2024/6/11 10:44
 * UpdateUser:
 * UpdateDate: 2024/6/11 10:44
 * UpdateRemark:
 */
class Circle : IPart {


    private var context: Context

    // 圆心
    private var center: Point = Point(0f, 0f)

    // 半径
    private var radius: Float = 0f

    // 顶点数量
    private var vertexCount: Int = 0

    // 顶点坐标
    private var vertexArray: FloatArray = floatArrayOf()


    private var a_position = 0

    private var a_color = 0

    private var u_matrix = 0

    private val mProjectionMatrix = FloatArray(16)


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

    constructor(center: Point, radius: Float, vertexCount: Int, context: Context) {
        this.center = center
        this.radius = radius
        this.vertexCount = vertexCount
        this.context = context
//        vertexArray = FloatArray(vertexCount * 2)
//        // 计算每个顶点的坐标
//        for (i in 0 until vertexCount) {
//            val x = (center.x + radius * Math.cos(2 * Math.PI * i / vertexCount)).toFloat()
//            val y = (center.y + radius * Math.sin(2 * Math.PI * i / vertexCount)).toFloat()
//            vertexArray[i * 2] = x
//            vertexArray[i * 2 + 1] = y
//        }

        vertexArray = getVertexData()


    }


    // 圆心x坐标
    private var x = 0f

    // 圆心y坐标
    private var y = 0f

    // 圆半径
    private var r = 0.6f

    // 三角形的个数
    private var count = 50

    val POSITION_COMPONENT_COUNT = 2
    val BYTES_PER_FLOAT = 4


    /**
     * 生成圆的顶点数据
     *
     * @return 返回圆的顶点坐标
     */
    fun getVertexData(): FloatArray {
        // 切分为count个三角形，需要一个重复的顶点和一个圆心顶点,所以需要加2
        val nodeCount = count + 2
        // 储存顶点数据的容器
        val vertexData = FloatArray(nodeCount * POSITION_COMPONENT_COUNT)

        var offset = 0

        vertexData[offset++] = x
        vertexData[offset++] = y

        for (i in 0 until count + 1) {
            val angleInRadians = i.toFloat() / count.toFloat() * (Math.PI * 2f).toFloat()
            vertexData[offset++] = x + r * Math.cos(angleInRadians.toDouble()).toFloat()
            vertexData[offset++] = y + r * Math.sin(angleInRadians.toDouble()).toFloat()
        }

        return vertexData
    }

    /**
     * 初始化openGl
     */
    override fun init() {
        //1、初始化openGl 数据容器
        val floatBuffer = OpenGlHelp.initOpenGlData(tableVertices, BYTES_PER_FLOAT)
        //2、加载着色器资源
        val vertexShader = OpenGlHelp.readResourceShader(context, R.raw.vertext_shaer_circle)//顶点着色器
        val fragmentShader = OpenGlHelp.readResourceShader(context, R.raw.fragment_shader_circle)//片段着色器
        //3、编译着色器
        val vertexShaderId = OpenGlHelp.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader)
        val fragmentShaderId = OpenGlHelp.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader)
        //4、链接程序
        val program = OpenGlHelp.linkProgram(vertexShaderId, fragmentShaderId)
        //5、验证程序
        val validateProgram = OpenGlHelp.validateProgram(program)
        if (!validateProgram) {
            Log.i("Circle", "openGl 验证程序失败 ")
            return
        }
        //6、使用程序
        OpenGlHelp.useProgram(program)

        //7、获取着色器属性
        a_position = OpenGlHelp.getAttribLocation(program, "a_Position")
        a_color = OpenGlHelp.getUniformLocation(program, "u_Color")
        u_matrix = OpenGlHelp.getUniformLocation(program, "u_Matrix")

        OpenGlHelp.bindVertexAttribPointer(a_position, 2, 0, floatBuffer)
    }


    override fun measure(width: Int, height: Int) {
        LogUtil.i("Circle", "measure")
        GLES20.glViewport(0, 0, width, height);
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

    override fun draw() {
        LogUtil.i("Circle", "draw")
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        //设置圆的颜色 红色
        GLES20.glUniform4f(a_color, 1.0f, 1.0f, 1.0f, 1f)
        //设置矩阵数据
        GLES20.glUniformMatrix4fv(u_matrix, 1, false, mProjectionMatrix, 0)
        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, count + 2)

    }


    /**
     * 点
     */
    class Point(var x: Float, var y: Float)
}