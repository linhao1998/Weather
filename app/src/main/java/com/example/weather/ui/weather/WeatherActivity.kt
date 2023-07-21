package com.example.weather.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weather.R
import com.example.weather.logic.model.HourlyForecast
import com.example.weather.logic.model.PlaceManage
import com.example.weather.logic.model.Weather
import com.example.weather.logic.model.getSky
import com.example.weather.ui.placesearch.PlaceSearchActivity
import com.example.weather.ui.weather.placemanage.PlaceManageAdapter
import com.example.weather.ui.weather.placemanage.PlaceManageViewModel
import com.example.weather.ui.weather.weathershow.HourlyAdapter
import com.example.weather.ui.weather.weathershow.WeatherShowViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherActivity : AppCompatActivity() {

    private lateinit var placeName: TextView

    private lateinit var currentTemp: TextView

    private lateinit var currentSky: TextView

    private lateinit var currentAQI: TextView

    private lateinit var nowLayout: RelativeLayout

    private lateinit var forecastLayout: LinearLayout

    private lateinit var coldRiskText: TextView

    private lateinit var dressingText: TextView

    private lateinit var ultravioletText: TextView

    private lateinit var carWashingText: TextView

    private lateinit var weatherLayout: ScrollView

    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var navBtn: Button

    private lateinit var hourlyRecyclerView: RecyclerView

    private val hourlyForecastList = ArrayList<HourlyForecast>()

    private lateinit var hourlyAdapter: HourlyAdapter

    private lateinit var searchPlaceEntrance: EditText

    private lateinit var addBtn: Button

    private lateinit var placeManageRecyclerView: RecyclerView

    private lateinit var placeManageAdapter: PlaceManageAdapter

    lateinit var drawerLayout: DrawerLayout

    val weatherViewModel  by lazy { ViewModelProvider(this).get(WeatherShowViewModel::class.java) }

    val placeManageViewModel by lazy {ViewModelProvider(this).get(PlaceManageViewModel::class.java)}

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT

        placeName = findViewById(R.id.placeName)
        currentTemp = findViewById(R.id.currentTemp)
        currentSky = findViewById(R.id.currentSky)
        currentAQI = findViewById(R.id.currentAQI)
        nowLayout = findViewById(R.id.nowLayout)
        forecastLayout = findViewById(R.id.forecastLayout)
        coldRiskText = findViewById(R.id.coldRiskText)
        dressingText = findViewById(R.id.dressingText)
        ultravioletText = findViewById(R.id.ultravioletText)
        carWashingText = findViewById(R.id.carWashingText)
        weatherLayout = findViewById(R.id.weatherLayout)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        navBtn = findViewById(R.id.navBtn)
        drawerLayout = findViewById(R.id.drawerLayout)
        hourlyRecyclerView = findViewById(R.id.hourlyRecyclerView)
        searchPlaceEntrance = findViewById(R.id.searchPlaceEntrance)
        addBtn = findViewById(R.id.addBtn)
        placeManageRecyclerView = findViewById(R.id.placeManageRecyclerView)

        //启动PlaceSearchActivity
        searchPlaceEntrance.setOnClickListener {
            val intent = Intent(this, PlaceSearchActivity::class.java).apply {
                putExtra("FROM_ACTIVITY","WeatherActivity")
            }
            startActivity(intent)
        }

        //设置24小时预报的RecyclerView
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        hourlyRecyclerView.layoutManager = layoutManager
        hourlyAdapter = HourlyAdapter(hourlyForecastList)
        hourlyRecyclerView.adapter = hourlyAdapter

        //设置地点管理的RecyclerView
        val layoutManager2 = LinearLayoutManager(this)
        placeManageRecyclerView.layoutManager = layoutManager2
        placeManageAdapter = PlaceManageAdapter(this,placeManageViewModel.placeManageList)
        placeManageRecyclerView.adapter = placeManageAdapter

        if (weatherViewModel.locationLng.isEmpty()) {
            weatherViewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (weatherViewModel.locationLat.isEmpty()) {
            weatherViewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (weatherViewModel.placeName.isEmpty()) {
            weatherViewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        if (weatherViewModel.placeAddress.isEmpty()) {
            weatherViewModel.placeAddress = intent.getStringExtra("place_address") ?: ""
        }

        weatherViewModel.weatherLiveData.observe(this) { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing = false
        }

        placeManageViewModel.placeManageLiveData.observe(this) { result ->
            val placeManages = result.getOrNull()
            if (placeManages != null) {
                placeManageViewModel.placeManageList.clear()
                placeManageViewModel.placeManageList.addAll(placeManages)
                placeManageAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this,"无法获取地点管理数据", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        }

        placeManageViewModel.toastLiveData.observe(this) { it ->
            if (it != "") {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        swipeRefresh.setColorSchemeResources(R.color.royal_blue)
        refreshWeather()                    //刷新天气
        placeManageViewModel.refreshPlaceManage()     //刷新地点管理

        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        navBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        addBtn.setOnClickListener {
            drawerLayout.open()
            val addPlaceManage = PlaceManage(weatherViewModel.placeName,weatherViewModel.locationLng,weatherViewModel.locationLat,
                                weatherViewModel.placeAddress,weatherViewModel.placeRealtimeTem,weatherViewModel.placeSkycon)
            placeManageViewModel.addPlaceManage(addPlaceManage)
        }
        drawerLayout.addDrawerListener(object: DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        drawerLayout.closeDrawers()
        weatherViewModel.locationLng = intent?.getStringExtra("location_lng") ?: ""
        weatherViewModel.locationLat = intent?.getStringExtra("location_lat") ?: ""
        weatherViewModel.placeName = intent?.getStringExtra("place_name") ?: ""
        weatherViewModel.placeAddress = intent?.getStringExtra("place_address") ?: ""
        refreshWeather()
    }

    override fun onStop() {
        super.onStop()
        placeManageViewModel.clearToast()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showWeatherInfo(weather: Weather) {
        placeName.text = weatherViewModel.placeName
        val realtime = weather.realtime
        val hourly = weather.hourly
        val daily = weather.daily
        // 填充now.xml布局中的数据
        val realtimeTemInt = realtime.temperature.toInt()
        val currentSkyInfo = getSky(realtime.skycon).info
        val currentTempText = "$realtimeTemInt ℃"
        currentTemp.text = currentTempText
        currentSky.text = currentSkyInfo
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        //将网络请求的当前温度和Skycon保存到ViewModel中
        weatherViewModel.placeRealtimeTem = realtimeTemInt
        weatherViewModel.placeSkycon = currentSkyInfo

        //是否是点击地点管理中的地点更新该地点温度和天气信息
        if (weatherViewModel.isUpdatePlaceManage) {
            val updatePlaceManage = PlaceManage(weatherViewModel.placeName,weatherViewModel.locationLng,
                weatherViewModel.locationLat, weatherViewModel.placeAddress,
                weatherViewModel.placeRealtimeTem,weatherViewModel.placeSkycon)
            placeManageViewModel.updatePlaceManage(updatePlaceManage)
            weatherViewModel.isUpdatePlaceManage = false
        }

        //填充forecast_hourly.xml布局中的数据
        hourlyForecastList.clear()
        val hours = hourly.skycon.size
        for (i in 0 until hours) {
            val temVal = hourly.temperature[i].value
            val skyVal = hourly.skycon[i].value
            val datetime = hourly.skycon[i].datetime
            hourlyForecastList.add(HourlyForecast(temVal, skyVal, datetime))
        }
        hourlyAdapter.notifyDataSetChanged()

        // 填充forecast_daily.xml布局中的数据
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_daily_item,
                forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())
            val dateInfoStr = when(i) {
                0 -> "今天  ${simpleDateFormat.format(skycon.date)}"
                1 -> "明天  ${simpleDateFormat.format(skycon.date)}"
                else -> "${getDayOfWeek(skycon.date)}  ${simpleDateFormat.format(skycon.date)}"
            }
            dateInfo.text = dateInfoStr
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }
        // 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE
    }

    fun refreshWeather() {
        weatherViewModel.refreshWeather(weatherViewModel.locationLng,weatherViewModel.locationLat)
        swipeRefresh.isRefreshing = true
    }

    private fun getDayOfWeek(date: Date): String {
        val sdf = SimpleDateFormat("E", Locale.getDefault())
        return sdf.format(date)
    }
}