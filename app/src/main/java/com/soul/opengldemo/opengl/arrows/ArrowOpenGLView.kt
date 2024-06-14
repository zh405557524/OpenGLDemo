package com.soul.opengldemo.opengl.arrows

import android.opengl.GLSurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.soul.lib.utils.LogUtil

/**
 * Description: 方向箭OpenGlView
 * Author: 祝明
 * CreateDate: 2024/6/14 14:36
 * UpdateUser:
 * UpdateDate: 2024/6/14 14:36
 * UpdateRemark:
 */
@Composable
fun ArrowOpenGLView(renderer: DirectionArrowRendererImpl) {
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