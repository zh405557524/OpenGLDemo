package com.soul.opengldemo.opengl.body

import android.opengl.GLSurfaceView
import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.soul.lib.utils.LogUtil

/**
 * Description: TODO
 * Author: 祝明
 * CreateDate: 2024/6/13 22:53
 * UpdateUser:
 * UpdateDate: 2024/6/13 22:53
 * UpdateRemark:
 */
@Composable
fun OpenGLView(
    renderer: CreateBodyRendererImpl,
    renderMode: Int = GLSurfaceView.RENDERMODE_CONTINUOUSLY ) {
    val context = LocalContext.current
    AndroidView(
        factory = { ctx ->
            CustomGLSurfaceView(ctx).apply {
                setEGLContextClientVersion(2) // 使用 OpenGL ES 2.0
                setRenderer(renderer)
                this.renderMode = renderMode
                onTouchEventListener = { event: MotionEvent -> handleTouchEvent(event, renderer) } // 设置触摸事件监听器
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

fun handleTouchEvent(event: MotionEvent, renderer: CreateBodyRendererImpl): Boolean {
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
                Cylinder.instance.rotate(-dx, 1F, 0F, 0F)
//                            Cylinder.getInstance().translate(0.1f, 0, 0);
            } else {
                Cylinder.instance.rotate(dx, 1F, 0F, 0F)
//                            Cylinder.getInstance().translate(-0.1f, 0, 0);
            }

            if (dy > 0) {
                Cylinder.instance.rotate(-dy, 0F, 0F, 1F)
            } else {
                Cylinder.instance.rotate(dy, 0F, 0F, 1F)
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

