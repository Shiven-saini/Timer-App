package me.shiven.timer.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TimerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun setTimer(durationInMinutes: Int, durationInSeconds: Int) = viewModelScope.launch {
        _uiState.update { ui ->
            ui.copy(timerDuration = durationInMinutes*60 + durationInSeconds, isTimerEnabled = false)
        }
    }

    fun resetAndStartTimer() {
        _uiState.update { ui ->
            ui.copy(isTimerEnabled = false)
        }

        viewModelScope.launch {
            delay(500)
            _uiState.update { ui ->
                ui.copy(isTimerEnabled = true)
            }
        }
    }
}



data class UiState(
    val timerDuration: Int = 10,
    val isTimerEnabled: Boolean = false
)