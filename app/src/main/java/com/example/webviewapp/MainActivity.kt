package com.example.webviewapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var clickCount = 0 // 连续点击次数
    private val maxClicks = 7 // 设置需要连续点击的次数
    private val clickTimeout = 500L // 设置点击之间的最大间隔，单位是毫秒
    private var lastClickTime = 0L // 记录上次点击的时间
    private val handler = Handler() // 用来延迟重置点击计数
    private val defaultURL = "https://flash-lhr.github.io/goal_and_vocation/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        // 配置 WebView
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)      // 启用缩放
        webView.settings.builtInZoomControls = true // 启用内置缩放控件（+/- 按钮）
        webView.settings.displayZoomControls = false // 隐藏缩放控件按钮
        webView.settings.useWideViewPort = true // 支持使用广角视图

        // 从 SharedPreferences 中加载 URL
        val sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val defaultUrl =
            sharedPreferences.getString("webViewUrl", defaultURL)
        webView.settings.userAgentString =
            "Mozilla/5.0 (Linux; Android 10; SM-G975F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36"
        webView.loadUrl(defaultUrl!!)

        // 设置 WebView 的点击事件监听器
        webView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                handleClick()
            }
            // 返回 false 让 WebView 继续响应其他触摸事件
            false
        }
    }

    override fun onResume() {
        super.onResume()
        // 每次返回主页面时，重新加载设置的 URL
        val sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val defaultUrl =
            sharedPreferences.getString("webViewUrl", defaultURL)
        val webView = findViewById<WebView>(R.id.webView)
        webView.loadUrl(defaultUrl!!)
    }

    private fun navigateToSettings() {
        // 跳转到设置页面
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "快速连点7次打开设置", Toast.LENGTH_SHORT).show()
    }

    private fun handleClick() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime <= clickTimeout) {
            clickCount++
        } else {
            clickCount = 1 // 重置计数器
        }

        lastClickTime = currentTime

        // 判断点击次数是否达到要求
        if (clickCount >= maxClicks) {
            navigateToSettings()
            clickCount = 0 // 重置点击计数
        } else {
            // 每次点击后，设置一个延时来重置计数器
            handler.removeCallbacksAndMessages(null) // 移除所有以前的延时任务
            handler.postDelayed({ clickCount = 0 }, clickTimeout)
        }
    }
}
