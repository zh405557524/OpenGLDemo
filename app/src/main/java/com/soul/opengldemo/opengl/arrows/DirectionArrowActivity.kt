package com.soul.opengldemo.opengl.arrows

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.soul.lib.utils.LogUtil

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
        val directionArrowRendererImpl = DirectionArrowRendererImpl(this)
        setContent { ArrowOpenGLView(directionArrowRendererImpl) }

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        val sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    directionArrowRendererImpl.updateCameraRotation(it.values[0], it.values[1], it.values[2])
                }
            }
        }

        sensorManager.registerListener(sensorEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME)
    }

}