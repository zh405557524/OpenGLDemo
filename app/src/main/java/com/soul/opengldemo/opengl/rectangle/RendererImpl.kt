package com.soul.opengldemo.opengl.rectangle

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
 * Description: gl的渲染实现
 * Author: 祝明
 * CreateDate: 2024/6/6 18:01
 * UpdateUser:
 * UpdateDate: 2024/6/6 18:01
 * UpdateRemark:
 */
class RendererImpl(val context: Context) : GLSurfaceView.Renderer {
    val TAG = "RendererImpl"
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
    private val BYTES_PER_FLOAT = 4
    var u_color: Int = 0
    lateinit var verticeData: FloatBuffer

//    var tableVertices: FloatArray = floatArrayOf(
//        // 转换每个点到NDC坐标
//        normalizeX(0f), normalizeY(0f),
//        normalizeX(9f), normalizeY(14f),
//        normalizeX(0f), normalizeY(14f),
//        normalizeX(0f), normalizeY(0f),
//        normalizeX(9f), normalizeY(0f),
//        normalizeX(9f), normalizeY(14f),
//        normalizeX(0f), normalizeY(7f),
//        normalizeX(9f), normalizeY(7f),
//        normalizeX(4.5f), normalizeY(2f),
//        normalizeX(4.5f), normalizeY(12f)
//    )

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


    private fun initOpenGL() {
        //读取着色器源码
        val fragment_shader_source = ReadResouceText.readResourceText(context, R.raw.fragment_shader0);
        val vertex_shader_source = ReadResouceText.readResourceText(context, R.raw.vertex_shader);

        //编译着色器源码
        val mVertexshader = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertex_shader_source);
        val mFragmentshader = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragment_shader_source);

        //链接
        val program = ShaderHelper.linkProgram(mVertexshader, mFragmentshader);

        //验证opengl对象
        ShaderHelper.validateProgram(program);
        //使用程序
        GLES20.glUseProgram(program);

        //获取指定uniform的位置，并保存在返回值u_color变量中，方便之后使用
        u_color = GLES20.glGetUniformLocation(program, "u_Color")

        //获取属性位置
        var a_position = GLES20.glGetAttribLocation(program, "a_Position")

        //绑定a_position和verticeData顶点位置
        /**
         * 第一个参数，这个就是shader属性
         * 第二个参数，每个顶点有多少分量，我们这个只有来个分量
         * 第三个参数，数据类型
         * 第四个参数，只有整形才有意义，忽略
         * 第5个参数，一个数组有多个属性才有意义，我们只有一个属性，传0
         * 第六个参数，opengl从哪里读取数据
         */
        verticeData.position(0);

        GLES20.glVertexAttribPointer(
            a_position, //这个是属性的位置，传入之前获取的a_position
            2,//这个是每个属性的数据计数，对于这个属性有多少个分量与每一个顶点关联，我们上一节定义顶点用了俩个分量x,y,这就意味着每个
            // 顶点需要俩个分量，我们为顶点设置了俩个分量，但是a_Position定义为vec4，他有4个分量，如果没有有指定值，那么默认第三个分量为0，
            // 第四个分量为1
            GLES20.GL_FLOAT,//这个是数据类型，我们是浮点数所以设置为GLES20.GL_FLOAT
            false, //只有使用整形数据他才有意义，我们暂时忽略设为false
            0, //当数组存储多个属性时他才有意义，本章只有一个属性，暂时忽略传0
            verticeData//	告诉opengl在哪里读取数据，
        );
        //使用顶点
        GLES20.glEnableVertexAttribArray(a_position);
        LogUtil.i(TAG, "初始化open完成")
    }


    private fun drawOpenGl() {
        LogUtil.i(TAG, "drawOpenGl")
        //更新着色器u_color的值，后面四个参数分别为，红，绿，蓝，透明度
        //指定着色器u_color的颜色为白色
        GLES20.glUniform4f(u_color, 1.0f, 1.0f, 1.0f, 1.0f);
        /**
         * 第一个参数：绘制绘制三角形
         * 第二个参数：从顶点数组0索引开始读
         * 第三个参数：读入6个顶点
         *
         * 最终绘制俩个三角形，组成矩形
         */
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        //绘制分割线

        GLES20.glUniform4f(u_color, 1.0f, 0.0f, 0.0f, 1.0f);

        //第一个参数：你想画什么，有三种模式GLES20.GL_TRIANGLES三角形，GLES20.GL_LINES线，GLES20.GL_POINTS点，
        // 第二个参数：从数组那个位置开始读，第三个参数：一共读取几个顶点
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        //绘制点
        GLES20.glUniform4f(u_color, 0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        GLES20.glUniform4f(u_color, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
    }


}