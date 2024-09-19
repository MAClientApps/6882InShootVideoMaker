package com.amazingvideoeditor.inshootvideomaker.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import com.amazingvideoeditor.inshootvideomaker.misc.setImmersiveMode
import com.amazingvideoeditor.inshootvideomaker.misc.setupSystemUi
import com.amazingvideoeditor.inshootvideomaker.settings.SettingsDataStore
import com.amazingvideoeditor.inshootvideomaker.videomaker.InShootVideoMakerActivity

class MainActivity : ComponentActivity() {
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var pickProject: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSystemUi()

        val dataStore = SettingsDataStore(this)
        pickMedia =
            registerForActivityResult(CustomPickVisualMedia { dataStore.getLegacyFilePickerBlocking() }) { uri ->
                if (uri != null) {
                    gotoVideoEditor(uri)
                }
            }
        pickProject = registerForActivityResult(
            CustomOpenDocument()
        ) { uri ->
            if (uri != null) {
                gotoVideoEditor(uri)
            }
        }

        setContent {
            setImmersiveMode(false)
            MainScreen(pickMedia, pickProject)
        }
    }

    private fun gotoVideoEditor(uri: Uri) {
        val intent = Intent(this, InShootVideoMakerActivity::class.java)
        intent.action = Intent.ACTION_EDIT
        intent.data = uri
        startActivity(intent)
    }
}
