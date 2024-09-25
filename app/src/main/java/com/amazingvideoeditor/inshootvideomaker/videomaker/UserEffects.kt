package com.amazingvideoeditor.inshootvideomaker.videomaker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Tv
import androidx.media3.effect.Presentation
import androidx.media3.effect.RgbFilter
import androidx.media3.effect.ScaleAndRotateTransformation
import com.amazingvideoeditor.inshootvideomaker.R
import com.amazingvideoeditor.inshootvideomaker.misc.validateFloat
import com.amazingvideoeditor.inshootvideomaker.misc.validateFloatAndNonzero
import com.amazingvideoeditor.inshootvideomaker.misc.validateUIntAndNonzero
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

val userEffectsArray: ImmutableList<UserEffect> = persistentListOf(
    UserEffect(
        R.string.grayscale,
        { Icons.Filled.Filter }) { RgbFilter.createGrayscaleFilter() },
    UserEffect(
        R.string.invert_colors,
        { Icons.Filled.InvertColors }) { RgbFilter.createInvertedFilter() }
)

val dialogUserEffectsArray: ImmutableList<DialogUserEffect> = persistentListOf(
    DialogUserEffect(
        R.string.resolution,
        { Icons.Filled.Tv },
        persistentListOf(
            EffectDialogSetting(key = "Width", R.string.width, textfieldValidation = {
                validateUIntAndNonzero(it)
            }
            ),
            EffectDialogSetting(key = "Height", R.string.height, textfieldValidation =
            {
                validateUIntAndNonzero(it)
            }
            ),
            EffectDialogSetting(
                key = "Layout", R.string.layout, dropdownOptions =
                mutableListOf(
                    "Scale to fit",
                    "Scale to fit with crop",
                    "Stretch to fit",
                )
            )
        )
    ) { args ->
        val width = args["Width"]!!.toInt()
        val height = args["Height"]!!.toInt()
        val layout: Int = when (args["Layout"]) {
            "Scale to fit" -> Presentation.LAYOUT_SCALE_TO_FIT
            "Scale to fit with crop" -> Presentation.LAYOUT_SCALE_TO_FIT_WITH_CROP
            "Stretch to fit" -> Presentation.LAYOUT_STRETCH_TO_FIT
            else -> Presentation.LAYOUT_SCALE_TO_FIT
        }
        { Presentation.createForWidthAndHeight(width, height, layout) }
    },
    DialogUserEffect(
        R.string.scale,
        { Icons.Filled.FormatSize },
        persistentListOf(
            EffectDialogSetting(key = "X", R.string.x, textfieldValidation = {
                validateFloatAndNonzero(it)
            }
            ),
            EffectDialogSetting(key = "Y", R.string.y, textfieldValidation =
            {
                validateFloatAndNonzero(it)
            }
            )
        )
    ) { args ->
        val x = args["X"]!!.toFloat()
        val y = args["Y"]!!.toFloat();
        { ScaleAndRotateTransformation.Builder().setScale(x, y).build() }
    },
    DialogUserEffect(
        R.string.rotate,
        { Icons.AutoMirrored.Filled.RotateRight },
        persistentListOf(
            EffectDialogSetting(
                key = "Degrees",
                stringResId = R.string.degrees,
                textfieldValidation = {
                    validateFloat(it)
                }
            )
        )
    ) { args ->
        val degrees = args["Degrees"]!!.toFloat();
        { ScaleAndRotateTransformation.Builder().setRotationDegrees(degrees).build() }
    }
)

