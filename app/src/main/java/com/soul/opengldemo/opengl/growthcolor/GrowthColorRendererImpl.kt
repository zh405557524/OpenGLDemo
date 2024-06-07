package com.soul.opengldemo.opengl.growthcolor

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.soul.lib.utils.LogUtil
import com.soul.opengldemo.R
import com.soul.opengldemo.opengl.ReadResouceText
import com.soul.opengldemo.opengl.ShaderHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Description: TODO
 * Author: 祝明
 * CreateDate: 2024/6/7 11:35
 * UpdateUser:
 * UpdateDate: 2024/6/7 11:35
 * UpdateRemark:
 */
class GrowthColorRendererImpl(val context: Context) : GLSurfaceView.Renderer {

    val TAG = "RendererImpl"

    // 逆时针绘制三角形
    val tableVertices = floatArrayOf(
        // 顶点
        0f, 0f,
        // 顶点颜色值
        1f, 1f, 1f,

        -0.5f, -0.5f,
        0.7f, 0.7f, 0.7f,

        0.5f, -0.5f,
        0.7f, 0.7f, 0.7f,

        0.5f, 0.5f,
        0.7f, 0.7f, 0.7f,

        -0.5f, 0.5f,
        0.7f, 0.7f, 0.7f,

        -0.5f, -0.5f,
        0.7f, 0.7f, 0.7f,

        // 线
        -0.5f, 0f,
        1f, 0f, 0f,

        0.5f, 0f,
        0f, 1f, 0f,

        // 点
        0f, -0.25f,
        1f, 0f, 0f,

        0f, 0.25f,
        0f, 0f, 1f
    )

    private val BYTES_PER_FLOAT = 4
    lateinit var verticeData: FloatBuffer

    private val POSITION_COMPONENT_COUNT = 2

    //新增
    private val COLOR_COMPONENT_COUNT = 3
    private val STRIDE: Int = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT


    init {
        LogUtil.i(TAG, "初始化openGl")
        verticeData =
            ByteBuffer.allocateDirect(tableVertices.size * BYTES_PER_FLOAT)//分配一块本地内存，分配大小由外部传入
                .order(ByteOrder.nativeOrder())//告诉缓冲区，按照本地字节序组织内容
                .asFloatBuffer()//我们希望操作Float，调用这个方法会返回FloatBuffer
                .put(tableVertices)//	填充数据
                .apply {
                    position(0)//把数据下标移动到指定位置
                }


    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //当surface被创建时，GlsurfaceView会调用这个方法，这个发生在应用程序
        // 第一次运行的时候或者从其他Activity回来的时候也会调用
        //清空屏幕
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        initOpenGL()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
//在Surface创建以后，每次surface尺寸大小发生变化，这个方法会被调用到，比如横竖屏切换
        GLES20.glViewport(0, 0, width, height);
    }

    override fun onDrawFrame(gl: GL10?) {
        //当绘制每一帧数据的时候，会调用这个放方法，这个方法一定要绘制一些东西，即使只是清空屏幕
        //因为这个方法返回后，渲染区的数据会被交换并显示在屏幕上，如果什么都没有话，会看到闪烁效果
        //设置清空屏幕用的颜色，前三个参数分别是 红 绿 蓝 最后一个参数成为阿尔发（alpha）代表透明度，
        // 上面我们把第一个设为1，其余为0，表示红色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        drawOpenGl();
    }

    private var a_position = 0
    private var a_color = 0
    private fun initOpenGL() {
        // 读取着色器源码
        val fragmentShaderSource = ReadResouceText.readResourceText(context, R.raw.fragment_shader2)
        val vertexShaderSource = ReadResouceText.readResourceText(context, R.raw.vertex_shader2)

// 编译着色器源码
        val mVertexShader = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource)
        val mFragmentShader = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource)
// 链接程序
        val program = ShaderHelper.linkProgram(mVertexShader, mFragmentShader)

// 验证 OpenGL 对象
        ShaderHelper.validateProgram(program)
// 使用程序
        GLES20.glUseProgram(program)

// 获取 shader 属性
        a_position = GLES20.glGetAttribLocation(program, "a_Position")
        a_color = GLES20.glGetAttribLocation(program, "a_Color")

// 绑定 a_position 和 verticeData 顶点位置
        /**
         * 第一个参数，这个就是 shader 属性
         * 第二个参数，每个顶点有多少分量，我们这个只有两个分量
         * 第三个参数，数据类型
         * 第四个参数，只有整型才有意义，忽略
         * 第五个参数，一个数组有多个属性才有意义，我们只有一个属性，传 0
         * 第六个参数，OpenGL 从哪里读取数据
         */
        verticeData.position(0)
        GLES20.glVertexAttribPointer(
            a_position, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
            false, STRIDE, verticeData
        )
// 开启顶点
        GLES20.glEnableVertexAttribArray(a_position)

        verticeData.position(POSITION_COMPONENT_COUNT)
        GLES20.glVertexAttribPointer(
            a_color, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT,
            false, STRIDE, verticeData
        )
// 开启顶点
        GLES20.glEnableVertexAttribArray(a_color)


    }


    private fun drawOpenGl() {
        LogUtil.i(TAG, "drawOpenGl")
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //绘制长方形
        //指定着色器u_color的颜色为白色
        /**
         * 第一个参数：绘制绘制三角形
         * 第二个参数：从顶点数组0索引开始读
         * 第三个参数：读入6个顶点
         *
         * 最终绘制俩个三角形，组成矩形
         */
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        //绘制分割线

        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        //绘制点
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);

    }
}