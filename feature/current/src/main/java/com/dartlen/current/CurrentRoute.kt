package com.dartlen.current

import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.dartlen.designsystem.NiaTheme
import com.dartlen.designsystem.chart.chart
import com.dartlen.designsystem.chart.previewInformation
import com.dartlen.elevation.feature.current.R
import com.dartlen.mylibrary.LocationService
import com.dartlen.ui.DevicePreviews
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun currentRoute(viewModel: CurrentViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        MultiplePermissions(
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            MultiplePermissions(list = listOf(POST_NOTIFICATIONS))
        }

        Row() {
            previewInformation(
                modifier = Modifier.weight(0.33f),
                painterResource(R.drawable.gps),
                "Location",
                LocationService.currentLocation.collectAsState().value
            )

            previewInformation(
                modifier = Modifier.weight(0.33f),
                painterResource(R.drawable.location),
                "Elevation",
                String.format("%.2f", LocationService.elevation.collectAsState().value)
            )
        }

        Row {
            previewInformation(
                modifier = Modifier.weight(0.33f),
                painterResource(R.drawable.distance),
                "Distance",
                LocationService.distance.collectAsState().value.toString()
            )

            previewInformation(
                modifier = Modifier.weight(0.33f),
                painterResource(R.drawable.distance),
                "Compas",
                ""
            )

            previewInformation(
                modifier = Modifier.weight(0.33f),
                painterResource(R.drawable.distance),
                "Av. Speed",
                ""
            )
        }

        chart(mutableListOf())

    }
}


@DevicePreviews
@Composable
fun currentRoutePreview() {
    BoxWithConstraints {
        NiaTheme {
            currentRoute()
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun MultiplePermissions(list: List<String>) {
    val permissionStates = rememberMultiplePermissionsState(permissions = list)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionStates.launchMultiplePermissionRequest()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    permissionStates.permissions.forEach { per ->
        when {
            per.status.isGranted -> {
                /* Permission has been granted by the user.
                   You can use this permission to now acquire the location of the device.
                   You can perform some other tasks here.
                */
                //Text(text = "permission has been granted")
            }

            per.status.shouldShowRationale -> {
                /*Happens if a user denies the permission two times

                 */
                //Text(text = "permission is needed")
            }

            !per.status.isGranted && !per.status.shouldShowRationale -> {
                /* If the permission is denied and the should not show rationale
                    You can only allow the permission manually through app settings
                 */
                //Text(text = "Navigate to settings and enable the permission")

            }
        }
    }

}