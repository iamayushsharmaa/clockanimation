package com.example.clockanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clockanimation.ui.theme.ClockAnimationTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClockAnimationTheme {
                Surface(color = Color.Black) {
                    Clock()
                }
            }
        }
    }
}

@Composable
fun Clock() {

    val infiniteTransition = rememberInfiniteTransition()

    // Define colors for disco effect
    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta)

    // Animate through colors infinitely
    val animatedColorIndex by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = (colors.size-1).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val calendar = Calendar.getInstance()
    val hours = calendar.get(Calendar.HOUR)
    val minutes = calendar.get(Calendar.MINUTE)
    val hoursRotation = remember { Animatable((hours % 12) * 30f + minutes / 2f) }
    val minutesRotation = remember { Animatable(minutes * 6f) }

    LaunchedEffect(hours, minutes) {
        hoursRotation.animateTo((hours % 12) * 30f + minutes / 2f)
        minutesRotation.animateTo(minutes * 6f)
    }

    // Date and day formatting
    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
    val date = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Text(
            text = "$dayOfWeek, $date",
            color = Color(0xFFCF5D8F),
            style = androidx.compose.ui.text.TextStyle(fontSize = 24.sp),
            modifier = Modifier
                .padding(20.dp)
                .width(600.dp),
            textAlign = TextAlign.Center
        )

        Canvas(modifier = Modifier.fillMaxSize()
            .padding(50.dp))
        {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension / 2.2f

            // Custom colors
            val hourHandColor = Color(0xFFCF5D8F)
            val minuteHandColor = Color(0xFF303FC2)
            val circleColor = Color(0xFFCF5D8F)

            drawCircle(
                color = circleColor,
                center = androidx.compose.ui.geometry.Offset(centerX, centerY),
                radius = radius,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
            )

            val hoursEndX = centerX + radius * 0.5f * kotlin.math.cos(Math.toRadians(hoursRotation.value.toDouble() - 90)).toFloat()
            val hoursEndY = centerY + radius * 0.5f * kotlin.math.sin(Math.toRadians(hoursRotation.value.toDouble() - 90)).toFloat()

            drawLine(
                color = hourHandColor,
                start = androidx.compose.ui.geometry.Offset(centerX, centerY),
                end = androidx.compose.ui.geometry.Offset(hoursEndX, hoursEndY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )

            val minutesEndX =
                centerX + radius * 0.7f * kotlin.math.cos(Math.toRadians(minutesRotation.value.toDouble() - 90))
                    .toFloat()
            val minutesEndY =
                centerY + radius * 0.7f * kotlin.math.sin(Math.toRadians(minutesRotation.value.toDouble() - 90))
                    .toFloat()
            drawLine(
                color = minuteHandColor,
                start = androidx.compose.ui.geometry.Offset(centerX, centerY),
                end = androidx.compose.ui.geometry.Offset(minutesEndX, minutesEndY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }


    }
}

@Preview(showBackground = true)
@Composable
fun ClockPreview() {
    ClockAnimationTheme {
        Clock()
    }
}