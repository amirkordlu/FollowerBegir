package com.amk.followerbegir.ui.features.detailScreen

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.amk.followerbegir.ui.theme.FollowerBegirTheme
import dev.burnoo.cokoin.navigation.getNavViewModel

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    FollowerBegirTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            DetailScreen("")
        }
    }
}

@Composable
fun DetailScreen(serviceId: String?) {
    val viewModel = getNavViewModel<DetailScreenViewModel>()
    val detail = viewModel.itemDetail.value
    val isLoading = viewModel.isLoading.value
    val isError = viewModel.isError.value

    LaunchedEffect(Unit) {
        if (detail == null && serviceId != null) {
            viewModel.loadServiceDetail(serviceId)
        }
    }


    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        isError -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("خطا در دریافت اطلاعات", color = MaterialTheme.colorScheme.error)
            }
        }

        detail != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "مشخصات سرویس",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                DetailCard(title = "نام سرویس", value = detail.name)
                DetailCard(title = "شناسه سرویس", value = detail.service)
                DetailCard(title = "حداقل سفارش", value = detail.min.toString())
                DetailCard(title = "حداکثر سفارش", value = detail.max.toString())
                DetailCard(title = "قیمت", value = "${detail.rate} تومان")
                DetailCard(title = "قابل لغو", value = if (detail.cancel) "بله" else "خیر")
                DetailCard(title = "قابل شارژ مجدد", value = if (detail.refill) "بله" else "خیر")

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "توضیحات",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HtmlDescriptionView(htmlText = detail.desc)
                    }
                }
            }
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("آیتمی یافت نشد")
            }
        }
    }
}



@Composable
fun DetailCard(title: String, value: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun HtmlDescriptionView(htmlText: String) {
    val spannedText = remember(htmlText) {
        HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                text = spannedText
                textSize = 15f
                setTextColor(android.graphics.Color.BLACK)
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    )
}
