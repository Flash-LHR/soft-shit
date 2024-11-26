package com.example.webviewapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar

class SettingsActivity : AppCompatActivity() {

    private val defaultURL = "https://flash-lhr.github.io/goal_and_vocation/"

    // SharedPreferences 用于保存设置
    private val sharedPreferences by lazy {
        getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val editTextUrl: EditText = findViewById(R.id.editTextUrl)
        val btnSaveUrl: Button = findViewById(R.id.btnSaveUrl)
        val btnResetToDefault: Button = findViewById(R.id.btnResetToDefault)

        // 获取默认URL或之前保存的URL
        val savedUrl =
            sharedPreferences.getString(
                "webViewUrl",
                defaultURL
            )
        editTextUrl.setText(savedUrl)

        // 保存新的 URL 配置
        btnSaveUrl.setOnClickListener {
            val newUrl = editTextUrl.text.toString()
            if (newUrl.isNotBlank()) {
                val editor = sharedPreferences.edit()
                editor.putString("webViewUrl", newUrl)
                editor.apply()

                // 提示保存成功
                Snackbar.make(
                    findViewById(R.id.editTextUrl),
                    "URL 保存成功",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                // 提示 URL 为空
                Snackbar.make(
                    findViewById(R.id.editTextUrl),
                    "请输入有效的 URL",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        // 重置配置为默认 URL
        btnResetToDefault.setOnClickListener {
            val editor = sharedPreferences.edit()

            // 重置 URL 配置为默认值
            editor.putString("webViewUrl", defaultURL)
            editor.apply()

            // 提示重置成功
            Snackbar.make(
                findViewById(R.id.editTextUrl),
                "设置已重置为默认",
                Snackbar.LENGTH_SHORT
            ).show()

            // 更新 EditText 内容为默认 URL
            editTextUrl.setText(defaultURL)
        }


        // 获取 ImageView
        val gifImageView = findViewById<ImageView>(R.id.gifImageView)
        // 加载 GIF
        Glide.with(this)
            .asGif() // 显示 GIF
            .load(R.drawable.jiabo) // 替换为您自己的 GIF 资源文件
            .diskCacheStrategy(DiskCacheStrategy.ALL) // 设置缓存策略
            .into(gifImageView) // 加载到 ImageView
    }
}


