package com.soulware.tcompro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import com.soulware.tcompro.core.AppNav
import com.soulware.tcompro.core.data.AppTheme
import com.soulware.tcompro.core.data.PreferencesManager
import com.soulware.tcompro.core.ui.theme.TcomproTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settings by preferencesManager.appSettingsFlow.collectAsState(initial = null)

            if (settings != null) {

                val useDarkTheme = when (settings!!.appTheme) {
                    AppTheme.LIGHT -> false
                    AppTheme.DARK -> true
                    else -> isSystemInDarkTheme()
                }

                val currentLocale = AppCompatDelegate.getApplicationLocales().toLanguageTags()
                val newLanguage = settings!!.language

                if (!currentLocale.startsWith(newLanguage)) {
                    val appLocale = LocaleListCompat.forLanguageTags(newLanguage)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }

                TcomproTheme(darkTheme = useDarkTheme) {
                    AppNav()
                }
            }
        }
    }
}