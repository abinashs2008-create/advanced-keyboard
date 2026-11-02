package org.advancedkeyboard.app.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
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
            preferenceScreen = screen
        }
    }
}
