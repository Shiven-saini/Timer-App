package me.shiven.timer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class OrientationListener(context: Context, private val onOrientationChange: (Boolean) -> Unit) :
    SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var isInvert: Boolean = false

    fun startListening() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.values?.let { values ->
            val yAxis = values[1]
            if(yAxis.toInt() in 9..10){
                isInvert = false
            }
            if(yAxis.toInt() in -9 downTo -10) {
                isInvert = true
            }
            onOrientationChange(isInvert)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
