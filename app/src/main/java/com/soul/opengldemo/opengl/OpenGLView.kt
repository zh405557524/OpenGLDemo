package com.soul.opengldemo.opengl

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.soul.lib.utils.LogUtil
import com.soul.opengldemo.opengl.body.CustomGLSurfaceView

/**
 * Description: openGLView
 * Author: 祝明
 * CreateDate: 2024/6/6 18:00
 * UpdateUser:
 * UpdateDate: 2024/6/6 18:00
 * UpdateRemark:
 */
@Composable
fun OpenGLView(context: Context, renderer: GLSurfaceView.Renderer) {
    AndroidView(
        factory = { ctx ->
            GLSurfaceView(ctx).apply {
                setEGLContextClientVersion(2) // 使用 OpenGL ES 2.0
                setRenderer(renderer)
                renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
            }
        },
        update = { view ->
            // 在这里可以更新视图的属性，如果有必要
            LogUtil.i("OpenGLView", "update")
        }
    )
}

