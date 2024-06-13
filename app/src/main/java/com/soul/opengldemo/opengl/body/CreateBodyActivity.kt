package com.soul.opengldemo.opengl.body

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.soul.opengldemo.opengl.OpenGLView

/**
 * Description: 构造简单物体
 * Author: 祝明
 * CreateDate: 2024/6/11 9:53
 * UpdateUser:
 * UpdateDate: 2024/6/11 9:53
 * UpdateRemark:
 */
class CreateBodyActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent { OpenGLView(CreateBodyRendererImpl(this)) }
        }
}