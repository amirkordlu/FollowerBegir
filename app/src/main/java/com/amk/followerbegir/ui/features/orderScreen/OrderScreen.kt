package com.amk.followerbegir.ui.features.orderScreen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amk.followerbegir.model.data.OrderStatusResponse
import com.amk.followerbegir.ui.features.profileScreen.AccountViewModel
import com.amk.followerbegir.ui.theme.FollowerBegirTheme
import com.amk.followerbegir.util.getPersianStatus
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

    val isLoading = accountViewModel.isLoading.value
    val orders = accountViewModel.orderNumbers.value
    val orderStatuses = orderViewModel.ordersStatusMap.value

    LaunchedEffect(Unit) {
        accountViewModel.loadUserData(context, lifecycleOwner)
    }

    LaunchedEffect(orders) {
        if (orders.isNotEmpty()) {
            val joinedOrders = orders.joinToString(",")
            orderViewModel.getOrdersStatus(joinedOrders)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            orders.isEmpty() -> {
                Text(
                    text = "هیچ سفارشی ثبت نشده است.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                val orderStatusList = orderStatuses.values.toList()

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("شماره سفارش: ${orderStatus.order}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("وضعیت: ${getPersianStatus(orderStatus.status)}")
            Text("تعداد باقی‌مانده: ${orderStatus.remains}")
            Text("شروع از: ${orderStatus.start_count}")
        }
    }
}
