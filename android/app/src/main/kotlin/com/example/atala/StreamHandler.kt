package com.example.atala

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.flutter.plugin.common.EventChannel

// sensorManage : SensorManager means sensorManager of Type SensorManager
//got error on class so had to implement members to get the onListen, on Cancel etc.
class StreamHandler(private val sensorManager: SensorManager, sensorType: Int, private var interval: Int = SensorManager.SENSOR_DELAY_NORMAL) :
EventChannel.StreamHandler, SensorEventListener{
    private val sensor = sensorManager.getDefaultSensor(sensorType)
    private var eventSink: EventChannel.EventSink? = null

    //Fired when someone starts listening
    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        if (sensor != null) {
            eventSink = events
            sensorManager.registerListener(this, sensor, interval)
        }
    }
    //Fired when cancelled
    override fun onCancel(arguments: Any?) {
        sensorManager.unregisterListener(this)
        eventSink = null
    }

    //What gives the actual reading
    override fun onSensorChanged(event: SensorEvent?) {
        val sensorValues = event!!.values[0]
        eventSink?.success(sensorValues)

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}