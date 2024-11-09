package com.manikandareas.stories.core.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StoriesOutlineBox(
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Black,
    borderWidth: Dp = 2.dp,
    dashLength: Dp = 10.dp,
    spaceLength: Dp = 5.dp,
    backgroundColor: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokeWidthPx = borderWidth.toPx()
            val dashLengthPx = dashLength.toPx()
            val spaceLengthPx = spaceLength.toPx()

            drawRoundRect(
                color = borderColor,
                style = Stroke(
                    width = strokeWidthPx,
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(dashLengthPx, spaceLengthPx), 0f
                    ),
                    cap = StrokeCap.Round
                )
            )
        }
        content()
    }
}