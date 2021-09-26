package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NightMode

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference
        val listPrefTheme =
                findPreference<ListPreference>(getString(R.string.pref_key_dark))
        listPrefTheme?.setOnPreferenceChangeListener { _, newValue ->
            when (newValue) {
                getString(R.string.pref_dark_auto) -> updateTheme(NightMode.AUTO.value)
                getString(R.string.pref_dark_on) -> updateTheme(NightMode.ON.value)
                getString(R.string.pref_dark_off) -> updateTheme(NightMode.OFF.value)
                else -> updateTheme(NightMode.OFF.value)
            }
        }

        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        val prefNotification =
                findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
        prefNotification?.setOnPreferenceChangeListener { _, newValue ->
            val dailyReminder = DailyReminder()
            when (newValue) {
                true -> dailyReminder.setDailyReminder(context as SettingsActivity)
                false -> dailyReminder.cancelAlarm(context as SettingsActivity)
            }
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}