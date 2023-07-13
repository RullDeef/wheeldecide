package project.wheeldecide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import project.wheeldecide.ui.theme.WheelDecideTheme
import kotlin.math.pow

@Composable
fun DecisionView(decision: String, modifier: Modifier = Modifier) {
    val gradientLen = 30
    val gradient = IntRange(0, gradientLen).map {
        val point = it.toFloat() / gradientLen
        val alpha = ((1 - point) * point * 4f).pow(5)
        Pair(point, Color(0f, 0f, 0f, alpha))
    }.toTypedArray()

    Box(modifier.background(Brush.verticalGradient(*gradient))) {
        Text(
            decision,
            Modifier
                .fillMaxSize()
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDecisionView() {
    WheelDecideTheme {
        DecisionView(
            decision = "Hello!",
            Modifier
                .width(200.dp)
                .height(200.dp)
        )
    }
}