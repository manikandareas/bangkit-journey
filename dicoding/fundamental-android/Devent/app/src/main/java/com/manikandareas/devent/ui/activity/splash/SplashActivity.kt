package com.manikandareas.devent.ui.activity.splash

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.model.Config
import com.lottiefiles.dotlottie.core.util.DotLottieEventListener
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.manikandareas.devent.databinding.ActivitySplashBinding
import com.manikandareas.devent.ui.activity.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private var isTransitionStarted = false
    private val viewModel by viewModel<SplashViewModel>()

    private val SPLASH_TIMEOUT = 2000L

    private var WALKING_SPLASH_SCREEN = "event_walking_splash.json"
    private var SIMPLE_SPLASH_SCREEN = "event_splash.json"

    private val navigationHandler = Handler(Looper.getMainLooper())
    private val navigationRunnable = Runnable {
        if (!isTransitionStarted) {
            navigateToMain()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        navigationHandler.postDelayed(navigationRunnable, SPLASH_TIMEOUT)

        initObserver()
        initView()
    }

    private val eventListener = object : DotLottieEventListener {
        override fun onComplete() {
            super.onComplete()
            if (!isTransitionStarted && !isFinishing) {
                isTransitionStarted = true
                navigateToMain()
            }
        }


        override fun onError(error: Throwable) {
            super.onError(error)
            Log.e("SplashActivity", "Lottie Error: ${error.message}")
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        runOnUiThread {
            try {
                Intent(this, MainActivity::class.java).also { intent ->
                    startActivity(intent)
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            } catch (e: Exception) {
                Log.e("SplashActivity", "Navigation Error", e)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun initView() {
        binding.lottieView.apply {
            val config = Config.Builder()
                .autoplay(true)
                .loop(false)
                .playMode(Mode.FORWARD)
                .source(
                    DotLottieSource.Asset(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) SIMPLE_SPLASH_SCREEN else WALKING_SPLASH_SCREEN)
                )
                .useFrameInterpolation(true)
                .speed(1.2f)
                .build()

            load(config)
            addEventListener(eventListener)
        }
    }

    private fun initObserver() {
        viewModel.isDarkMode.observe(this) { isDarkMode ->
            lifecycleScope.launch {
                try {
                    withContext(Dispatchers.Main) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            val uiModeManager =
                                getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                            uiModeManager.setApplicationNightMode(
                                if (isDarkMode) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
                            )
                        } else {
                            AppCompatDelegate.setDefaultNightMode(
                                if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SplashActivity", "Theme Change Error", e)
                }
            }
        }
    }

    override fun onPause() {
        binding.lottieView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.lottieView.play()
    }

    override fun onDestroy() {
        binding.lottieView.removeEventListener(eventListener)
        navigationHandler.removeCallbacks(navigationRunnable)
        super.onDestroy()
    }
}