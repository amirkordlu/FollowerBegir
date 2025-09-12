package com.amk.rubikafollower.ui.features.aboutScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amk.rubikafollower.R
import com.amk.rubikafollower.ui.theme.FollowerBegirTheme
import com.amk.rubikafollower.ui.theme.bodyMediumCard
import com.amk.rubikafollower.ui.theme.bodySmallCard
import com.amk.rubikafollower.util.sendEmail

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    FollowerBegirTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            AboutScreen()
        }
    }
}

@Composable
fun AboutScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AboutAppCard()
        AboutTextCard()
        AboutButton("گزارش باگ") {
            sendEmail(context, "amir.kordlu@gmail.com")
        }
        AboutDeveloperCard(
            "Designed by Stitch AI powered by Google" +
                    "\n<Developed by AMK/>"
        ) {}
    }
}

@Composable
fun AboutAppCard() {
    Surface(
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth(0.96f)
            .height(210.dp)
            .padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircleIconCart(
                icon = painterResource(id = R.drawable.img_app_icon)
            )

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Text(
                    modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
                    text = "فالوئربگیر روبیکا",
                    style = bodyMediumCard,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "نسخه 1.0.0",
                    style = bodySmallCard,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AboutTextCard() {
    Surface(
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth(0.96f)
            .height(250.dp)
            .padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Text(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    ),
                    text = "از اینکه «فالوئربگیر روبیکا» رو برای رشد حساب کاربری خودتون انتخاب کردید، بسیار خوشحالیم. هدف ما اینه که کمکت کنیم تو روبیکا بیشتر دیده بشی و تعداد فالوئرهات رو خیلی سریع و راحت افزایش بدی.\n" +
                            "ما همیشه در حال بهتر کردن برنامه هستیم، پس اگه ایده\u200Cای داشتی یا با مشکلی روبرو شدی، حتما بهمون خبر بده. نظرت برامون خیلی ارزش داره❤\uFE0F",
                    style = bodySmallCard,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun AboutButton(buttonText: String, onClick: () -> Unit) {
    Surface(
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth(0.96f)
            .height(80.dp)
            .padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp),
        onClick = { onClick.invoke() }
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(14.dp)
            )

            Text(
                text = buttonText,
                style = bodySmallCard,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun AboutDeveloperCard(buttonText: String, onClick: () -> Unit) {
    Surface(
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth(0.96f)
            .height(80.dp)
            .padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp),
        onClick = { onClick.invoke() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buttonText,
                style = bodySmallCard,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun CircleIconCart(
    icon: Painter
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(80.dp)
    ) {
        Image(
            painter = icon,
            contentDescription = "App Icon",
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}