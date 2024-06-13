package com.soul.opengldemo.opengl.body

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import com.soul.opengldemo.R
import com.soul.opengldemo.opengl.MatrixHelper
import com.soul.opengldemo.opengl.ReadResouceText
import com.soul.opengldemo.opengl.ShaderHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin


/**
 * Description: TODO
 * Author: 祝明
 * CreateDate: 2024/6/13 22:20
 * UpdateUser:
 * UpdateDate: 2024/6/13 22:20
 * UpdateRemark:
 */
class Cylinder private constructor() {
    private var floatBuffer: FloatBuffer? = null
    private var program = 0
    private var a_position = 0
    private var u_matrix = 0
    private val mProjectionMatrix = FloatArray(16)
    private val mModelMatrix = FloatArray(16)

    //视图矩阵
    private val mViewMatrix = FloatArray(16)

    private val mViewProjectionMatrix = FloatArray(16)

    private val mViewModelProjectionMatrix = FloatArray(16)

    //圆心x坐标
    private val x = 0f

    //圆心y坐标
    private val y = 0f

    //圆半径
    private val r = 0.6f

    //三角形的个数
    private val count = 50
    private var u_color = 0
    private var offerset = 0

    private var vertextData: FloatArray = FloatArray(0)

    private val drawList = ArrayList<DrawCommand>()

    fun init(context: Context) {
        //设置为单位矩阵
        Matrix.setIdentityM(mModelMatrix, 0)
        //1 生成顶点
        //2 加载顶点到本地内存
        val point = Geometry.Point(0f, 0f, 0f)
        val cylinder = Geometry.Cylinder(point, 0.4f, 0.5f)
        createPuck(cylinder, 50)
        initVertexData(vertextData)
        //3 加载着色器的源码并且加载程序
        loadShaderAndProgram(context)
        //4 加载着色器中的属性
        loadShaderAttributes()
        //5 把着色器属性和顶点数据绑定起来，开启使用顶点
        bindAttributes()
    }


    //创建圆柱
    fun createPuck(puck: Geometry.Cylinder, number: Int) {
        //计算需要的画圆柱一共需要的顶点数

        val size = sizeOfCricleInVerTices(number) * 2 + sizeOfCylinderInVerTices(number)

        vertextData = FloatArray(size * POSITION_COMPONENT_COUNT)
        //创建顶部圆
        val puckTop = Geometry.Circle(puck.center.translateY(puck.height / 2), puck.radius)
        //创建底部圆
        val puckTop1 = Geometry.Circle(puck.center.translateY(-puck.height / 2), puck.radius)

        //绘制侧面
        appendCylinder(puck, number)

        //绘制顶部圆
        appendCircle(puckTop, number, true)
        //绘制底部圆
        appendCircle(puckTop1, number, false)
    }

