package com.amk.follower.ui.features.detailScreen

import android.annotation.SuppressLint
import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import com.amk.follower.R
import com.amk.follower.ui.features.orderScreen.ErrorSection
import com.amk.follower.ui.features.orderScreen.NoInternetSection
import com.amk.follower.ui.features.platformServicesScreen.EmptyServiceSection
import com.amk.follower.ui.features.profileScreen.AccountViewModel
import com.amk.follower.ui.theme.FollowerBegirTheme
import com.amk.follower.ui.theme.bodyLargeCard
import com.amk.follower.ui.theme.bodyMediumCard
import com.amk.follower.ui.theme.bodySmallCard
import com.amk.follower.ui.theme.customColors
import com.amk.follower.ui.theme.textFieldStyle
import com.amk.follower.util.MyScreens
import com.amk.follower.util.NetworkChecker
import com.amk.follower.util.PROFIT_PERCENT
import com.amk.follower.util.formatBalanceWithCommas
import com.amk.follower.util.toPersianDigits
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    FollowerBegirTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            DetailScreen("1")
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun DetailScreen(serviceId: String?) {
    val viewModel = getNavViewModel<DetailScreenViewModel>()
    val accountViewModel = getNavViewModel<AccountViewModel>()
    val navigation = getNavController()
    val detail = viewModel.itemDetail.value
    val isLoading = viewModel.isLoading.value
    val isError = viewModel.isError.value
    val orderMessage = viewModel.orderMessage.value
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

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
            }
        }

        isError && detail == null -> {
            if (!NetworkChecker(context).isInternetConnected) {
                NoInternetSection {
                    if (!serviceId.isNullOrEmpty()) {
                        viewModel.loadServiceDetail(serviceId)
                    }
                }
            } else {
                ErrorSection {
                    if (!serviceId.isNullOrEmpty()) {
                        viewModel.loadServiceDetail(serviceId)
                    }
                }
            }
        }

        detail != null -> {
            val scrollState = rememberScrollState()
            val coroutineScope = rememberCoroutineScope()

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    if (orderMessage != null) {
                        OrderMessagePopup(
                            message = orderMessage,
                            isError = isError
                        ) {
                            viewModel.orderMessage.value = null
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navigation.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                        Text(
                            text = "ثبت سفارش",
                            style = bodyLargeCard,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "مشخصات سرویس",
                                style = bodyLargeCard,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "نام سرویس",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 4.dp),
                                    fontFamily = FontFamily(Font(R.font.dana_medium))
                                )
                                Text(
                                    text = detail.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(textDirection = TextDirection.Rtl),
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = FontFamily(Font(R.font.dana_medium))
                                )
                            }

                            val serviceRateWithProfit = (detail.rate * PROFIT_PERCENT).toInt()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.customColors.successContainer)
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = serviceRateWithProfit.formatBalanceWithCommas()
                                        .toPersianDigits() + " تومان",
                                    style = MaterialTheme.typography.titleMedium.copy(textDirection = TextDirection.Rtl),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.customColors.onSuccessContainer,
                                    fontFamily = FontFamily(Font(R.font.dana_medium))
                                )
                                Text(
                                    text = "قیمت (هر هزار تا)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.customColors.onSuccessContainer,
                                    fontFamily = FontFamily(Font(R.font.dana_medium))
                                )
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                "توضیحات",
                                style = bodyMediumCard,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 14.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            HtmlDescriptionView(detail.desc)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 22.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 14.dp),
                                text = "آیدی پیج یا لینک",
                                style = bodyMediumCard,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            OutlinedTextField(
                                value = pageId.value,
                                onValueChange = {
                                    pageId.value = it
                                    pageIdError.value = if (it.any { ch -> ch in 'آ'..'ی' }) {
                                        "لطفاً فقط از حروف انگلیسی، اعداد یا کاراکترهای مجاز استفاده کنید"
                                    } else ""
                                },
                                isError = pageIdError.value.isNotEmpty(),
                                placeholder = {
                                    Text(
                                        text = "آیدی پیج یا لینک را وارد کنید",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Right,
                                        style = textFieldStyle,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                supportingText = {
                                    if (pageIdError.value.isNotEmpty()) {
                                        Text(
                                            pageIdError.value,
                                            color = MaterialTheme.colorScheme.error,
                                            style = bodySmallCard,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.End)
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                                ),
                                leadingIcon = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource((R.drawable.ic_insert_link)),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                modifier = Modifier.padding(bottom = 14.dp),
                                text = "تعداد سفارش" + " (${detail.min} تا ${detail.max})",
                                style = bodyMediumCard,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
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
                                placeholder = {
                                    Text(
                                        text = "تعداد مورد نظر را وارد کنید",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Right,
                                        style = textFieldStyle,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                supportingText = {
                                    if (quantityError.value.isNotEmpty()) {
                                        Text(
                                            quantityError.value,
                                            color = MaterialTheme.colorScheme.error,
                                            style = bodySmallCard,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.End)
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                                ),
                                leadingIcon = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource((R.drawable.ic_numbers)),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            val quantityValue = quantity.value.toIntOrNull() ?: 0
                            val finalPrice =
                                ((quantityValue.toFloat() / 1000f) * (detail.rate * PROFIT_PERCENT)).toInt()

                            val buttonText = if (finalPrice > 0 && quantityError.value.isEmpty()) {
                                "ثبت سفارش | ${
                                    finalPrice.formatBalanceWithCommas().toPersianDigits()
                                } تومان"
                            } else {
                                "ثبت سفارش"
                            }

                            Button(
                                onClick = {
                                    val isPageIdValid =
                                        pageId.value.isNotBlank() && pageIdError.value.isEmpty()
                                    val isQuantityValid = quantity.value.toIntOrNull()
                                        ?.let { it in detail.min..detail.max } == true

                                    if (!isPageIdValid || !isQuantityValid) {
                                        if (pageId.value.isBlank()) pageIdError.value =
                                            "لطفا آیدی را وارد کنید"
                                        if (quantity.value.isBlank()) quantityError.value =
                                            "لطفا تعداد سفارش را وارد کنید"
                                        Toast.makeText(
                                            context,
                                            "موارد وارد شده را بررسی کنید",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        showConfirmDialog.value = true
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_shopping_cart_checkout),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.padding(end = 8.dp))
                                Text(
                                    text = buttonText,
                                    style = bodyMediumCard,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.StartEllipsis
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }

                    if (showConfirmDialog.value) {
                        val quantityValue = quantity.value.toIntOrNull() ?: 0
                        val totalPriceWithProfit =
                            (quantityValue.toFloat() / 1000f) * (detail.rate * PROFIT_PERCENT)
                        val finalPrice = totalPriceWithProfit.toInt()

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
                                            navigation.navigate(MyScreens.ProfileScreen.route)
                                            return@Button
                                        }

                                        val currentWallet = accountViewModel.wallet.value
                                        if (currentWallet >= finalPrice) {
                                            accountViewModel.decreaseWallet(
                                                context,
                                                lifecycleOwner,
                                                finalPrice,
                                                onError = {
                                                    Toast.makeText(context, it, Toast.LENGTH_SHORT)
                                                        .show()
                                                })
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
                                            navigation.navigate(MyScreens.ShopScreen.route)
                                        }
                                    },
                                    enabled = hasReadDescription.value
                                ) {
                                    Text(
                                        "آره، ثبت کن",
                                        fontFamily = FontFamily(Font(R.font.dana_medium))
                                    )
                                }
                            },
                            dismissButton = {
                                Button(onClick = {
                                    showConfirmDialog.value = false
                                    hasReadDescription.value = false
                                }) {
                                    Text("خیر", fontFamily = FontFamily(Font(R.font.dana_medium)))
                                }
                            },
                            title = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "ثبت سفارش",
                                    style = bodyLargeCard,
                                    textAlign = TextAlign.Right,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            text = {
                                Column {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "آیا از ثبت این سفارش مطمئنی؟",
                                        style = bodyMediumCard,
                                        textAlign = TextAlign.Right,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "قیمت نهایی سفارش: ${
                                            finalPrice.formatBalanceWithCommas().toPersianDigits()
                                        } تومان",
                                        style = bodyMediumCard,
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.Right
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                hasReadDescription.value = !hasReadDescription.value
                                            },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = "توضیحات را خواندم",
                                            style = bodySmallCard,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Checkbox(
                                            checked = hasReadDescription.value,
                                            onCheckedChange = { hasReadDescription.value = it }
                                        )
                                    }
                                }
                            }
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
                }

                AnimatedVisibility(
                    visible = scrollState.canScrollForward,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp, end = 16.dp),
                    enter = slideInVertically(initialOffsetY = { it * 2 }),
                    exit = slideOutVertically(targetOffsetY = { it * 2 })
                ) {
                    FloatingActionButton(
                        onClick = {
                            coroutineScope.launch {
                                scrollState.animateScrollTo(scrollState.maxValue)
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Scroll to End"
                        )
                    }
                }
            }
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                EmptyServiceSection()
            }
        }
    }
}

