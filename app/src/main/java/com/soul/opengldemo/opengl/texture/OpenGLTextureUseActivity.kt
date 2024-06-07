package com.soul.opengldemo.opengl.texture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.soul.opengldemo.opengl.OpenGLView

/**
 * Description: openGL 纹理使用
 * Author: 祝明
 * CreateDate: 2024/6/7 15:46
 * UpdateUser:
 * UpdateDate: 2024/6/7 15:46
 * UpdateRemark:
 */
class OpenGLTextureUseActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { OpenGLView(context = this, TextureRendererImpl(this)) }
    }

}