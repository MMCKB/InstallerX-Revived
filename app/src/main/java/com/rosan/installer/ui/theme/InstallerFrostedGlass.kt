// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Enables the Material 3 installer-only frosted-glass treatment.
 *
 * The installer keeps its Material 3 layout and semantics; only its visual
 * surfaces become translucent so the dialog window's real-time blur can show through.
 */
val LocalInstallerFrostedGlass = staticCompositionLocalOf { false }

/** The real-time window blur radius used by Material 3 installer surfaces. */
val LocalInstallerFrostedGlassBlurRadius = staticCompositionLocalOf { 30 }

/** The alpha percentage used by Material 3 installer glass surfaces. */
val LocalInstallerFrostedGlassOpacity = staticCompositionLocalOf { 72 }

/** Enables the optional liquid-glass highlights on Material 3 installer surfaces. */
val LocalInstallerLiquidGlass = staticCompositionLocalOf { false }

/** Percentage controlling the white specular highlight applied to liquid glass. */
val LocalInstallerLiquidGlassHighlight = staticCompositionLocalOf { 42 }

/** Percentage controlling the prismatic edge tint applied to liquid glass. */
val LocalInstallerLiquidGlassDispersion = staticCompositionLocalOf { 28 }

/**
 * Applies the currently selected installer glass opacity to a surface color.
 * Callers should only use this while [LocalInstallerFrostedGlass] is enabled.
 */
@Composable
fun Color.withInstallerFrostedGlassOpacity(multiplier: Float = 1f): Color {
    val alpha = (LocalInstallerFrostedGlassOpacity.current.coerceIn(35, 90) / 100f) * multiplier
    return copy(alpha = alpha.coerceIn(0f, 1f))
}

/**
 * Draws a composited liquid-glass edge over an existing glass surface.
 *
 * The effect is intentionally limited to lightweight Compose canvas strokes rather than a
 * full-screen shader: it preserves the real-time window blur while keeping battery and GPU
 * pressure low on installation screens.
 */
@Composable
fun Modifier.installerLiquidGlassEffect(shape: Shape): Modifier {
    if (!LocalInstallerLiquidGlass.current) return this

    val highlight = LocalInstallerLiquidGlassHighlight.current.coerceIn(0, 100) / 100f
    val dispersion = LocalInstallerLiquidGlassDispersion.current.coerceIn(0, 100) / 100f
    val edgeWidth = (1f + dispersion * 1.5f).dp
    val prismAlpha = 0.04f + dispersion * 0.22f
    val highlightAlpha = 0.04f + highlight * 0.28f

    return this.drawWithContent {
        drawContent()

        val outline = shape.createOutline(size, layoutDirection, this)
        if (dispersion > 0f) {
            drawInstallerLiquidOutline(
                outline = outline,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF80D8FF).copy(alpha = prismAlpha),
                        Color(0xFFFF8FD8).copy(alpha = prismAlpha * 0.85f),
                        Color(0xFFFFE082).copy(alpha = prismAlpha)
                    ),
                    start = Offset.Zero,
                    end = Offset(size.width, size.height)
                ),
                width = edgeWidth.toPx()
            )
        }
        if (highlight > 0f) {
            drawInstallerLiquidOutline(
                outline = outline,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = highlightAlpha),
                        Color.White.copy(alpha = highlightAlpha * 0.4f)
                    ),
                    start = Offset.Zero,
                    end = Offset(size.width, size.height)
                ),
                width = 1.dp.toPx()
            )
        }
    }
}

private fun DrawScope.drawInstallerLiquidOutline(
    outline: Outline,
    brush: Brush,
    width: Float
) {
    val style = Stroke(width = width)
    when (outline) {
        is Outline.Rectangle -> drawRect(
            brush = brush,
            topLeft = outline.rect.topLeft,
            size = outline.rect.size,
            style = style
        )

        is Outline.Rounded -> drawRoundRect(
            brush = brush,
            topLeft = outline.roundRect.topLeft,
            size = outline.roundRect.size,
            cornerRadius = outline.roundRect.topLeftCornerRadius,
            style = style
        )

        is Outline.Generic -> drawPath(
            path = outline.path,
            brush = brush,
            style = style
        )
    }
}
