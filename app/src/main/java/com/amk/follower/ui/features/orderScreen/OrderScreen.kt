package com.amk.follower.ui.features.orderScreen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amk.follower.R
import com.amk.follower.model.data.OrderStatusResponse
import com.amk.follower.ui.features.profileScreen.AccountViewModel
import com.amk.follower.ui.theme.FollowerBegirTheme
import com.amk.follower.ui.theme.bodyLargeCard
import com.amk.follower.ui.theme.bodyMediumCard
import com.amk.follower.ui.theme.bodySmallCard
import com.amk.follower.util.NetworkChecker
import com.amk.follower.util.getPersianStatus
import com.amk.follower.util.getRemain
import com.amk.follower.util.getStatusIconInfo
import com.amk.follower.util.toPersianDigits
import com.farsitel.bazaar.BazaarClientProxy
import dev.burnoo.cokoin.navigation.getNavViewModel

@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    FollowerBegirTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
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

    val isCheckingLogin = accountViewModel.isLoginCheckInProgress.value
    val isLoggedIn = accountViewModel.hasLogin.value
    val isAccountLoading = accountViewModel.isLoading.value
    val orderNumbers = accountViewModel.orderNumbers.value

    val orderStatuses = orderViewModel.ordersStatusMap.value
    val isOrderLoading = orderViewModel.isLoading.value
    val isOrderError = orderViewModel.isError.value

    var hasInternet by remember { mutableStateOf(true) }

    val retryLoad = {
        hasInternet = NetworkChecker(context).isInternetConnected
        if (hasInternet) {
            accountViewModel.getBazaarLogin(context, lifecycleOwner)
            accountViewModel.loadUserData(context, lifecycleOwner)
        }
    }

    LaunchedEffect(Unit) {
        retryLoad()
    }

    // if (isLoggedIn && orderNumbers.isNotEmpty() && orderStatuses.isEmpty()) {

    LaunchedEffect(orderNumbers) {
        if (isLoggedIn && orderNumbers.isNotEmpty()) {
            val joinedOrders = orderNumbers.joinToString(",")
            orderViewModel.getOrdersStatus(joinedOrders)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            !BazaarClientProxy.isBazaarInstalledOnDevice(context) -> {
                LaunchedEffect(Unit) {
                    Toast.makeText(
                        context,
                        "برای استفاده از برنامه لطفا کافه بازار را نصب کنید.",
                        Toast.LENGTH_SHORT
                    ).show()
                    BazaarClientProxy.showInstallBazaarView(context)
                }
            }

            !hasInternet -> {
                NoInternetSection(retryLoad)
            }

            isCheckingLogin || isAccountLoading || isOrderLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            !isLoggedIn -> {
                NoLoginSection()
            }

            isOrderError -> {
                ErrorSection {
                    val joinedOrders = orderNumbers.joinToString(",")
                    orderViewModel.getOrdersStatus(joinedOrders)
                }
            }

            orderNumbers.isEmpty() -> {
                EmptyOrderSection()
            }

            else -> {
                val orderStatusList = orderStatuses.values.toList()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(orderStatusList) { orderStatus ->
                        OrderStatusCard(orderStatus = orderStatus)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp,
        onClick = { clipboardManager.setText(AnnotatedString(copyText)) }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                OrderStatusChip(
                    text = statusText,
                    iconRes = statusIconInfo.iconRes,
                    backgroundColor = statusIconInfo.tint.copy(alpha = 0.1f),
                    contentColor = statusIconInfo.tint
                )

                Text(
                    "سفارش $orderNumber",
                    style = bodyLargeCard,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 17.sp
                )
            }

            Surface(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                shape = RoundedCornerShape(size = 12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = remains,
                            style = bodySmallCard,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = startCount,
                            style = bodySmallCard,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = ":تعداد باقی‌مانده",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = bodySmallCard,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_inventory),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = ":شروع از",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = bodySmallCard,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_start),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderStatusChip(
    text: String,
    iconRes: Int,
    backgroundColor: Color,
    contentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        modifier = Modifier.height(38.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = text,
                color = contentColor,
                style = bodySmallCard
            )
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun EmptyOrderSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_shopping_off),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(76.dp)
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Text(
            text = "هنوز چیزی سفارش ندادی",
            style = bodyMediumCard,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            text = "سفارش‌های فعلی و گذشته‌ات اینجا نمایش داده میشن",
            style = bodyMediumCard,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NoInternetSection(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_internet_off),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(76.dp)
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Text(
            text = "اینترنت نداری",
            style = bodyMediumCard,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            text = "اتصال اینترنت رو بررسی کن و دوباره تلاش کن",
            style = bodyMediumCard,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Button(onClick = { onClick.invoke() }) {
            Text("تلاش مجدد", style = bodyMediumCard, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun NoLoginSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_login),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(76.dp)
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Text(
            text = "وارد حسابت نشدی",
            style = bodyMediumCard,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            text = "از بخش پروفایل وارد حساب بازارت شو تا سفارشاتت رو ببینی",
            style = bodyMediumCard,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ErrorSection(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_error),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(76.dp)
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Text(
            text = "خطا در دریافت اطلاعات",
            style = bodyMediumCard,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            text = "یکم دیگه دوباره تلاش کن و اگر درست نشد با پشتیبانی تماس بگیر",
            style = bodyMediumCard,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { onClick.invoke() }) {
            Text("تلاش مجدد", style = bodyMediumCard, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}