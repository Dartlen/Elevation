package com.dartlen.map

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.rasterDemSource
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.extension.style.terrain.generated.terrain
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.Plugin
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.compass.CompassViewPlugin
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.scalebar.ScaleBarPlugin

@Composable
fun MapWrapper() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val mapboxMap = createRef()
        val fab = createRef()
        val mapView = rememberMapViewWithLifecycle()

        AndroidView(
            factory = { mapView },
            modifier = Modifier.constrainAs(mapboxMap) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        IconButton(
            modifier = Modifier
                .padding(top = 57.dp, start = 15.dp)
                .width(30.dp)
                .height(30.dp),
            enabled = true,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onClick = {

            }
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }


    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
//        Mapbox.getInstance(
//            context,
//            "sk.eyJ1IjoiZGFydGxlbiIsImEiOiJjbGZyMTVicXgwM3psNDVvMDIzcTlqbGQ2In0.G0e5jBgfosjSbzEgSTm_jA"//context.getString(R.string.mapbox_access_token)
//        )
        MapView(
            context, MapInitOptions(
                context,
                resourceOptions = ResourceOptions.Builder()
                    .accessToken("pk.eyJ1IjoiZGFydGxlbiIsImEiOiJjbGFmdGt2bmowbnZrM3BwcTMwZXdoZXdyIn0.q0DctWm2bZiqp5cDwUSIkQ")
                    .build(),
                styleUri = "mapbox://styles/dartlen/clfseodxe002j01lesi7zqd6i"
            )
        )
            .apply {

                val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
                    //getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
                }

                val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
                    getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
                    gestures.focalPoint = getMapboxMap().pixelForCoordinate(it)
                }

                val onMoveListener = object : OnMoveListener {
                    override fun onMoveBegin(detector: MoveGestureDetector) {
                        //onCameraTrackingDismissed()
                    }

                    override fun onMove(detector: MoveGestureDetector): Boolean {
                        return false
                    }

                    override fun onMoveEnd(detector: MoveGestureDetector) {}
                }

                initLocationComponent(
                    this,
                    context,
                    onIndicatorPositionChangedListener,
                    onIndicatorBearingChangedListener
                )
                setupGesturesListener(this, onMoveListener)


//            val mapView = this
//            getMapAsync { mapboxMap ->
//                mapboxMap.setStyle(Style.MAPBOX_STREETS)
//
//                val position = CameraPosition.Builder()
//                    .target(LatLng(70.04004014308637, -20.744085852141072))
//                    .zoom(15.0)
//                    .build()
//
//                //mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1)
//
//                mapboxMap.getStyle {
//                }
//            }
            }


    }

//    val lifecycle = LocalLifecycleOwner.current.lifecycle
//    DisposableEffect(lifecycle, mapView) {
//        // Make MapView follow the current lifecycle
//        val lifecycleObserver = getMapLifecycleObserver(mapView)
//        lifecycle.addObserver(lifecycleObserver)
//        onDispose {
//            lifecycle.removeObserver(lifecycleObserver)
//        }
//    }

    mapView.getMapboxMap().loadStyle(
        styleExtension = style("mapbox://styles/dartlen/clfseodxe002j01lesi7zqd6i") {
            +terrain("TERRAIN_SOURCE").exaggeration(5.5)
//            +skyLayer("sky") {
//                skyType(SkyType.ATMOSPHERE)
//                skyAtmosphereSun(listOf(-50.0, 90.2))
//            }
//            +atmosphere { }
            //+projection(ProjectionName.GLOBE)
        }
    ) {
        it.addSource(rasterDemSource("TERRAIN_SOURCE") {
            url("mapbox://mapbox.terrain-rgb")
            tileSize(512)
        })

//        val compassView = mapView.findViewWithTag<View>("compassView")
//        println("compass : ${compassView.toString()}")
//        compassView?.setOnApplyWindowInsetsListener { p0, p1 -> p1.apply {
//            println("padding ${p0.paddingBottom}, ${p0.paddingTop}, ${p0.paddingEnd}, ${p0.paddingStart}")
//            p0.updatePadding(
//                bottom = p0.paddingBottom
//            )
//            p1
//        } }
    }

    val compass = mapView.getPlugin<CompassViewPlugin>(Plugin.MAPBOX_COMPASS_PLUGIN_ID)
    compass?.marginTop = 150f
    compass?.marginRight = 50f
    println("compass : ${compass.toString()}")

    val scaleBar = mapView.getPlugin<ScaleBarPlugin>(Plugin.MAPBOX_SCALEBAR_PLUGIN_ID)
    scaleBar?.marginTop = 150f
    scaleBar?.marginLeft = 200f


    val initialCameraOptions = CameraOptions.Builder()
        .center(Point.fromLngLat(44.738941, 41.732831))
        .pitch(45.0)
        .zoom(15.5)
        .bearing(-17.6)
        .build()

    var animationOptions = MapAnimationOptions.Builder().duration(15000).build()

    mapView.getMapboxMap().flyTo(initialCameraOptions, animationOptions)


    // Create an instance of the Annotation API and get the polyline manager.
    val annotationApi = mapView.annotations
    val polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()
