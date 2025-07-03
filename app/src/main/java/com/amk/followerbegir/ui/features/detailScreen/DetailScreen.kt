package com.amk.followerbegir.ui.features.detailScreen

import android.annotation.SuppressLint
import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.amk.followerbegir.ui.features.profileScreen.AccountViewModel
import com.amk.followerbegir.ui.theme.FollowerBegirTheme
import com.amk.followerbegir.util.MyScreens
import dev.burnoo.cokoin.navigation.getNavController
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
    val accountViewModel = getNavViewModel<AccountViewModel>()
    val detail = viewModel.itemDetail.value
    val isLoading = viewModel.isLoading.value
    val isError = viewModel.isError.value
    val orderMessage = viewModel.orderMessage.value
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val navController = getNavController()

    val pageId = remember { mutableStateOf("") }
    val pageIdError = remember { mutableStateOf("") }

    val quantity = remember { mutableStateOf("") }
    val quantityError = remember { mutableStateOf("") }

    val showConfirmDialog = remember { mutableStateOf(false) }
    val hasReadDescription = remember { mutableStateOf(false) }

    LaunchedEffect(serviceId) {
        if (serviceId != null) {
            viewModel.loadServiceDetail(serviceId)
        }
        accountViewModel.getBazaarLogin(context, lifecycleOwner)
        accountViewModel.loadUserData(context, lifecycleOwner)
    }

    when {
        isLoading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text("لطفا کمی صبر کنید")
            }
        }

        isError && detail == null -> {
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
                Text("ثبت سفارش", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = pageId.value,
                    onValueChange = {
                        pageId.value = it
                        pageIdError.value = if (it.any { ch -> ch in 'آ'..'ی' }) {
                            "لطفاً فقط از حروف انگلیسی، اعداد یا کاراکترهای مجاز استفاده کنید"
                        } else ""
                    },
                    isError = pageIdError.value.isNotEmpty(),
                    label = { Text("آیدی پیج یا لینک") },
                    supportingText = {
                        if (pageIdError.value.isNotEmpty()) {
                            Text(pageIdError.value, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = quantity.value,
                    onValueChange = {
                        quantity.value = it
                        quantityError.value = if (it.isNotBlank()) {
                            val intVal = it.toIntOrNull()
                            if (intVal == null || intVal !in detail.min..detail.max) {
                                "تعداد باید بین ${detail.min} تا ${detail.max} باشد"
                            } else ""
                        } else ""
                    },
                    isError = quantityError.value.isNotEmpty(),
                    label = { Text("تعداد سفارش (${detail.min} تا ${detail.max})") },
                    supportingText = {
                        if (quantityError.value.isNotEmpty()) {
                            Text(quantityError.value, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val isPageIdValid = pageId.value.isNotBlank() && pageIdError.value.isEmpty()
                        val isQuantityValid = quantity.value.toIntOrNull()?.let {
                            it in detail.min..detail.max
                        } == true

                        if (!isPageIdValid || !isQuantityValid) {
                            if (pageId.value.isBlank()) pageIdError.value = "لطفاً آیدی را وارد کنید"
                            if (quantity.value.isBlank()) quantityError.value = "لطفاً تعداد سفارش را وارد کنید"
                            Toast.makeText(context, "لطفاً موارد وارد شده را بررسی کنید", Toast.LENGTH_LONG).show()
                        } else {
                            showConfirmDialog.value = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ثبت سفارش")
                }

                if (showConfirmDialog.value) {
                    val quantityValue = quantity.value.toIntOrNull() ?: 0
                    val totalPrice = ((quantityValue / 1000f) * detail.rate).toInt()

                    AlertDialog(
                        onDismissRequest = {
                            showConfirmDialog.value = false
                            hasReadDescription.value = false
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (!accountViewModel.hasLogin.value) {
                                        Toast.makeText(
                                            context,
                                            "برای ثبت سفارش باید وارد حساب کاربری خود شوید.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        showConfirmDialog.value = false
                                        hasReadDescription.value = false
                                        return@Button
                                    }

                                    val currentWallet = accountViewModel.wallet.value
                                    if (currentWallet >= totalPrice) {
                                        accountViewModel.decreaseWallet(
                                            context,
                                            lifecycleOwner,
                                            totalPrice,
                                            onError = {
                                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                        viewModel.addOrderService(
                                            serviceId?.toInt() ?: 0,
                                            pageId.value,
                                            quantityValue
                                        )
                                        showConfirmDialog.value = false
                                        hasReadDescription.value = false
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "موجودی کیف پول کافی نیست. لطفاً آن را شارژ کنید.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        showConfirmDialog.value = false
                                        navController.navigate(MyScreens.ShopScreen.route)
                                    }
                                },
                                enabled = hasReadDescription.value
                            ) {
                                Text("آره، ثبت کن")
                            }
                        },
                        dismissButton = {
                            Button(onClick = {
                                showConfirmDialog.value = false
                                hasReadDescription.value = false
                            }) {
                                Text("خیر")
                            }
                        },
                        title = { Text("تأیید ثبت سفارش") },
                        text = {
                            Column {
                                Text("آیا از ثبت این سفارش مطمئنی؟")
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "قیمت نهایی سفارش: ${String.format("%,d", totalPrice)} تومان",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = hasReadDescription.value,
                                        onCheckedChange = { hasReadDescription.value = it }
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("توضیحات را خواندم")
                                }
                            }
                        }
                    )
                }

                if (orderMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        orderMessage,
                        color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }

                LaunchedEffect(orderMessage) {
                    if (orderMessage == "✅ سفارش با موفقیت ثبت شد" && !viewModel.isOrderSaved.value) {
                        viewModel.isOrderSaved.value = true
                        accountViewModel.addOrderNumber(
                            context,
                            lifecycleOwner,
                            viewModel.orderId.value ?: return@LaunchedEffect
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("مشخصات سرویس", style = MaterialTheme.typography.headlineSmall)
                DetailCard("نام سرویس", detail.name)
                DetailCard("قیمت (هر هزار تا)", "${detail.rate} تومان")

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("توضیحات", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        HtmlDescriptionView(detail.desc)
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
