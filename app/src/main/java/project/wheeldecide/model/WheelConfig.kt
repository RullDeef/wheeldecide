package project.wheeldecide.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
class WheelConfig(val optionsList: List<DecideOption> = listOf()) {

    companion object {
        private const val mColorSchemeSize = 20
        private val mColorScheme = IntRange(0, mColorSchemeSize - 1).map {
            Color.hsl(360f * it / mColorSchemeSize, 0.4f, 0.6f)
        }.shuffled()
    }

    val pies by lazy {
        optionsList.mapIndexed { index, it ->
            WheelPie(
                label = it.title,
                fraction = 1f / optionsList.size.toFloat(),
                color = mColorScheme[index],
            )
        }
    }

    fun addOption(optionName: String) = WheelConfig(optionsList + DecideOption(optionName))
    fun removeOption(index: Int) = WheelConfig(optionsList - optionsList[index])

    fun chooseAtAngle(pointAngle: Float): String {
        if (optionsList.isEmpty()) {
            throw Exception("options list empty!")
        }

        val deltaAngle = 360f / optionsList.size
        var angle = pointAngle - deltaAngle / 2
        var i = 0
        while (angle > 0) {
            angle -= deltaAngle
            i = (i + 1) % optionsList.size
        }

        return optionsList[i].title
    }
}