// Define a list of geographic coordinates to be connected.
    val points = listOf(
        Point.fromLngLat(44.738941, 41.732831),
        Point.fromLngLat(44.740292, 41.733055),
        Point.fromLngLat(44.742175, 41.733363)
    )
// Set options for the resulting line layer.
    val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
        .withPoints(points)
        // Style the line that will be added to the map.
        .withLineColor("#ee4e8b")
        .withLineWidth(5.0)
// Add the resulting line to the map.
    polylineAnnotationManager.create(polylineAnnotationOptions)



    return mapView
}

fun setupGesturesListener(
    mapView: MapView,
    onMoveListener: OnMoveListener
) {
    mapView.gestures.addOnMoveListener(onMoveListener)
}

private fun initLocationComponent(
    mapView: MapView,
    context: Context,
    onIndicatorPositionChangedListener: OnIndicatorPositionChangedListener,
    onIndicatorBearingChangedListener: OnIndicatorBearingChangedListener
) {
    val locationComponentPlugin = mapView.location
    locationComponentPlugin.updateSettings {
        this.enabled = true
        locationPuck = LocationPuck2D(
            topImage = context.getDrawable(com.mapbox.maps.R.drawable.mapbox_user_icon),
            bearingImage = context.getDrawable(com.mapbox.maps.R.drawable.mapbox_user_bearing_icon),
            shadowImage = context.getDrawable(com.mapbox.maps.R.drawable.mapbox_user_stroke_icon)
        )
//        this.locationPuck = LocationPuck2D(
//            bearingImage = getDrawable(context, com.dartlen.elevation.core.designsystem.R.drawable.ic_upcoming_border),
//            shadowImage = getDrawable(context, com.dartlen.elevation.core.designsystem.R.drawable.ic_upcoming_border),
//            scaleExpression = interpolate {
//                linear()
//                zoom()
//                stop {
//                    literal(0.0)
//                    literal(0.6)
//                }
//                stop {
//                    literal(20.0)
//                    literal(1.0)
//                }
//            }.toJson()
//        )
    }
    locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
    locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
}


private const val SOURCE = "TERRAIN_SOURCE"
private const val SKY_LAYER = "sky"
private const val TERRAIN_URL_TILE_RESOURCE = "mapbox://mapbox.mapbox-terrain-dem-v1"


/**
 * Handles lifecycle of provided mapView
 */
private fun getMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            //Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
            Lifecycle.Event.ON_START -> mapView.onStart()
            //Lifecycle.Event.ON_RESUME -> mapView.onResume()
            //Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            //TODO: onLowMemory
            Lifecycle.Event.ON_STOP -> mapView.onStop()
            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
            else -> throw IllegalStateException()
        }
    }