package project.wheeldecide.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.wheeldecide.model.WheelPie
import project.wheeldecide.ui.theme.WheelDecideTheme

@OptIn(ExperimentalTextApi::class)
@Composable
fun Wheel(pies: List<WheelPie>, globalOffset: Float, side: Dp, modifier: Modifier = Modifier) {
    val px = with(LocalDensity.current) { 1.dp.toPx() }
    val arcOffset = Offset(px, px)
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier
            .size(width = side, height = side)
            .rotate(globalOffset)
    ) {
        drawCircle(Color.Black)
        drawCircle(Color.White, side.toPx() - 4 * px)

        val arcSize = Size(width = size.width - 2 * px, height = size.height - 2 * px)

        var angle = 0f // globalOffset
        for (pie in pies) {
            val sweep = 360f * pie.fraction
            angle += sweep / 2
            rotate(angle) {
                drawArc(pie.color, -sweep / 2, sweep, true, arcOffset, arcSize)
                translate(left = size.width / 2, top = size.height / 2) {
                    val measuredText = textMeasurer.measure(
                        AnnotatedString(pie.label),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        constraints = Constraints.fixedWidth((size.width / 2).toInt()),
                        style = TextStyle(fontSize = 12.sp, textAlign = TextAlign.Center),
                    )
                    drawText(
                        measuredText, topLeft = Offset(0f, -measuredText.size.height / 2f)
                    )
                }
            }

            angle += sweep / 2
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WheelPreview() {
    WheelDecideTheme {
        Box(Modifier.padding(all = 10.dp)) {
            Wheel(
                listOf(
                    WheelPie("pepeg", 0.5f, Color.Yellow),
                    WheelPie("omega", 0.1f, Color.Green),
                    WheelPie("hewwo", 0.2f, Color.Red),
                    WheelPie("woofa", 0.2f, Color.Blue),
                ),
                40f,
                300.dp,
            )
        }
    }
}
