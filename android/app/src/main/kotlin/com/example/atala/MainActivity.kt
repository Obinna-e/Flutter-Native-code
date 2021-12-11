package com.example.atala


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    //val is basically final in Kotlin

    private val METHOD_CHANNEL_NAME = "com.julow.barometer/method"
    private val PRESSURE_CHANNEL_NAME = "com.julow.barometer/pressure"

    private var methodChannel: MethodChannel? = null
    private lateinit var sensorManager: SensorManager
    private var pressureChannel: EventChannel? = null
    private var pressureStreamHandler: StreamHandler? = null


    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        //Setup Channels
        setupChannels(this, flutterEngine.dartExecutor.binaryMessenger)
    }

    override fun onDestroy() {
        teardownChannels()
        super.onDestroy()
    }
    private fun setupChannels(context:Context, messenger: BinaryMessenger) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        methodChannel = MethodChannel(messenger, METHOD_CHANNEL_NAME)
        //setup call handler
        methodChannel!!.setMethodCallHandler{
            call,result ->
            if (call.method == "isSensorAvailable") {
                result.success(sensorManager!!.getSensorList(Sensor.TYPE_PRESSURE).isNotEmpty())

            } else {
                result.notImplemented()
            }
        }
        //get channel, create instance of a handler and then link handler to channel
        pressureChannel = EventChannel(messenger, PRESSURE_CHANNEL_NAME)
        pressureStreamHandler = StreamHandler(sensorManager!!, Sensor.TYPE_PRESSURE)
        pressureChannel!!.setStreamHandler(pressureStreamHandler)
    }


    private fun teardownChannels() {
        //!! forces methodChannel to open
        methodChannel!!.setMethodCallHandler(null)
        pressureChannel!!.setStreamHandler(null)
    }

}
