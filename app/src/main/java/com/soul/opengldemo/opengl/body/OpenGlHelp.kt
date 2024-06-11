package com.soul.opengldemo.opengl.body

import android.content.Context
import android.opengl.GLES20
import android.util.Log
import com.soul.lib.utils.LogUtil
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Description: openGL帮助类
 * Author: 祝明
 * CreateDate: 2024/6/11 10:15
 * UpdateUser:
 * UpdateDate: 2024/6/11 10:15
 * UpdateRemark:
 */
class OpenGlHelp {
    companion object {
        /**
         * 1、初始化openGl 数据容器
         * @param tableVertices 数据
         */
        fun initOpenGlData(tableVertices: FloatArray, BYTES_PER_FLOAT: Int): FloatBuffer {
            return ByteBuffer.allocateDirect(tableVertices.size * BYTES_PER_FLOAT)//分配内存空间
                .order(ByteOrder.nativeOrder())//告诉缓冲区按照本子字节组织内容
                .asFloatBuffer()//转换为浮点缓冲区
                .put(tableVertices)//把数据复制到缓冲区
                .apply { position(0) }//把数据下标移动到指定位置
        }


        /**
         * 2、加载着色器资源
         * @param context 上下文
         * @param resourceId 资源id
         */
        fun readResourceShader(context: Context, resourceId: Int): String {
            val body = StringBuilder()
            try {
                val inputStream = context.resources.openRawResource(resourceId)
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var nextLine: String?
                while (bufferedReader.readLine().also { nextLine = it } != null) {
                    body.append(nextLine)
                    body.append("\n")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return body.toString()
        }

        /**
         * 3、编译着色器
         * @param type 类型 ,GLES20.GL_FRAGMENT_SHADER 片段着色器和GLES20.GL_VERTEX_SHADER 顶点着色器
         * @param source 源码,着色器加载出来的资源
         */
        fun compileShader(type: Int, source: String): Int {
            // 创建 shader
            //	创建一个新的着色器对象，如果返回值是0，那么创建失败，参数type有俩种类型GLES20.GL_FRAGMENT_SHADER 片段着色器和
            //	GLES20.GL_VERTEX_SHADER 顶点着色器
            val shaderId = GLES20.glCreateShader(type)
            if (shaderId == 0) {
                Log.d("mmm", "创建shader失败")
                return 0
            }
            //上传shader的源代码，并把它与现有的shaderid关联
            GLES20.glShaderSource(shaderId, source)
            //	编译着色器，编译之前需要先上传源码
            GLES20.glCompileShader(shaderId)
            // 取出编译结果
            val compileStatus = IntArray(1)
            //取出shaderId的编译状态并把他写入compileStatus的0索引
            GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            Log.d("mmm编译状态", GLES20.glGetShaderInfoLog(shaderId));

            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(shaderId);
                Log.d("mmm", "创建shader失败");
                return 0;
            }

            return shaderId;
        }


        /**
         * 4、链接着色器
         * @param vertexshader 顶点着色器
         * @param fragmentshader 片段着色器
         */
        fun linkProgram(vertexShader: Int, fragmentShader: Int): Int {

            //创建程序对象
            val programId = GLES20.glCreateProgram()
            if (programId == 0) {
                Log.d("mmm", "创建program失败")
                return 0
            }

            //依附着色器
            GLES20.glAttachShader(programId, vertexShader)
            GLES20.glAttachShader(programId, fragmentShader)

            //链接程序
            GLES20.glLinkProgram(programId)

            //检查链接状态
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0)
            Log.d("mmm", "链接程序" + GLES20.glGetProgramInfoLog(programId))
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programId)
                Log.d("mmm", "链接program失败")
                return 0
            }

            return programId
        }


        /**
         * 5、验证openl程序对象
         * @param program 程序对象
         */
        fun validateProgram(program: Int): Boolean {
            GLES20.glValidateProgram(program)
            val validateStatus = IntArray(1)
            GLES20.glGetProgramiv(program, GLES20.GL_VALIDATE_STATUS, validateStatus, 0)
            Log.d("mmm", "验证程序" + GLES20.glGetProgramInfoLog(program))
            if (validateStatus[0] == 0) {
                Log.d("mmm", "验证program失败")
            }
            return validateStatus[0] != 0
        }

        /**
         * 6、使用程序
         */
        fun useProgram(program: Int) {
            Log.i("OpenHelp", "使用程序")
            GLES20.glUseProgram(program)
        }

        /**
         * 7、获取着色器属性
         * @param program 程序对象
         * @param name 属性名称
         */
        fun getAttribLocation(program: Int, name: String): Int {
            return GLES20.glGetAttribLocation(program, name)
        }

        /**
         * 8、绑定顶点属性
         */
        fun bindVertexAttribPointer(a_position: Int,
                                    POSITION_COMPONENT_COUNT: Int,
                                    STRIDE: Int,
                                    verticeData: FloatBuffer) {
            verticeData.position(0)
            GLES20.glVertexAttribPointer(
                a_position, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, STRIDE, verticeData
            )
            //使用顶点
            GLES20.glEnableVertexAttribArray(a_position)
            LogUtil.i("OpenHelp", "初始化open完成")
        }

        /**
         * 9、获取着色器属性
         */
        fun getUniformLocation(program: Int, s: String): Int {
            return GLES20.glGetUniformLocation(program, s)
        }

    }
}