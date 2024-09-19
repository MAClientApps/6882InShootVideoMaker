package com.amazingvideoeditor.inshootvideomaker.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.amazingvideoeditor.inshootvideomaker.misc.setImmersiveMode
import com.amazingvideoeditor.inshootvideomaker.misc.setupSystemUi

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSystemUi()

        setContent {
            setImmersiveMode(false)
            SettingsScreen()
        }
    }
}
