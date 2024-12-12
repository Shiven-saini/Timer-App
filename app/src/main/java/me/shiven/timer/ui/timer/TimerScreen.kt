package me.shiven.timer.ui.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import me.shiven.timer.OrientationListener
import me.shiven.timer.getSystemAnimationScale
import me.shiven.timer.timeFormatHelper

@Composable
fun TimerScreen(isFirstLaunch: Boolean, viewModel: TimerViewModel, modifier: Modifier = Modifier) {
    val toShowDialog = remember { mutableStateOf(false) }
    val toShowLaunchDialog = remember { mutableStateOf(isFirstLaunch) }
    val progress = remember { Animatable(0f) }
    val uiState = viewModel.uiState.collectAsState()
    val currentTime = remember { mutableStateOf(0) }
    val animationScale = remember { mutableStateOf(1f) }
    val context = LocalContext.current
    var isInverted by remember { mutableStateOf(false) }
    val justLaunched = remember { mutableStateOf(true) }

    val orientationListener = remember {
        OrientationListener(context) { orientation ->
            isInverted= orientation
        }
    }

    DisposableEffect(Unit) {
        orientationListener.startListening()
        onDispose {
            orientationListener.stopListening()
        }
    }

    LaunchedEffect(key1 = isInverted) {
        if(!justLaunched.value){
            viewModel.resetAndStartTimer()
        } else {
            justLaunched.value = false
        }
    }

    LaunchedEffect(Unit) {
        animationScale.value = getSystemAnimationScale(context)
    }

    LaunchedEffect(key1 = uiState.value.isTimerEnabled) {
        if(uiState.value.isTimerEnabled){
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = (uiState.value.timerDuration * 1000 / animationScale.value).toInt(), easing = LinearEasing)
            )
        } else {
            progress.snapTo(targetValue = 0f)
        }
    }

    LaunchedEffect(key1 = uiState.value.timerDuration, key2 = uiState.value.isTimerEnabled) {
        currentTime.value = uiState.value.timerDuration
        if(uiState.value.isTimerEnabled) {
            while (currentTime.value > 0) {
                delay(1000)
                currentTime.value --
            }
        }
    }

    AnimatedVisibility(
        visible = toShowLaunchDialog.value,
        enter = fadeIn(),
        exit = fadeOut()
    ){
        LaunchDialog{
            toShowLaunchDialog.value = false
        }
    }

    AnimatedVisibility(
        visible = toShowDialog.value,
        enter = fadeIn(),
        exit = fadeOut()
    ){
        DurationPickerDialog(
            isInverted = isInverted,
            onDismiss = { toShowDialog.value = false },
            onConfirm = { minutes, seconds ->
                viewModel.setTimer(minutes, seconds)
                toShowDialog.value = false
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .graphicsLayer {
                rotationZ = if (isInverted) 180f else 0f
            }
            .clickable {
                toShowDialog.value = true
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(progress.value)
                .align(Alignment.BottomCenter)
                .background(Color.Gray)
        )

        Text(
            text = timeFormatHelper(currentTime.value),
            color = Color(0xFFFFD700),
            fontSize = 100.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}