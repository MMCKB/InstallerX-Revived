// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

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

/**
 * Applies the currently selected installer glass opacity to a surface color.
 * Callers should only use this while [LocalInstallerFrostedGlass] is enabled.
 */
@Composable
fun Color.withInstallerFrostedGlassOpacity(multiplier: Float = 1f): Color {
    val alpha = (LocalInstallerFrostedGlassOpacity.current.coerceIn(35, 90) / 100f) * multiplier
    return copy(alpha = alpha.coerceIn(0f, 1f))
}
