package com.soul.opengldemo.opengl.texture.helper

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log

/**
 * Description: 纹理帮助类
 * Author: 祝明
 * CreateDate: 2024/6/7 16:47
 * UpdateUser:
 * UpdateDate: 2024/6/7 16:47
 * UpdateRemark:
 */
class TextureHelper {

    companion object {
        /**
         * 加载纹理
         */

        fun loadTexture(context: Context, resourceId: Int): Int {
            val textureId = IntArray(1)
            // 获取纹理id
            GLES20.glGenTextures(1, textureId, 0)
            if (textureId[0] == 0) {
                Log.d("mmm", "没有成功创建一个新的 Texture")
                return 0
            }

            val options = BitmapFactory.Options()
            options.inScaled = false

            val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)

            if (bitmap == null) {
                Log.d("mmm", "创建 bitmap 失败")
                GLES20.glDeleteTextures(1, textureId, 0)
                return 0
            }

            // 绑定纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0])

            // 设置过滤器
            //缩小，使用三线性过滤
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR)
            //放大，使用双线性过滤
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

            // 把图片加载到 OpenGL 里面
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            // 释放 bitmap
            bitmap.recycle()
            // 生成 mip 贴图
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

            // 解除纹理绑定，第二个参数传 0 就是解除绑定
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

            return textureId[0]
        }
    }
}