    private fun appendCircle(circle: Geometry.Circle, number: Int, color: Boolean) {
        val startVertex = offerset / FLOATS_PER_VERTEX
        val numberVertices = sizeOfCricleInVerTices(number)

        vertextData[offerset++] = circle.center.x
        vertextData[offerset++] = circle.center.y
        vertextData[offerset++] = circle.center.z

        for (i in 0..number) {
            //计算每个圆心角的角度
            val angle = (i.toFloat() / number.toFloat()) * (Math.PI.toFloat() * 2f)

            vertextData[offerset++] = circle.center.x + circle.radius * cos(angle.toDouble()).toFloat()
            vertextData[offerset++] = circle.center.y
            vertextData[offerset++] = circle.center.z + circle.radius * sin(angle.toDouble()).toFloat()

            //            Log.d("mmm1最后", offerset + "/");
        }

        Log.d("mmm1", "$startVertex/$numberVertices$color")

        drawList.add(object : DrawCommand {
            override fun draw() {
                if (color) {
                    GLES20.glUniform4f(u_color, 0.0f, 1.0f, 0.0f, 1f)
                } else {
                    GLES20.glUniform4f(u_color, 1.0f, 0.0f, 0.0f, 1f)
                }
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, startVertex, numberVertices)
            }
        })
    }


    fun appendCylinder(cylinder: Geometry.Cylinder, number: Int) {
        val startVertex = offerset / FLOATS_PER_VERTEX
        Log.d("mmm1怎么回事", "$offerset/")
        val numberVertices = sizeOfCylinderInVerTices(number)
        val yStart = cylinder.center.y - cylinder.height / 2
        val yEed = cylinder.center.y + cylinder.height / 2

        for (i in 0..number) {
            val angle = (i.toFloat() / number.toFloat()) * (Math.PI.toFloat() * 2f)

            val xPosition = cylinder.center.x + cylinder.radius * cos(angle.toDouble()).toFloat()
            val zPosition = cylinder.center.z + cylinder.radius * sin(angle.toDouble()).toFloat()

            vertextData[offerset++] = xPosition
            vertextData[offerset++] = yStart
            vertextData[offerset++] = zPosition

            vertextData[offerset++] = xPosition
            vertextData[offerset++] = yEed
            vertextData[offerset++] = zPosition
        }

        Log.d("mmm2", "$startVertex/$numberVertices")

        drawList.add(object : DrawCommand {
            override fun draw() {
                GLES20.glUniform4f(u_color, 1.0f, 1.0f, 1.0f, 1f)
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, startVertex, numberVertices)
            }
        })
    }

    /**
     * 把顶点数据加载到本地内存中
     *
     * @param vertexData 顶点数据
     */
    fun initVertexData(vertexData: FloatArray) {
        floatBuffer = ByteBuffer
            .allocateDirect(vertexData.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)

        floatBuffer?.position(0)
    }


    /**
     * 加载着色器的源码并且加载程序
     */
    fun loadShaderAndProgram(context: Context) {
        //读取着色器源码
        val fragment_shader_source: String = OpenGlHelp.readResourceShader(context, R.raw.fragment_shader_circle)
        val vertex_shader_source: String = OpenGlHelp.readResourceShader(context, R.raw.vertext_shaer_circle)

        //编译着色器源码
        val mVertexshader = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertex_shader_source)
        val mFragmentshader = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragment_shader_source)

        //链接程序
        program = ShaderHelper.linkProgram(mVertexshader, mFragmentshader)

        //验证opengl对象
        OpenGlHelp.validateProgram(program)

        //使用程序
        GLES20.glUseProgram(program)
    }


    /**
     * 加载着色器中的属性
     */
    fun loadShaderAttributes() {
        //获取shader属性
        u_color = GLES20.glGetUniformLocation(program, "u_Color")
        a_position = GLES20.glGetAttribLocation(program, "a_Position")
        u_matrix = GLES20.glGetUniformLocation(program, "u_Matrix")
    }


    /**
     * 把着色器属性和顶点数据绑定起来，开启使用顶点
     */
    fun bindAttributes() {
        //绑定a_position和verticeData顶点位置
        /**
         * 第一个参数，这个就是shader属性
         * 第二个参数，每个顶点有多少分量，我们这个只有来个分量
         * 第三个参数，数据类型
         * 第四个参数，只有整形才有意义，忽略
         * 第5个参数，一个数组有多个属性才有意义，我们只有一个属性，传0
         * 第六个参数，opengl从哪里读取数据
         */
        floatBuffer!!.position(0)
        GLES20.glVertexAttribPointer(
            a_position, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
            false, 0, floatBuffer
        )
        //开启顶点
        GLES20.glEnableVertexAttribArray(a_position)
    }


    /**
     * 根据屏幕宽高创建正交矩阵，修复宽高比问题
     *
     * @param width  屏幕宽
     * @param height 屏幕高
     */
    fun projectionMatrix(width: Int, height: Int) {
        //45度视野角创建一个透视投影，这个视椎体从z轴-1开始，-10结束

        MatrixHelper.perspetiveM(mProjectionMatrix, 45F, width.toFloat() / height.toFloat(), 1f, 10f)

        //创建视图矩阵
        Matrix.setLookAtM(
            mViewMatrix, 0, 0f, 2f, 2f, 0f, 0f,
            0f, 0f, 1f, 0f
        )
    }


    /**
     * 开始画圆
     */
    fun draw() {
        //设置圆的颜色 红色
        //设置矩阵数据
        Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)

        positionTableInScreen()
        GLES20.glUniformMatrix4fv(u_matrix, 1, false, mViewModelProjectionMatrix, 0)

        //        Log.d("mmm", drawList.size() + "/");
        for (command in drawList) {
            command.draw()
        }
    }

    private fun positionTableInScreen() {
        //矩阵相乘
        Matrix.multiplyMM(
            mViewModelProjectionMatrix, 0, mViewProjectionMatrix,
            0, mModelMatrix, 0
        )
    }

    fun translate(x: Float, y: Float, z: Float) //设置沿xyz轴移动
    {
        Log.d("mmm", "平移")
        Matrix.translateM(mModelMatrix, 0, x, y, z)
    }


    //旋转变换
    fun rotate(angle: Float, x: Float, y: Float, z: Float) { // 设置绕xyz轴移动
        Log.d("mmm", "旋转 angle:$angle x:$x y:$y z:$z")
        Matrix.rotateM(mModelMatrix, 0, angle, x, y, z)
    }


    //缩放变换
    fun scale(x: Float, y: Float, z: Float) {
        Matrix.scaleM(mModelMatrix, 0, x, y, z)
    }


    interface DrawCommand {
        fun draw()
    }

    companion object {
        // 每个顶点包含的数据个数 （ x 和 y ）
        private const val POSITION_COMPONENT_COUNT = 3

        //每个顶点占用4个字节
        private const val BYTES_PER_FLOAT = 4
        const val FLOATS_PER_VERTEX: Int = 3
        val instance: Cylinder = Cylinder()


        private fun sizeOfCricleInVerTices(number: Int): Int {
            //切分为number个三角形，需要一个重复的顶点和一个圆心顶点,所以需要加2
            return 1 + number + 1
        }


        private fun sizeOfCylinderInVerTices(number: Int): Int {
            //围绕顶部圆的每一个顶点，都需要俩个顶点，并且前俩个顶点需要重复俩次才能闭合
            return (number + 1) * 2
        }
    }
}