@Composable
fun HtmlDescriptionView(htmlText: String) {
    val context = LocalContext.current
    val textColor = MaterialTheme.colorScheme.onSurface

    val spannedText = remember(htmlText) {
        HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    val customFont = remember {
        ResourcesCompat.getFont(context, R.font.dana_medium)
    }

    AndroidView(
        factory = {
            TextView(it).apply {
                text = spannedText
                textSize = 14f
                setTextColor(textColor.toArgb())
                typeface = customFont
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = {
            it.setTextColor(textColor.toArgb())
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun OrderMessagePopup(
    message: String,
    isError: Boolean,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    val backgroundColor =
        if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.customColors.successContainer
    val contentColor =
        if (isError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.customColors.onSuccessContainer
    val iconColor =
        if (isError) MaterialTheme.colorScheme.error else MaterialTheme.customColors.success

    LaunchedEffect(Unit) {
        delay(3000)
        visible = false
        delay(500)
        onDismiss()
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(initialOffsetY = { -it }),
        exit = fadeOut(animationSpec = tween(500)) + slideOutVertically(targetOffsetY = { -it })
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.0f)),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 6.dp,
                color = backgroundColor,
                modifier = Modifier
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(if (isError) R.drawable.ic_error else R.drawable.ic_success),
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = message,
                        style = bodyMediumCard.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = contentColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}