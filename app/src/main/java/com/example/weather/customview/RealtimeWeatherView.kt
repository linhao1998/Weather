package com.example.weather.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.weather.R

class RealtimeWeatherView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private var aqBitmap: Bitmap
    private var tempBitmap: Bitmap
    private var wsBitmap: Bitmap
    private var humBitmap: Bitmap

    private val defaultWidth = 610
    private val defaultHeight = 500

    private var rotationCenterX = 0f
    private var rotationCenterY = 0f

    private var temperature: Int? = null
    private var skycon: String? = null
    private var airQuality: Int? = null
    private var apparentTemp: Int? = null
    private var humidity: Int? = null
    private var windDir = 0f
    private var windScale: Int? = null

    init {
        paint.isAntiAlias = true
        aqBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_air_quality)
        tempBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_temperature)
        wsBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_wind_direction)
        humBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_humidity)
        rotationCenterX = wsBitmap.width / 2 + 245f
        rotationCenterY = wsBitmap.height / 2 + 375f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = defaultWidth
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = defaultHeight
        }

        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.textSize = 225f
        canvas.drawText("${temperature}°", 150f, 250f, paint)
        paint.textSize = 40f
        canvas.drawText("$skycon", 450f, 250f, paint)
        canvas.drawBitmap(aqBitmap, 170f, 305f, paint)
        paint.textSize = 38f
        canvas.drawText("空气指数:   $airQuality", 220f, 335f, paint)
        canvas.drawBitmap(tempBitmap, 70f, 360f, paint)
        paint.textSize = 40f
        canvas.drawText("${apparentTemp}°", 140f, 410f, paint)
        canvas.save()
        canvas.rotate(windDir + 145, rotationCenterX, rotationCenterY)
        canvas.drawBitmap(wsBitmap, 245f, 375f, paint)
        canvas.restore()
        canvas.drawText("${windScale}级", 310f, 410f, paint)
        canvas.drawBitmap(humBitmap, 390f, 360f, paint)
        canvas.drawText("${humidity}%", 460f, 410f, paint)
    }

    fun setRealtimeWeather(temperature: Int, skycon: String, airQuality: Int,
                           apparentTemp: Int, windDir: Float, windScale: Int, humidity: Int) {
        this.temperature = temperature
        this.skycon = skycon
        this.airQuality = airQuality
        this.apparentTemp = apparentTemp
        this.windDir = windDir
        this.windScale = windScale
        this.humidity = humidity
        invalidate()
    }
}