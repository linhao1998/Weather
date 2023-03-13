package com.example.weather.ui.placesearch

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.ui.weather.WeatherActivity

class PlaceSearchActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(PlaceSearchViewModel::class.java) }

    private lateinit var adapter: PlaceSearchAdapter

    private lateinit var recyclerView: RecyclerView

    private lateinit var searchPlaceEdit: EditText

    private lateinit var bgImageView: ImageView

    private lateinit var cancelBtn: TextView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_search)

        val fromActivity = intent.getStringExtra("FROM_ACTIVITY")
        if (fromActivity == null && viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(this, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
                putExtra("place_address",place.address)
            }
            startActivity(intent)
            finish()
            return
        }

        recyclerView = findViewById(R.id.recyclerView)
        searchPlaceEdit = findViewById(R.id.searchPlaceEdit)
        bgImageView = findViewById(R.id.bgImageView)
        cancelBtn = findViewById(R.id.cancelBtn)

        //获取searchPlaceEdit焦点，弹出软键盘
        searchPlaceEdit.requestFocus()


        //设置地点搜索的RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = PlaceSearchAdapter(this,viewModel.placeList)
        recyclerView.adapter = adapter

        searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        cancelBtn.setOnClickListener {
            finish()
        }

        viewModel.placeLiveData.observe(this, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this,"未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}