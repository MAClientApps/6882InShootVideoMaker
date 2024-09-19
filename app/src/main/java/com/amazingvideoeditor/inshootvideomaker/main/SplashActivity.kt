package com.amazingvideoeditor.inshootvideomaker.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.amazingvideoeditor.inshootvideomaker.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            setTheme(R.style.Theme_AppCompat_LauncherScreen)
            val splashScreen: SplashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finish()
        } else {
            setTheme(R.style.Theme_AppCompat_LauncherScreenFallback)
            setContentView(R.layout.activity_splash)
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finish()
        }
    }
}