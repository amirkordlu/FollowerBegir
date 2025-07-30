package com.amk.followerbegir.ui.features.orderScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amk.followerbegir.R
import com.amk.followerbegir.model.data.OrderStatusResponse
import com.amk.followerbegir.ui.features.profileScreen.AccountViewModel
import com.amk.followerbegir.ui.theme.FollowerBegirTheme
import com.amk.followerbegir.ui.theme.LightColorScheme
import com.amk.followerbegir.ui.theme.bodyLargeCard
import com.amk.followerbegir.ui.theme.bodyMediumCard
import com.amk.followerbegir.ui.theme.bodySmallCard
import com.amk.followerbegir.util.getPersianStatus
import com.amk.followerbegir.util.getRemain
import com.amk.followerbegir.util.getStatusIconInfo
import com.amk.followerbegir.util.toPersianDigits
import dev.burnoo.cokoin.navigation.getNavViewModel

@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    FollowerBegirTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            OrderScreen()
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun OrderScreen() {
    val accountViewModel = getNavViewModel<AccountViewModel>()
    val orderViewModel = getNavViewModel<OrderScreenViewModel>()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val isAccountLoading = accountViewModel.isLoading.value
    val orderNumbers = accountViewModel.orderNumbers.value
    val orderStatuses = orderViewModel.ordersStatusMap.value
    val isOrderLoading = orderViewModel.isLoading.value
    val isOrderError = orderViewModel.isError.value

    LaunchedEffect(Unit) {
        if (orderNumbers.isEmpty()) {
            accountViewModel.loadUserData(context, lifecycleOwner)
        }
    }

    LaunchedEffect(orderNumbers) {
        if (orderNumbers.isNotEmpty() && orderStatuses.isEmpty()) {
            val joinedOrders = orderNumbers.joinToString(",")
            orderViewModel.getOrdersStatus(joinedOrders)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            isAccountLoading || isOrderLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            isOrderError -> {
                Text(
                    text = "خطا در دریافت وضعیت سفارش‌ها",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            orderNumbers.isEmpty() -> {
                Text(
                    text = "هیچ سفارشی ثبت نشده است.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                val orderStatusList = orderStatuses.values.toList()

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(orderStatusList) { orderStatus ->
                        OrderStatusCard(orderStatus = orderStatus)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderStatusCard(orderStatus: OrderStatusResponse) {
    val clipboardManager = LocalClipboardManager.current
    val orderNumber = orderStatus.order.toPersianDigits()
    val statusText = getPersianStatus(orderStatus.status)
    val remains = getRemain(orderStatus.remains).toString()
    val startCount = getRemain(orderStatus.start_count).toString()
    val statusIconInfo = getStatusIconInfo(orderStatus.status)

    val copyText = """
        سفارش: $orderNumber
        وضعیت: $statusText
        تعداد باقی‌مانده: $remains
        شروع از: $startCount
    """.trimIndent()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 2.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightColorScheme.background),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(copyText))
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_copy),
                        contentDescription = null
                    )
                }

                Text(
                    "سفارش $orderNumber",
                    style = bodyLargeCard,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 12.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = statusIconInfo.iconRes),
                        contentDescription = null,
                        tint = statusIconInfo.tint,
                        modifier = Modifier
                            .size(28.dp)
                            .padding(end = 2.dp)
                    )

                    LabeledText(
                        label = "وضعیت",
                        value = statusText,
                        valueColor = statusIconInfo.tint,
                        textStyle = bodyMediumCard
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LabeledText(
                    label = "تعداد باقی‌مانده",
                    value = remains
                )

                Spacer(modifier = Modifier.height(8.dp))

                LabeledText(
                    label = "شروع از",
                    value = startCount
                )
            }
        }
    }
}

@Composable
fun LabeledText(
    label: String,
    value: String,
    textStyle: TextStyle = bodySmallCard,
    valueColor: Color = Color.Black
) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$label: ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = valueColor)) {
                append(value)
            }
        },
        style = textStyle
    )
}
