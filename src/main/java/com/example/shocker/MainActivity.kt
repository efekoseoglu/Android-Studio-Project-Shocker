package com.example.shocker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import android.view.contentcapture.ContentCaptureCondition
import android.widget.Toast

class MainActivity : AppCompatActivity(),SensorEventListener {

    var sensor:Sensor?=null
    var sensorManager:SensorManager?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager=getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor= sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    var x_old=0.0
    var y_old=0.0
    var z_old=0.0
    var threshold=3000.0
    var old_time:Long=0

    override fun onSensorChanged(event: SensorEvent?) {
        var x=event!!.values[0]
        var y=event!!.values[1]
        var z=event!!.values[2]
        var currentTime=System.currentTimeMillis()

        if((currentTime-old_time)>100){//adding this to reduce number of executing of the code

            var timeDiff=currentTime-old_time
            old_time=currentTime
            var Speed=Math.abs(x+y+z-x_old-y_old-z_old)/timeDiff*10000//calculating speed

            if(Speed>threshold){//if speed is greater than threshold vibrate
                var v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                v.vibrate(500)
                Toast.makeText(applicationContext,"shock",Toast.LENGTH_LONG).show()//show alert when its vibrated
            }

        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }
}