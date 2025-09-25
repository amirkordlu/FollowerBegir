package com.amk.sakoo.ui.features.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amk.sakoo.R
import com.amk.sakoo.ui.theme.FollowerBegirTheme
import com.amk.sakoo.util.MyScreens
import com.amk.sakoo.util.SocialPlatform
import com.amk.sakoo.util.allPlatforms
import dev.burnoo.cokoin.navigation.getNavController

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FollowerBegirTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen()
        }
    }
}

@Composable
fun HomeScreen() {
    val navigation = getNavController()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        HeaderSection(onClick = { navigation.navigate(MyScreens.NotificationScreen.route) })
        PromoBanner(onClick = { navigation.navigate(MyScreens.ShopScreen.route) })
        PlatformGrid()
    }
}

@Composable
fun HeaderSection(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onClick.invoke() }) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications"
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "سکو",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.dana_medium))
            )
            Text(
                text = "سکوی پرتاب شما در دنیای مجازی",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = FontFamily(Font(R.font.dana_medium))
            )
        }
    }
}

@Composable
fun PromoBanner(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        Box(contentAlignment = Alignment.CenterEnd) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(90.dp)
                    .offset(x = (-40).dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(60.dp)
                    .offset(x = (20).dp, y = (20).dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
            )

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "!تخفیف ویژه",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = FontFamily(Font(R.font.dana_medium))
                )
                Text(
                    text = "با شارژ بالای 1 میلیون تومان، ۵٪ اعتبار بیشتر هدیه بگیرید!",
                    style = MaterialTheme.typography.bodyMedium.copy(textDirection = TextDirection.Rtl),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Right,
                    fontFamily = FontFamily(Font(R.font.dana_medium))
                )
                Button(
                    onClick = { onClick.invoke() },
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "افزایش موجودی",
                        fontFamily = FontFamily(Font(R.font.dana_medium))
                    )
                }
            }
        }
    }
}

@Composable
fun PlatformGrid() {
    val navigation = getNavController()
    val chunkedPlatforms = allPlatforms.chunked(4)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        chunkedPlatforms.forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowItems.forEach { platform ->
                    Box(modifier = Modifier.weight(1f)) {
                        PlatformItem(platform = platform) {
                            navigation.navigate(
                                MyScreens.PlatformServicesScreen.createRoute(
                                    platform.id
                                )
                            )
                        }
                    }
                }
                repeat(4 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun PlatformItem(platform: SocialPlatform, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = platform.iconRes),
                    contentDescription = platform.displayName,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Text(
            text = platform.displayName,
            style = MaterialTheme.typography.labelLarge.copy(textDirection = TextDirection.Rtl),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
            fontFamily = FontFamily(Font(R.font.dana_medium))
        )
    }
}