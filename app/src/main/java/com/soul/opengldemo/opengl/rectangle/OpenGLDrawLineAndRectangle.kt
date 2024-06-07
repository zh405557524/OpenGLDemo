package com.soul.opengldemo.opengl.rectangle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.soul.opengldemo.opengl.OpenGLView

/**
 * Description: openGL绘制线和矩形
 * Author: 祝明
 * CreateDate: 2024/6/7 11:22
 * UpdateUser:
 * UpdateDate: 2024/6/7 11:22
 * UpdateRemark:
 */
class OpenGLDrawLineAndRectangle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { OpenGLView(context = this, RendererImpl(this)) }
    }
}