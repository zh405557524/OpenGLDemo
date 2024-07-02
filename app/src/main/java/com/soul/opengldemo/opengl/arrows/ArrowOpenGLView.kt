package com.soul.opengldemo.opengl.arrows

import android.opengl.GLSurfaceView
import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.soul.lib.utils.LogUtil
import com.soul.opengldemo.opengl.body.CreateBodyRendererImpl
import com.soul.opengldemo.opengl.body.CustomGLSurfaceView
import com.soul.opengldemo.opengl.body.Cylinder

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
            CustomGLSurfaceView(ctx).apply {
                setEGLContextClientVersion(2) // 使用 OpenGL ES 2.0
                setRenderer(renderer)
                renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY //三种模式，手动模式，RENDERMODE_CONTINUOUSLY 自动模式
//                onTouchEventListener = { event: MotionEvent -> handleTouchEvent(event, renderer) } // 设置触摸事件监听器

            }
        },
        update = { view ->
            // 在这里可以更新视图的属性，如果有必要
            LogUtil.i("OpenGLView", "update")
        }
    )

}


private var mPreviousX = 0f
private var mPreviousY = 0f

fun handleTouchEvent(event: MotionEvent, renderer: DirectionArrowRendererImpl): Boolean {
    val x = event.x //当前的触控位置X坐标
    val y = event.y //当前的触控位置X坐标
    when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            println("Touch Down")
        }

        MotionEvent.ACTION_MOVE -> {
            println("Touch Move")

            val dx = x - mPreviousX
            val dy = y - mPreviousY

            if (dx > 0) {
                renderer.rotate(-dx, 1F, 0F, 0F)
//                            Cylinder.getInstance().translate(0.1f, 0, 0);
            } else {
                renderer.rotate(dx, 1F, 0F, 0F)
//                            Cylinder.getInstance().translate(-0.1f, 0, 0);
            }

            if (dy > 0) {
                renderer.rotate(-dy, 0F, 0F, 1F)
            } else {
                renderer.rotate(dy, 0F, 0F, 1F)
            }
        }

        MotionEvent.ACTION_UP -> {
            println("Touch Up")
        }
    }
    mPreviousX = x;
    mPreviousY = y;
    return true
}
