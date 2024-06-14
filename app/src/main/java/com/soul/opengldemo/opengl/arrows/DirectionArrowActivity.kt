package com.soul.opengldemo.opengl.arrows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Description: 方向箭头
 * Author: 祝明
 * CreateDate: 2024/6/14 14:34
 * UpdateUser:
 * UpdateDate: 2024/6/14 14:34
 * UpdateRemark:
 */
class DirectionArrowActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { ArrowOpenGLView(DirectionArrowRendererImpl(this)) }
    }

}