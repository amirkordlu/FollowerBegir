package com.amk.sakoo.util

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.core.content.ContextCompat.startActivity
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import com.amk.sakoo.R
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
        "Not Start" -> "شروع نشده"
        else -> status
    }
}

data class StatusIconInfo(val iconRes: Int, val tint: Color)

fun getStatusIconInfo(status: String?): StatusIconInfo {
    return when (status) {
        "Completed" -> StatusIconInfo(R.drawable.ic_check, Color(0xFF4CAF50))
        "Pending", "processing" -> StatusIconInfo(R.drawable.ic_loading, Color(0xFFFF9800))
        "Canceled" -> StatusIconInfo(R.drawable.ic_cancel, Color(0xFFF44336))
        else -> StatusIconInfo(R.drawable.ic_info, Color.Gray)
    }
}

fun getRemain(remain: Any?): Any? {
    return when (remain) {
        null -> "نامشخص"
        else -> remain
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

val iconColorPairs = listOf(
    Pair(Color(0xFFE8EAF6), Color(0xFF3949AB)),
    Pair(Color(0xFFF3E5F5), Color(0xFF7E57C2)),
    Pair(Color(0xFFFFE6F0), Color(0xFFE91E63)),
    Pair(Color(0xFFE0F7FA), Color(0xFF0097A7)),
    Pair(Color(0xFFFFF4D6), Color(0xFFFFA726)),
    Pair(Color(0xFFECFFD8), Color(0xFF7CB342)),
    Pair(Color(0xFFE0E9FF), Color(0xFF3D5AFE)),
    Pair(Color(0xFFFFE0E0), Color(0xFFF44336)),
    Pair(Color(0xFFD5FAF0), Color(0xFF00C2A8)),
    Pair(Color(0xFFD6EEFF), Color(0xFF00ADEF)),
    Pair(Color(0xFFE8F5E9), Color(0xFF43A047)),
    Pair(Color(0xFFFFF3E0), Color(0xFFFB8C00)),
    Pair(Color(0xFFEFEBE9), Color(0xFF6D4C41)),
    Pair(Color(0xFFECEFF1), Color(0xFF546E7A)),
    Pair(Color(0xFFFBE9E7), Color(0xFFF4511E)),
    Pair(Color(0xFFEDE7F6), Color(0xFF5E35B1))
)

fun sendEmail(context: Context, email: String) {
    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
    startActivity(context, Intent.createChooser(emailIntent, "Send Email..."), null)
}