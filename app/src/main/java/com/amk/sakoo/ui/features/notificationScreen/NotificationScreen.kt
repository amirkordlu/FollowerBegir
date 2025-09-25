package com.amk.sakoo.ui.features.notificationScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amk.sakoo.R
import com.amk.sakoo.ui.theme.FollowerBegirTheme
import com.amk.sakoo.ui.theme.bodyMediumCard
import dev.burnoo.cokoin.navigation.getNavController

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    FollowerBegirTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NotificationScreen()
        }
    }
}

@Composable
fun NotificationScreen() {
    val navigation = getNavController()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navigation.navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "پیام‌ها",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.dana_medium))
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "پیامی یافت نشد.",
                style = bodyMediumCard,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}