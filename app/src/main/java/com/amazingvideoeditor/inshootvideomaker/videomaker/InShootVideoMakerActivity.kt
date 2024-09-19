package com.amazingvideoeditor.inshootvideomaker.videomaker

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amazingvideoeditor.inshootvideomaker.misc.PROJECT_MIME_TYPE
import com.amazingvideoeditor.inshootvideomaker.misc.setImmersiveMode
import com.amazingvideoeditor.inshootvideomaker.misc.setupSystemUi
import com.amazingvideoeditor.inshootvideomaker.R

class InShootVideoMakerActivity : ComponentActivity() {
    private lateinit var createDocument: ActivityResultLauncher<String>
    private lateinit var createProject: ActivityResultLauncher<String>
    private lateinit var requestVideoPermission: ActivityResultLauncher<String>
    private lateinit var viewModel: InShootVideoMakerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSystemUi()

        viewModel = InShootVideoMakerViewModel()

        window.decorView.setOnSystemUiVisibilityChangeListener {
            viewModel.setControlsVisible(it == 0)
        }

        createDocument = registerForActivityResult(
            ActivityResultContracts.CreateDocument("video/mp4")
        ) { uri ->
            uri?.let {
                viewModel.setOutputPath(it.toString())
            }
        }

        createProject = registerForActivityResult(
            ActivityResultContracts.CreateDocument(PROJECT_MIME_TYPE)
        ) { uri ->
            uri?.let {
                viewModel.setProjectOutputPath(it.toString())
            }
        }

        requestVideoPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                recreate()
            } else {
                showToast(getString(R.string.permission_denied_grant_video_permissions))
            }
        }

        handleIntentData(intent)
    }



    private fun handleIntentData(intent: Intent) {
        try {
            var uri: String? = null

            when (intent.action) {
                Intent.ACTION_EDIT, Intent.ACTION_VIEW -> {
                    uri = intent.dataString
                }
                Intent.ACTION_SEND -> {
                    uri = when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)?.toString()
                        }
                        else -> {
                            @Suppress("DEPRECATION")
                            (intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri)?.toString()
                        }
                    }
                }
            }

            uri?.let {
                setContent {
                    viewModel = viewModel { viewModel }
                    val controlsVisible by viewModel.controlsVisible.collectAsState()
                    setImmersiveMode(!controlsVisible)
                    VideoEditorScreen(it, createDocument, createProject, requestVideoPermission)
                }
            } ?: finish()

        } catch (e: Exception) {
            Log.e("VideoEditorActivity", "Error processing intent data", e)
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
