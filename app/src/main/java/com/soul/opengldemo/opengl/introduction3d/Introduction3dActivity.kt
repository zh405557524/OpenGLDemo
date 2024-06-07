package com.soul.opengldemo.opengl.introduction3d

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.soul.opengldemo.opengl.OpenGLView

/**
 * Description: 三维入门
 * Author: 祝明
 * CreateDate: 2024/6/7 14:54
 * UpdateUser:
 * UpdateDate: 2024/6/7 14:54
 * UpdateRemark:
 */
class Introduction3dActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { OpenGLView(context = this, Introduction3dRendererImpl(this)) }
    }

}