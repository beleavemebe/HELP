package company.vk.education.siriusapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object AppTypography {
    val title1Medium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 24.sp, color = TextPrimary)
    val title1Bold = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp, color = TextPrimary)

    val title2 = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp, color = TextPrimary)
    val title2Medium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp, color = TextPrimary)
    val title2Bold = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)

    val headlineMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp, color = TextPrimary)
    val headline = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, color = TextPrimary)

    val textMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp, color = TextPrimary)
    val text = TextStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp, color = TextPrimary)

    val subheadBold = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
    val subheadMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp, color = TextPrimary)
    val subhead = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, color = TextPrimary)

    val caption1Medium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 13.sp, color = TextPrimary)
    val caption1 = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp, color = TextPrimary)

    val caption2Medium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp, color = TextPrimary)
    val caption2 = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, color = TextPrimary)

    val caption3 = TextStyle(fontWeight = FontWeight.Normal, fontSize = 11.sp, color = TextPrimary)
    val caption4 = TextStyle(fontWeight = FontWeight.Normal, fontSize = 9.sp, color = TextPrimary)

    val Typography = Typography(
        h1 = title1Bold,
        h2 = title1Medium,
        h3 = title2Bold,
        h4 = title2Medium,
        h5 = title2,
        h6 = headlineMedium,
        body1 = textMedium,
        body2 = text,
        subtitle1 = subheadBold,
        subtitle2 = subheadMedium,
        caption = caption1Medium,
        overline = caption2Medium,
        button = subheadMedium,
    )
}