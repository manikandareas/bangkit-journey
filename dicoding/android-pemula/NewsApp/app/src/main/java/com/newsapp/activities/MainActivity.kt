package com.newsapp.activities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.newsapp.R
import com.newsapp.helpers.Api

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        fetchAllNewsPortals()
        fetchNewsByCategory("cnn", "terbaru")
    }

    private fun fetchAllNewsPortals() {
        lifecycleScope.launch {
            try {
                val response = Api.newsRepository.getAllNewsPortal()

                Log.d(TAG, "Data: ${response.toString()}")
                if (response.isSuccessful) {
                    val newsPortals = response.body()
                    Log.d(TAG, "News Portals fetched successfully")
                    Log.d(TAG, "News Portals: $newsPortals")
                    newsPortals?.let { portalsRes ->
                        Log.d(TAG, "All News Portals:")
                        portalsRes.endpoints.forEach { portal ->
                            Log.d(TAG, "Portal: ${portal.name}")
                        }
                        findViewById<TextView>(R.id.textView).text = portalsRes.maintainer
                    } ?: Log.d(TAG, "News Portals data is null")
                } else {
                    Log.e(TAG, "Error fetching news portals: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception when fetching news portals", e)
            }
        }
    }

    private fun fetchNewsByCategory(portal: String, category: String) {
        lifecycleScope.launch {
            try {
                val response = Api.newsRepository.getByCategoryNews(portal, category)
                Log.d(TAG, "Data: ${response.body()}")
                if (response.isSuccessful) {
                    val newsCategory = response.body()
                    Log.d(TAG, "News Category fetched successfully")
                    Log.d(TAG, "News Category: $newsCategory")
                    newsCategory?.let { categoryRes ->
                        Log.d(TAG, "News Category: ${categoryRes.data}")
                        findViewById<TextView>(R.id.textView).text = categoryRes.data.title
                    } ?: Log.d(TAG, "News Category data is null")
                } else {
                    Log.e(TAG, "Error fetching news category: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception when fetching news category", e)
            }
        }
    }

}