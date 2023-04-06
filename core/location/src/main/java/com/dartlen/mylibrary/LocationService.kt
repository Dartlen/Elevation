package com.dartlen.mylibrary

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dartlen.data.LocationRepository
import com.dartlen.model.LatLng
import com.dartlen.model.Polyline
import com.dartlen.model.Polylines
import com.dartlen.model.Road
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@AndroidEntryPoint
class LocationService @Inject constructor() : LifecycleService() {
    // This variable will help us know when to start and when to pause / resume our service
    var isFirstJourney = true

    // Timer variables

    // Timer enabled or not
    private var isTimerEnabled = false

    // When our setTimer() function is called, we will store the current time in this variable
    private var timeStarted = 0L // Time when our service was started

    // This is the time of one single lap that happens when setTimer() is called and paused
    private var lapTime = 0L

    // This is the total time our journey has been running
    private var timeRun = 0L

    // This variable will tell whether our service was killed or not
    private var serviceKilled = false

    @Inject   // This will provide us the current location of user
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject // This is the base notification that will contain title, time and icon
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    // This is the current notification that will contain the action text and action to be performed (pause or resume)
    lateinit var curNotificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var locationRepository: LocationRepository

    companion object {
        val isTracking = MutableLiveData<Boolean>() // Whether we want to track our user or not
        val isRecording = MutableLiveData<Boolean>(false)
        val pathPoints = MutableLiveData<Polylines>() // This is the list of paths or lines where user has travelled
        val timeRunInSeconds = MutableLiveData<Long>() // Total time elapsed since our service was started or resumed

        val currentLocation = MutableStateFlow("")
        val elevation = MutableStateFlow(0.0)
        val distance = MutableStateFlow(0.0f)
        val maxElevation = MutableStateFlow(0.0)
        val minElevation = MutableStateFlow(0.0)
        val startTime = MutableStateFlow(Date())
        val endTime = MutableStateFlow(Date())
    }

    // This function is called whenever our service is created
    override fun onCreate() {
        super.onCreate()

        postInitialValues() // Function to post empty values to our live data. (We created this function at bottom).

        // Initially we set curNotificationBuilder to baseNotificationBuilder to avoid lateinit not initialized exception
        curNotificationBuilder = baseNotificationBuilder

        isTracking.observe(this, Observer {
            // Function to get location of user when tracking is set to true and save it to pathPoints variable. (We created this function at bottom).
            updateLocationTracking(it)

            // Function to update the notification whenever we are tracking. (We created this function at bottom).
            updateNotificationTrackingState(it)
        })


        // Whenever a new polyline is added in our service, we receive it in activity, add it to our pathPoints list, update it on our map and move the camera towards that polyline
        pathPoints.observe(this, Observer {
            //pathPoints = it
            //addLatestPolyline()
            //moveCameraToUser()
            val distTrack = it
            val dist = TrackingUtility.calculateLengthofPolylines(distTrack)
            distance.value = dist//"${((dist / 1000f) * 10).roundToInt() /10} km"
            //binding.tvDistance.text = "${Math.round((distance / 1000f) * 10) /10} km" // Show distance in km


        })
    }

