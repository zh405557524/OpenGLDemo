package com.soul.opengldemo.opengl

/**
 * Description: TODO
 * Author: 祝明
 * CreateDate: 2024/6/7 15:06
 * UpdateUser:
 * UpdateDate: 2024/6/7 15:06
 * UpdateRemark:
 */
class MatrixHelper {

    companion object {
        /**
         * @param m      生成的新矩阵
         * @param degree 视野角度
         * @param aspect 宽高比
         * @param n      到近处平面的距离
         * @param f      到远处平面的距离
         */
        fun perspetiveM(m: FloatArray, degree: Float, aspect: Float, n: Float, f: Float) {

            // 计算焦距
            val angle = degree * Math.PI / 180.0
            val a = 1.0f / Math.tan(angle / 2.0).toFloat()

            m[0] = a / aspect
            m[1] = 0f
            m[2] = 0f
            m[3] = 0f

            m[4] = 0f
            m[5] = a
            m[6] = 0f
            m[7] = 0f

            m[8] = 0f
            m[9] = 0f
            m[10] = -((f + n) / (f - n))
            m[11] = -1f

            m[12] = 0f
            m[13] = 0f
            m[14] = -((2f * f * n) / (f - n))
            m[15] = 0f
        }

    }
}