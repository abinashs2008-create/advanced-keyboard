package org.advancedkeyboard.app.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import org.advancedkeyboard.app.themes.ThemeType

class KeyboardPreferences(context: Context) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        const val PREF_KEY_HEIGHT_SCALE = "pref_height_scale"
        const val PREF_KEY_FONT_SIZE_SCALE = "pref_font_size_scale"
        const val PREF_KEY_THEME = "pref_theme"
        const val PREF_KEY_NUMBER_ROW = "pref_show_number_row"
        const val PREF_KEY_KEY_BORDERS = "pref_show_key_borders"
        const val PREF_KEY_ONE_SHOT_MODIFIERS = "pref_modifier_one_shot"
        const val PREF_KEY_LOCK_MODIFIERS = "pref_modifier_lock_double_tap"
        const val PREF_KEY_SPACEBAR_TRACKPAD = "pref_spacebar_trackpad"
        const val PREF_KEY_VIBRATE = "pref_vibrate_on_keypress"
        const val PREF_KEY_VIBRATION_STRENGTH = "pref_vibration_strength"
        const val PREF_KEY_SOUND = "pref_sound_on_keypress"
    }

    var fontSizeScale: Float
        get() {
            val str = prefs.getString(PREF_KEY_FONT_SIZE_SCALE, "0.85")
            return str?.toFloatOrNull() ?: 0.85f
        }
        set(value) = prefs.edit().putString(PREF_KEY_FONT_SIZE_SCALE, value.toString()).apply()

    var themeType: ThemeType
        get() = try { ThemeType.valueOf(prefs.getString(PREF_KEY_THEME, ThemeType.DARK_SLATE.name) ?: ThemeType.DARK_SLATE.name) } catch (_: Exception) { ThemeType.DARK_SLATE }
        set(value) = prefs.edit().putString(PREF_KEY_THEME, value.name).apply()

    var showNumberRow: Boolean
        get() = prefs.getBoolean(PREF_KEY_NUMBER_ROW, true)
        set(value) = prefs.edit().putBoolean(PREF_KEY_NUMBER_ROW, value).apply()

    var showKeyBorders: Boolean
        get() = prefs.getBoolean(PREF_KEY_KEY_BORDERS, true)
        set(value) = prefs.edit().putBoolean(PREF_KEY_KEY_BORDERS, value).apply()

    var vibrateOnKeypress: Boolean
        get() = prefs.getBoolean(PREF_KEY_VIBRATE, true)
        set(value) = prefs.edit().putBoolean(PREF_KEY_VIBRATE, value).apply()

    var vibrationStrengthMs: Long
        get() = prefs.getLong(PREF_KEY_VIBRATION_STRENGTH, 20L)
        set(value) = prefs.edit().putLong(PREF_KEY_VIBRATION_STRENGTH, value).apply()

    var soundOnKeypress: Boolean
        get() = prefs.getBoolean(PREF_KEY_SOUND, false)
        set(value) = prefs.edit().putBoolean(PREF_KEY_SOUND, value).apply()
}