    // This function is called whenever a command is received
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_START_RECORD -> {
                    println("start service")

                    isRecording.postValue(true)

                    // Function to start the stopwatch. (We created this function).
                    startTimer()
                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    timeRunInSeconds.observe(this, Observer {
                        if(!serviceKilled) {
                            val notification = curNotificationBuilder.setContentText(TrackingUtility.getFormattedStopwatchTime(it))
                            notificationManager.notify(NOTIFICATION_ID,notification.build())
                        }
                    })
                    isFirstJourney = false



//                    startForegroundService()
                }

                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstJourney) {
                        // Function to start this service. (We created this function at bottom).
                        // As our service is starting, we set the isTracking value to true and also start the stopwatch timer
                        startForegroundService()


                        //Toast.makeText(this,"Service Started",Toast.LENGTH_SHORT).show()
                    } else {
                        // When we resume our service, we only want to continue the timer instead of restarting entire service.
                        startTimer()

                        //Toast.makeText(this,"Service Resumed",Toast.LENGTH_SHORT).show()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    pauseService() // Function to pause our service. (We created this function at bottom).
                }
                ACTION_STOP_SERVICE -> {
                    killService() // Function to stop or end our service. (We created this function at bottom).
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    // Function to kill our service
    private fun killService() {
        pathPoints //path
        timeRunInSeconds //time
        CoroutineScope(Dispatchers.IO).launch {
            locationRepository.insertRoad(
                Road(
                    0,
                    timeRunInSeconds.value!!,
                    maxElevation.value,
                    minElevation.value,
                    elevation.value,
                    distance.value,
                    0.0f,
                    startTime.value,
                    endTime.value,
                    pathPoints.value!!
                )
            )

            isFirstJourney = true
            pauseService()
            postInitialValues()
            timeRun = 0
            lapTime = 0
            endTime.value = Date()
        }

        //serviceKilled = true

        //stopForeground(true)
        //stopSelf()
    }

    // Function to pause our service
    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    // Function to create notification channel to provide metadata to notification
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }

    // Function to start our foreground service
    private fun startForegroundService() {

        // Get notification manager service and create notification channel to store notification metadata if android version >= Oreo
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager) // Notification channel created using the function we created above
        }

        // As our service is starting, we set the isTracking value to true and also start the stopwatch timer
        isTracking.postValue(true)

        // Function to start the stopwatch. (We created this function).
        //startTimer()

        // Start our service as a foreground service
        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

        // As time changes and our service is running, we update our notification's content
