package com.soul.opengldemo.opengl.ratiosize

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.soul.opengldemo.opengl.OpenGLView

/**
 * Description: OpenGL 宽高比例调整
 * Author: 祝明
 * CreateDate: 2024/6/7 14:11
 * UpdateUser:
 * UpdateDate: 2024/6/7 14:11
 * UpdateRemark:
 */
class GLRatioSizeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { OpenGLView(context = this, RatioSizeRendererImpl(this)) }
    }
}