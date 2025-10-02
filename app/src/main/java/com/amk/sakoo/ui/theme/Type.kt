package com.amk.sakoo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.amk.sakoo.R

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val navigationBarTextStyle = TextStyle(
    fontSize = 12.sp,
    fontFamily = FontFamily(Font(R.font.vazirmatn_medium)),
    fontWeight = FontWeight(500),
    color = Color(0xFF000000),
    textAlign = TextAlign.Center,
)

val textFieldStyle = TextStyle(
    fontSize = 16.sp,
    fontFamily = FontFamily(Font(R.font.vazirmatn_medium)),
    fontWeight = FontWeight(400),
    color = Color(0xFF424242),
    textAlign = TextAlign.Right,
)

val bodyLargeCard = TextStyle(
    fontSize = 22.sp,
    fontFamily = FontFamily(Font(R.font.vazirmatn_medium)),
    fontWeight = FontWeight(600),
    color = Color(0xFF000000),
    textAlign = TextAlign.Center,
)

val bodyMediumCard = TextStyle(
    fontSize = 16.sp,
    fontFamily = FontFamily(Font(R.font.vazirmatn_medium)),
    fontWeight = FontWeight(500),
    color = Color(0xFF000000),
    textAlign = TextAlign.Center,
    textDirection = TextDirection.Rtl
)

val bodySmallCard = TextStyle(
    fontSize = 14.sp,
    fontFamily = FontFamily(Font(R.font.vazirmatn_medium)),
    fontWeight = FontWeight(500),
    color = Color(0xFF000000),
    textAlign = TextAlign.Center,
)