package com.soul.opengldemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soul.lib.utils.LogUtil
import com.soul.opengldemo.opengl.growthcolor.GLGrowthColorActivity
import com.soul.opengldemo.opengl.ratiosize.GLRatioSizeActivity
import com.soul.opengldemo.opengl.rectangle.GLDrawRectangleActivity
import com.soul.opengldemo.ui.theme.OpenGlDemoTheme

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { ButtonList() }
        LogUtil.i(TAG, "进入主页")
    }


    @Composable
    fun ButtonList() {
        // 使用 Column 来垂直排列按钮
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(16.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp)
            ) {
                // 每个按钮执行不同的操作
                ButtonWithAction("OpenGL 点、线、面的制作") {
                    LogUtil.i(TAG, "按钮 1")
                    startActivity(Intent(this@MainActivity, GLDrawRectangleActivity::class.java))
                }
                ButtonWithAction("OpenGL 颜色调整") {
                    LogUtil.i(TAG, "按钮 2")
                    startActivity(Intent(this@MainActivity, GLGrowthColorActivity::class.java))
                }
                ButtonWithAction("OpenGL 宽高比例调整") {
                    LogUtil.i(TAG, "按钮 3")
                    startActivity(Intent(this@MainActivity, GLRatioSizeActivity::class.java))
                }
            }
        }
    }

    @Composable
    fun ButtonWithAction(buttonText: String, onClickAction: () -> Unit) {
        Button(
            onClick = { onClickAction() },  // 调用传入的操作
            modifier = Modifier
                .padding(PaddingValues(vertical = 8.dp))  // 添加垂直间距
                .fillMaxWidth()
        ) {
            Text(buttonText)
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OpenGlDemoTheme {
        Greeting("Android")
    }
}