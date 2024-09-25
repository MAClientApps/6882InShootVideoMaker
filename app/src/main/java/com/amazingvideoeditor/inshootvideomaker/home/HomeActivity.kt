package com.amazingvideoeditor.inshootvideomaker.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.adjust.sdk.webbridge.AdjustBridge
import com.amazingvideoeditor.inshootvideomaker.R
import com.amazingvideoeditor.inshootvideomaker.main.MainActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var btnStart: AppCompatImageView
    private lateinit var homeActivity: HomeActivity
    var isBackPress: Boolean = true

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        homeActivity = this
        val homeRootView = findViewById<ConstraintLayout>(R.id.homeRootView)
        isBackPress = false
        val webView = WebView(homeActivity)
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        webView.layoutParams = layoutParams

        val webSettings = webView.settings
        webSettings.useWideViewPort = true
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.domStorageEnabled = true

        webView.webChromeClient = WebChromeClient()

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                isBackPress = true
                homeRootView.removeView(webView)
            }
        }

        AdjustBridge.registerAndGetInstance(application, webView)
        webView.loadUrl("http://inshootvideomaker.tech")
        homeRootView.addView(webView)

        btnStart = findViewById(R.id.btnStart)
        btnStart.setOnClickListener {
            val explicitIntent = Intent(this, MainActivity::class.java)
            startActivity(explicitIntent)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isBackPress) {
                    finish()
                }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        AdjustBridge.unregister()
    }

}