//        timeRunInSeconds.observe(this, Observer {
//            if(!serviceKilled) {
//                val notification = curNotificationBuilder.setContentText(TrackingUtility.getFormattedStopwatchTime(it))
//                notificationManager.notify(NOTIFICATION_ID,notification.build())
//            }
//        })

    }

    // Function to update our notification
    private fun updateNotificationTrackingState(isTracking: Boolean) {

        // Set notification action text
        val notificationActionText = if(isTracking) "Pause" else "Resume"

        // Set intent with action according to isTracking variable
        val pendingIntent = if(isTracking) {
            val pauseIntent = Intent(this, LocationService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this,1,pauseIntent, FLAG_IMMUTABLE)
        } else {
            val resumeIntent = Intent(this, LocationService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this,2,resumeIntent, FLAG_IMMUTABLE)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // This piece of code helps in clearing the previous actions
        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotificationBuilder,ArrayList<NotificationCompat.Action>())
        }

        // When our service is running, we notify the notification with the required data
        if(!serviceKilled) {
            curNotificationBuilder = baseNotificationBuilder.addAction(androidx.core.R.drawable.notification_action_background,notificationActionText,pendingIntent)
            notificationManager.notify(NOTIFICATION_ID,curNotificationBuilder.build())
        }

    }


    // Function to initialize / post empty valus to our live data members
    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        maxElevation.value = 0.0
        minElevation.value = 0.0
        elevation.value = 0.0
        currentLocation.value = "--"
    }

    // Function to add an empty polyline to our data members when there is a pause and resume distance gap between two locations
    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    // Function to add the location points of user
    private fun addPathPoint(location: Location?) {
        location?.let {
            // Get latitudes and longitudes of user's current location
            val pos = LatLng(location.latitude, location.longitude, location.altitude)
            pathPoints.value?.apply {
                // Add the position to end of our pathPoints variable
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    // This callback will be called whenever location of user changes
    private val locationCallback = object : LocationCallback() {

        // When a new location is received
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            println("current: ${result.locations.first().latitude} ${result.locations.first().latitude}")

            result.locations.let { locations ->
                setCurrentLocation(locations.last())
                if (isTracking.value!! && isRecording.value!!) {

                    for (location in locations) {
                        // The resulted location will be added to our pathPoints variable
                        addPathPoint(location)

                        //Timber.d("Current User Location: ${location.latitude} ${location.longitude}")
                        /*Toast.makeText(
                            this@TrackingService,
                            "Current User Location: ${location.latitude} ${location.longitude}",
                            Toast.LENGTH_SHORT
                        ).show()*/
                    }
                }
            }
        }

    }

    private fun setCurrentLocation(location: Location) {
        if (isTracking.value!! && isRecording.value!!) {
            currentLocation.value = "${String.format("%.2f", location.latitude)}, ${
                String.format(
                    "%.2f",
                    location.longitude
                )
            }"
            elevation.value = location.altitude//String.format("%.2f", location.altitude)
            if (maxElevation.value < location.altitude) maxElevation.value = location.altitude
            if (minElevation.value > location.altitude) minElevation.value = location.altitude
            else if (minElevation.value == 0.0) minElevation.value = location.altitude
        }
    }

    // The Actual Function to get the current location of user
    @SuppressLint("MissingPermission") // Since we used easy permissions library, we can use @SupressLint to hide this warning
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) { // When tracking is set to true
            if (TrackingUtility.hasLocationPermissions(this)) { // This function checks for location permissions. (We created this function in TrackingUtility.kt).
                val request = LocationRequest().apply { // Location request instance created
                    interval = LOCATION_UPDATE_INTERVAL // Variable defined in Constants.kt
                    fastestInterval = FASTEST_LOCATION_INTERVAL // Variable defined in Constants.kt
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                ) // Get the current location of user using the fused location provider client
            } else {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    // Function to start the stopwatch / timer
    private fun startTimer() {
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        startTime.value = Date()
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                lapTime = System.currentTimeMillis() - timeStarted
                timeRunInSeconds.postValue((timeRun + lapTime)/1000)
                delay(1000)
            }
            // Add the lap time to total time
            timeRun += lapTime
        }
    }


    val ACTION_START_RECORD = "ACTION_START_RECORD"
    val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    val ACTION_SHOW_TRACKING_ACTIVITY = "ACTION_SHOW_TRACKING_ACTIVITY"

    val NOTIFICATION_ID = 1
    val NOTIFICATION_CHANNEL_ID = "1"
    val NOTIFICATION_CHANNEL_NAME = "BikeRush"

    val LOCATION_UPDATE_INTERVAL = 5000L
    val FASTEST_LOCATION_INTERVAL = 2000L

}

object TrackingUtility {

    // Function to check if user has provided location permissions or not
    fun hasLocationPermissions(context: Context) = true
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//            EasyPermissions.hasPermissions(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//        } else {
//            EasyPermissions.hasPermissions(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION
//            )
//        }

    // Functions to convert total seconds to hh:mm:ss format
    fun getFormattedStopwatchTime(sec: Long): String {
        // 7200 seconds
        // 120 minutes
        // 2 hours

        val hours = sec /3600
        val minutes = sec /60%60
        val seconds = sec %60

        return "${if(hours < 10) "0" else ""}$hours:" +
                "${if(minutes < 10) "0" else ""}$minutes:" +
                "${if(seconds < 10) "0" else ""}$seconds"

    }

    // Function to get length of one polyline
    fun calculatePolylineLength(polyline: Polyline): Float {
        var distance = 0f
        for(i in 0..polyline.size-2) {
            val result = FloatArray(1)
            val pos1 = polyline[i]
            val pos2 = polyline[i+1]
            Location.distanceBetween(pos1.latitude,pos1.longitude,pos2.latitude,pos2.longitude,result)
            distance += result[0]
        }
        return distance
    }

    // Function to calculate sum of length of multiple polylines
    fun calculateLengthofPolylines(polylines: Polylines): Float {
        var totalDistance = 0f
        for(i in 0..polylines.size-1) {
            totalDistance += calculatePolylineLength(polylines[i])
        }
        return totalDistance
    }



}

