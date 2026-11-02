package org.advancedkeyboard.app.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import org.advancedkeyboard.app.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment()).commit()
        }
        supportActionBar?.title = getString(R.string.settings_title)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val context = preferenceManager.context
            val screen = preferenceManager.createPreferenceScreen(context)

            val enableCat = androidx.preference.PreferenceCategory(context).apply { title = "Activation" }
            screen.addPreference(enableCat)
            enableCat.addPreference(Preference(context).apply {
                title = "Enable Advanced Keyboard"
                summary = "Tap to open system Input Method settings and activate"
                setOnPreferenceClickListener {
                    startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
                    true
                }
            })

            val layoutCat = androidx.preference.PreferenceCategory(context).apply { title = "Appearance & Layout" }
            screen.addPreference(layoutCat)

            val fontSizePref = ListPreference(context).apply {
                key = KeyboardPreferences.PREF_KEY_FONT_SIZE_SCALE
                title = "Key Font Size"
                summary = "%s"
                entries = arrayOf("Small (70%)", "Compact (85% - Default)", "Medium (100%)", "Large (115%)")
                entryValues = arrayOf("0.70", "0.85", "1.00", "1.15")
                setDefaultValue("0.85")
            }
            layoutCat.addPreference(fontSizePref)

            val numberRowPref = SwitchPreferenceCompat(context).apply {
                key = KeyboardPreferences.PREF_KEY_NUMBER_ROW
                title = "Show Dedicated Number Row"
                summary = "Display digits 0–9 above alpha keys"
                setDefaultValue(true)
            }
            layoutCat.addPreference(numberRowPref)

            val keyBordersPref = SwitchPreferenceCompat(context).apply {
                key = KeyboardPreferences.PREF_KEY_KEY_BORDERS
                title = "Key Borders"
                summary = "Draw visible outlines around individual keys"
                setDefaultValue(true)
            }
            layoutCat.addPreference(keyBordersPref)

            preferenceScreen = screen
        }
    }
}
