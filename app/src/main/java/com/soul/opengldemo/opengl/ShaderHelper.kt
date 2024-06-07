package com.soul.opengldemo.opengl

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log

/**
 * Description: TODO
 * Author: 祝明
 * CreateDate: 2024/6/6 19:01
 * UpdateUser:
 * UpdateDate: 2024/6/6 19:01
 * UpdateRemark:
 */
class ShaderHelper {
    companion object {
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
//            取出shaderId的编译状态并把他写入compileStatus的0索引，如果0索引处是0，那么就是编译失败
            GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

            //获取一个可读的消息，如果有着色器的有用内容，就会储存到着色器的信息日志中
            Log.d("mmm编译状态", GLES20.glGetShaderInfoLog(shaderId))

            if (compileStatus[0] == 0) {
                //	删除着色器
                GLES20.glDeleteShader(shaderId)
                Log.d("mmm", "创建shader失败")
                return 0
            }

            return shaderId
        }


        /**
         * 链接
         */
        fun linkProgram(vertexShader: Int, fragmentShader: Int): Int {
            // 创建程序对象，如果返回值是0则创建失败
            val programId = GLES20.glCreateProgram()
            if (programId == 0) {
                Log.d("mmm", "创建program失败")
                return 0
            }
            // 依附着色器，依次把顶点着色器和片段着色器添加到程序上
            GLES20.glAttachShader(programId, vertexShader)
            GLES20.glAttachShader(programId, fragmentShader)
            // 链接程序，把着色器和程序链接起来
            GLES20.glLinkProgram(programId)
            // 检查链接状态
            val linkStatus = IntArray(1)
            //查看程序状态，会把结果存入linkStatus数组的0索引位置，如果为0则链接程序失败
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
         * 验证openl程序对象
         */
        fun validateProgram(program: Int): Boolean {
            //验证程序
            GLES20.glValidateProgram(program)
            val validateStatus = IntArray(1)
            //	获取验证结果,如果为0则验证失败
            GLES20.glGetProgramiv(program, GLES20.GL_VALIDATE_STATUS, validateStatus, 0)

            //告诉opengl在绘制任何东西到屏幕需要用这里定义的程序
            Log.d("mmm", "当前openl情况" + validateStatus[0] + "/" + GLES20.glGetProgramInfoLog(program))

            return validateStatus[0] != 0
        }


        fun buildProgram(vertex_shader_source: String, fragment_shader_source: String): Int {
            //编译着色器源码
            val mVertexshader = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertex_shader_source);
            val mFragmentshader = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragment_shader_source);

            //链接
            val program = ShaderHelper.linkProgram(mVertexshader, mFragmentshader);
            //验证opengl对象
            ShaderHelper.validateProgram(program);
//            //使用程序
//            GLES20.glUseProgram(program);
            return program
        }



    }

}