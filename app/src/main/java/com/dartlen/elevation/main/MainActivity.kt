@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.dartlen.elevation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dartlen.designsystem.NiaTheme
import com.dartlen.elevation.commands.LoginEventHandler
import com.dartlen.elevation.ui.NiaApp
import com.dartlen.model.DarkThemeConfig
import com.dartlen.model.ThemeBrand
import com.dartlen.model.UserData
import com.dartlen.mylibrary.LocationService
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity(){

    val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var commandHandler: LoginEventHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        if(LocationService.isRecording.value == false) {
            lifecycleScope.launch {
                commandHandler.postLoginEvent("ACTION_START_OR_RESUME_SERVICE")
            }
        }

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect()
            }
        }

        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainActivityUiState.Loading -> true
                is MainActivityUiState.Success -> false
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = shouldUseDarkTheme(uiState)

            // Update the dark content of the system bars to match the theme
            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose {}
            }




            //SideEffect { // for map
                systemUiController.setStatusBarColor(color = Color.Transparent)
            //}



            CompositionLocalProvider() {
                NiaTheme(
                    darkTheme = darkTheme,
                    androidTheme = shouldUseAndroidTheme(uiState),
                    disableDynamicTheming = shouldDisableDynamicTheming(uiState),
                ) {
                    NiaApp(
                        //networkMonitor = networkMonitor,
                        windowSizeClass = calculateWindowSizeClass(this),
                        onStartRecord = {
                            lifecycleScope.launch {
                                if(viewModel.stateRecord.value){
                                    commandHandler.postLoginEvent("ACTION_STOP_SERVICE")
                                }else{
                                    commandHandler.postLoginEvent("ACTION_START_RECORD")
                                }
                                viewModel.stateRecord.value = !viewModel.stateRecord.value
                            }
                        },
                        stateRecord = viewModel.getRecordState().collectAsState()
                    )
                }
            }
        }
    }
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}

@Composable
private fun shouldUseAndroidTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> when (uiState.userData.themeBrand) {
        ThemeBrand.DEFAULT -> false
        ThemeBrand.ANDROID -> true
    }
}

/**
 * Returns `true` if the dynamic color is disabled, as a function of the [uiState].
 */
@Composable
private fun shouldDisableDynamicTheming(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> !uiState.userData.useDynamicColor
}

/**
 * Returns `true` if dark theme should be used, as a function of the [uiState] and the
 * current system context.
 */
@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (uiState.userData.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}



