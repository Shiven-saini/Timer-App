package me.shiven.timer.ui.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shiven.timer.ui.theme.spaceGrotesk

@Composable
fun DurationPickerDialog(
    isInverted: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var input by remember { mutableStateOf("") }

    AlertDialog(
        tonalElevation = 10.dp,
        containerColor = Color(0xFF000000),
        modifier = Modifier.graphicsLayer {
            rotationZ = if (isInverted) 180f else 0f
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = {
                val (minutes, seconds) = parseInputToMinutesAndSeconds(input)
                onConfirm(minutes, seconds)
            }) {
                Text("Set time", color = Color(0xFFFFD700))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel", color = Color(0xFFFFD700))
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                val (minutes, seconds) = formatInput(input)
                Text(
                    text = "${minutes}m ${seconds}s",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color(0xFFFFD700)
                )
                NumericKeypad(input) { newInput ->
                    if (newInput.length <= 4) input = newInput
                }
            }
        }
    )
}

private fun parseInputToMinutesAndSeconds(input: String): Pair<Int, Int> {
    val paddedInput = input.padStart(4, '0')
    val minutes = paddedInput.take(paddedInput.length - 2).toIntOrNull() ?: 0
    val seconds = paddedInput.takeLast(2).toIntOrNull() ?: 0
    return Pair(minutes, seconds)
}

private fun formatInput(input: String): Pair<String, String> {
    val paddedInput = input.padStart(4, '0')
    val minutesPart = paddedInput.take(paddedInput.length - 2).padStart(2, '0')
    val secondsPart = paddedInput.takeLast(2).padStart(2, '0')
    return Pair(minutesPart, secondsPart)
}

@Composable
fun NumericKeypad(input: String, onInputChange: (String) -> Unit) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("0", "←")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        keys.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                row.forEach { key ->
                    KeyButton(key) {
                        if (key == "←") {
                            onInputChange(input.dropLast(1))
                        } else {
                            onInputChange(input + key)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KeyButton(key: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(60.dp)
            .background(Color.Gray, CircleShape)
            .padding(8.dp)
    ) {
        TextButton(onClick = { onClick() }) {
            Text(key, fontSize = 18.sp, fontFamily = spaceGrotesk, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}