package com.amk.sakoo.ui.features.supportScreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amk.sakoo.R
import com.amk.sakoo.ui.theme.FollowerBegirTheme
import com.amk.sakoo.ui.theme.bodyLargeCard
import com.amk.sakoo.ui.theme.bodyMediumCard
import com.amk.sakoo.util.sendEmail

@Preview(showBackground = true)
@Composable
fun SupportScreenPreview() {
    FollowerBegirTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SupportScreen()
        }
    }
}

@Composable
fun SupportScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_support),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "پشتیبانی",
            style = bodyLargeCard,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "در صورت بروز هرگونه مشکل در فرآیند خرید، ثبت سفارش، یا استفاده از برنامه، می‌توانید از طریق راه‌های زیر با تیم پشتیبانی ما در ارتباط باشید.",
            style = bodyMediumCard,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))


        SupportButton(
            text = "پشتیبانی در تلگرام",
            icon = Icons.Outlined.Send,
            isPrimary = true,
            textColor = MaterialTheme.colorScheme.onPrimary
        ) {
            val telegramId = "amk_support"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/$telegramId"))
            context.startActivity(intent)
        }

        Spacer(modifier = Modifier.height(12.dp))


        SupportButton(
            text = "پشتیبانی از طریق ایمیل",
            icon = Icons.Outlined.Email,
            isPrimary = false,
            textColor = MaterialTheme.colorScheme.primary
        ) {
            sendEmail(context, "amir.kordlu@gmail.com")
        }
    }
}

@Composable
fun SupportButton(
    text: String,
    icon: ImageVector,
    textColor: Color,
    isPrimary: Boolean = true,
    onClick: () -> Unit
) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .height(56.dp)

    if (isPrimary) {
        Button(
            onClick = onClick,
            modifier = buttonModifier,
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            SupportButtonContent(text = text, icon = icon, textColor = textColor)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = buttonModifier,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            SupportButtonContent(text = text, icon = icon, textColor = textColor)
        }
    }
}

@Composable
private fun SupportButtonContent(text: String, icon: ImageVector, textColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = bodyMediumCard, color = textColor)
    }
}