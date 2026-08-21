// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Enables the Material 3 installer-only frosted-glass treatment.
 *
 * The installer keeps its Material 3 layout and semantics; only its visual
 * surfaces become translucent so the dialog window's real-time blur can show through.
 */
val LocalInstallerFrostedGlass = staticCompositionLocalOf { false }
