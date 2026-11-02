package org.advancedkeyboard.app.themes

import android.graphics.Color

data class KeyboardTheme(
    val backgroundColor: Int,
    val surfaceColor: Int,
    val keyColor: Int,
    val keyPressedColor: Int,
    val textPrimaryColor: Int,
    val textSecondaryColor: Int,
    val accentColor: Int,
    val accentLockedColor: Int,
    val borderColor: Int
) {
    companion object {
        fun get(type: ThemeType): KeyboardTheme {
            return when (type) {
                ThemeType.DARK_SLATE -> KeyboardTheme(
                    backgroundColor = Color.parseColor("#1E222A"),
                    surfaceColor = Color.parseColor("#282C34"),
                    keyColor = Color.parseColor("#353B45"),
                    keyPressedColor = Color.parseColor("#4B5263"),
                    textPrimaryColor = Color.parseColor("#E5E9F0"),
                    textSecondaryColor = Color.parseColor("#ABB2BF"),
                    accentColor = Color.parseColor("#61AFEF"),
                    accentLockedColor = Color.parseColor("#98C379"),
                    borderColor = Color.parseColor("#3E4451")
                )
                ThemeType.LIGHT_MINIMAL -> KeyboardTheme(
                    backgroundColor = Color.parseColor("#ECEFF4"),
                    surfaceColor = Color.parseColor("#E5E9F0"),
                    keyColor = Color.parseColor("#FFFFFF"),
                    keyPressedColor = Color.parseColor("#D8DEE9"),
                    textPrimaryColor = Color.parseColor("#2E3440"),
                    textSecondaryColor = Color.parseColor("#4C566A"),
                    accentColor = Color.parseColor("#5E81AC"),
                    accentLockedColor = Color.parseColor("#A3BE8C"),
                    borderColor = Color.parseColor("#D8DEE9")
                )
                ThemeType.AMOLED_BLACK -> KeyboardTheme(
                    backgroundColor = Color.parseColor("#000000"),
                    surfaceColor = Color.parseColor("#121212"),
                    keyColor = Color.parseColor("#1E1E1E"),
                    keyPressedColor = Color.parseColor("#333333"),
                    textPrimaryColor = Color.parseColor("#FFFFFF"),
                    textSecondaryColor = Color.parseColor("#888888"),
                    accentColor = Color.parseColor("#00E5FF"),
                    accentLockedColor = Color.parseColor("#00E676"),
                    borderColor = Color.parseColor("#282828")
                )
            }
        }
    }
}
