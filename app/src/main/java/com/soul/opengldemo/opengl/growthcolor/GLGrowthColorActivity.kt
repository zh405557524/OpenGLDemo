package com.soul.opengldemo.opengl.growthcolor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.soul.opengldemo.opengl.OpenGLView

/**
 * Description: GL 添加颜色
 * Author: 祝明
 * CreateDate: 2024/6/7 11:33
 * UpdateUser:
 * UpdateDate: 2024/6/7 11:33
 * UpdateRemark:
 */
class GLGrowthColorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { OpenGLView(context = this, GrowthColorRendererImpl(this)) }
    }
}