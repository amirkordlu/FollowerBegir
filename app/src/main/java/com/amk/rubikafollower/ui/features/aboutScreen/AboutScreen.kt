package com.amk.rubikafollower.ui.features.aboutScreen

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
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

        AboutButton("معرفی به دوستان") {

        }

        AboutButton("گزارش باگ") {

        }

        AboutDeveloperCard(
            "Designed by Stitch AI powered by Google" +
                    "\n<Developed by AMK/>"
        ) {

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppCard() {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth(0.96f)
            .height(210.dp)
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircleIconCart(
                ImageVector.vectorResource(R.drawable.ic_wallet)
            )

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Text(
                    modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
                    text = "فالوئر بگیر روبیکا",
                    style = bodyMediumCard,
                    color = Color(0xFF000000),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "نسخه 1.0.0",
                    style = bodySmallCard,
                    fontSize = 12.sp,
                    color = Color(0xFF595959)
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutTextCard() {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth(0.96f)
            .height(240.dp)
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
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
                    text = "لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از طراحان گرافیک است. چاپگرها و متون بلکه روزنامه و مجله در ستون و سطرآنچنان که لازم است و برای شرایط فعلی تکنولوژی مورد نیاز و کاربردهای متنوع با هدف بهبود ابزارهای کاربردی می باشد.",
                    style = bodySmallCard,
                    color = Color(0xFF000000)
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutButton(buttonText: String, onClick: () -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth(0.96f)
            .height(80.dp)
            .padding(vertical = 8.dp),
        onClick = { onClick.invoke() },
        colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
        shape = RoundedCornerShape(20.dp)
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
                tint = Color.Gray,
                modifier = Modifier
                    .size(14.dp)
            )

            Text(
                text = buttonText,
                style = bodySmallCard,
                color = Color(0xFF000000)
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDeveloperCard(buttonText: String, onClick: () -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth(0.96f)
            .height(80.dp)
            .padding(vertical = 8.dp),
        onClick = { onClick.invoke() },
        colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
        shape = RoundedCornerShape(20.dp)
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
                color = Color(0xFF000000)
            )

        }
    }
}

@Composable
fun CircleIconCart(
    icon: ImageVector
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(80.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )
    }
}