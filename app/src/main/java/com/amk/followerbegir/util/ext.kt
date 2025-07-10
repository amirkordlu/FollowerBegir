package com.amk.followerbegir.util

import android.text.Spanned
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import com.amk.followerbegir.R
import kotlinx.coroutines.CoroutineExceptionHandler

val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Log.e("Error", "error -> ${throwable.message}")
}

fun convertHTMLToText(htmlText: String): String {
    return htmlText.parseAsHtml().toString()
}

fun appendTextDialog(text: String): AnnotatedString {
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontFamily = FontFamily(Font(R.font.dana_medium)),
                fontSize = 14.sp,
                color = Color.Black
            )
        ) {
            append(text)
        }
    }
    return annotatedString
}

fun parseHtmlToSpanned(html: String): Spanned {
    return HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun getPersianStatus(status: String?): String {
    return when (status) {
        null -> "نامشخص"
        "Pending" -> "در انتظار"
        "Canceled" -> "لغو شده"
        "processing" -> "در حال پردازش"
        "Completed" -> "تکمیل شده"
        else -> status
    }
}

fun String.toPersianDigits(): String {
    val englishDigits = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    val persianDigits = listOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')

    var result = this
    englishDigits.forEachIndexed { index, engDigit ->
        result = result.replace(engDigit, persianDigits[index])
    }
    return result
}

fun Int.toPersianDigits(): String = this.toString().toPersianDigits()

fun Int.formatBalanceWithCommas(): String {
    return String.format("%,d", this)
}