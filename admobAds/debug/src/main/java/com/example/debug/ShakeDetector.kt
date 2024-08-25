package com.example.debug

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(
    private val context: Context,
    private val onShake: () -> Unit, // Callback to invoke when a shake is detected
) : SensorEventListener {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // Sensitivity settings
    private var shakeThreshold = if (BuildConfig.DEBUG) {
        30f
    } else {
        50f
    }
    private var lastShakeTime: Long = 0
    private val shakeInterval: Long = if (BuildConfig.DEBUG) {
        500
    } else {
        4000
    }

    fun start() {
//        BuildCo
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]

                // Calculate the magnitude of the acceleration vector
                val magnitude = sqrt(x * x + y * y + z * z)

                // Detect shake by comparing magnitude to threshold
                if (magnitude > shakeThreshold) {
                    val currentTime = System.currentTimeMillis()

                    // Ensure a delay between shakes
                    if (currentTime - lastShakeTime > shakeInterval) {
                        lastShakeTime = currentTime
                        onShake.invoke()
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
