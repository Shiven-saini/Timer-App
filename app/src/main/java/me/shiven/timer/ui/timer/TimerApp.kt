package me.shiven.timer.ui.timer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TimerApp(isFirstLaunch: Boolean, modifier: Modifier = Modifier) {

    val viewModel: TimerViewModel = TimerViewModel()
    TimerScreen(isFirstLaunch, viewModel)

}