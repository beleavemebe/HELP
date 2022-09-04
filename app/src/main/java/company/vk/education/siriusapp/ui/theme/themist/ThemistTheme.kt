package company.vk.education.siriusapp.ui.theme.themist

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

interface ThemistTheme {
    val currentPalette: List<Color>
    val currentSpacing: List<Dp>
    val currentColors: List<Color>
    val currentTypography: List<TextStyle>
    val currentShapes: List<Dp>

//    @Composable
//    fun wrapContent(content: @Composable () -> Unit)
}

object MaterialThemistTheme : ThemistTheme {
    val Blue200 = Color(0xFF7ca1d2)
    val Blue500 = Color(0xFF5586c6)
    val Blue700 = Color(0xFF4f89d9)
    val Blue900 = Color(0xFF2D81E0)
    val Teal200 = Color(0xFF03DAC5)

    override val currentPalette: List<Color>
        get() = listOf(Blue200, Blue500, Blue700, Blue900, Teal200)

    override val currentSpacing: List<Dp>
        get() = listOf()

    override val currentColors: List<Color>
        get() = TODO("Not yet implemented")

    override val currentTypography: List<TextStyle>
        get() = TODO("Not yet implemented")

    override val currentShapes: List<Dp>
        get() = TODO("Not yet implemented")

//    @Composable
//    override fun wrapContent(content: () -> Unit) = MaterialTheme() {
//        TODO("Not yet implemented")
//    }
}
