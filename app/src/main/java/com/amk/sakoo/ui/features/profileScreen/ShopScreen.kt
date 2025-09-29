package com.amk.sakoo.ui.features.profileScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amk.sakoo.R
import com.amk.sakoo.ui.theme.FollowerBegirTheme
import com.amk.sakoo.ui.theme.bodyLargeCard
import com.amk.sakoo.ui.theme.bodyMediumCard
import com.amk.sakoo.ui.theme.bodySmallCard
import com.amk.sakoo.util.RSA_KEY
import com.amk.sakoo.util.formatBalanceWithCommas
import com.amk.sakoo.util.toPersianDigits
import com.maxkeppeker.sheets.core.CoreDialog
import com.maxkeppeker.sheets.core.models.CoreSelection
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState

@Preview(showBackground = true)
@Composable
fun ShopScreenPreview() {
    FollowerBegirTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ShopScreen()
        }
    }
}

@SuppressLint("ResourceAsColor")
@Composable
fun ShopScreen() {
    val context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val activityResultRegistry =
        LocalActivityResultRegistryOwner.current!!.activityResultRegistry

    val purchaseDialogState = rememberUseCaseState()
    val purchaseInfo = remember { mutableStateOf("") }

    val viewModel: AccountViewModel = viewModel()
    val paymentViewModel: PaymentViewModel = viewModel()

    LaunchedEffect(Unit) {
        paymentViewModel.initSecurityCheck(RSA_KEY)
        paymentViewModel.initPaymentConfiguration()
        paymentViewModel.initPayment(context)
        paymentViewModel.connectToBazaar(
            onSuccess = {
                Log.v("LoginScreen", "Connected to Bazaar")
            },
            onFailure = { throwable ->
                Toast.makeText(context, "خطا در برقراری ارتباط با کافه بازار، لطفا با پشتیبانی تماس بگیرید", Toast.LENGTH_SHORT)
                    .show()
                Log.v("LoginScreen", "Failed to connect: ${throwable.message}")
            },
            onDisconnected = {
                Log.v("LoginScreen", "Disconnected from Bazaar")
            }
        )
        viewModel.getBazaarLogin(context, lifecycleOwner)
        viewModel.loadUserData(context, lifecycleOwner)
    }


    PurchaseDialog(
        infoDialogState = purchaseDialogState,
        bodyText = purchaseInfo.value
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Surface(
            modifier = Modifier
                .align(Alignment.End)
                .height(76.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            onClick = { },
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(30.dp),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp)
                    .wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                val walletText = when {
                    viewModel.isLoading.value -> {
                        buildAnnotatedString {
                            append("...")
                        }
                    }

                    else -> {
                        val currentPoints = viewModel.wallet.value
                            .formatBalanceWithCommas()
                            .toPersianDigits()
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                append("موجودی کیف پول: ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                append("$currentPoints تومان")
                            }
                        }
                    }
                }

                Text(
                    text = walletText,
                    style = bodySmallCard,
                )

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_shop_wallet),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp),
                    tint = Color(0xFFF59E0B)
                )

            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            BuyCoinCard(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                {
                    if (viewModel.wallet.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "charge_10k",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "خرید ناموفق. دوباره رو همین محصول کلیک کن تا به موجودیت اضافه بشه",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.increaseWallet(context, lifecycleOwner, 10000)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        purchaseEntity.purchaseToken
                                    purchaseDialogState.show()
                                }, {})
                            })
                    } else {
                        Toast.makeText(
                            context,
                            "اول باید به اینترنت وصل باشی تا بتونی خرید کنی",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                "۱۰,۰۰۰ تومان",
                Color(0xFFD6EEFF),
                Color(0xFF00ADEF),
                false,
                ""
            )

            BuyCoinCard(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                {
                    if (viewModel.wallet.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "charge_5k",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "خرید ناموفق. دوباره رو همین محصول کلیک کن تا به موجودیت اضافه بشه",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.increaseWallet(context, lifecycleOwner, 5000)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        purchaseEntity.purchaseToken
                                    purchaseDialogState.show()
                                }, {})
                            })
                    } else {
                        Toast.makeText(
                            context,
                            "اول باید به اینترنت وصل باشی تا بتونی خرید کنی",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                "۵,۰۰۰ تومان",
                Color(0xFFD5FAF0),
                Color(0xFF00C2A8),
                false,
                ""
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            BuyCoinCard(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                {
                    if (viewModel.wallet.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "charge_50k",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "خرید ناموفق. دوباره رو همین محصول کلیک کن تا به موجودیت اضافه بشه",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.increaseWallet(context, lifecycleOwner, 50000)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        purchaseEntity.purchaseToken
                                    purchaseDialogState.show()
                                }, {})
                            })
                    } else {
                        Toast.makeText(
                            context,
                            "اول باید به اینترنت وصل باشی تا بتونی خرید کنی",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, "۵۰,۰۰۰ تومان",
                Color(0xFFFFE0E0),
                Color(0xFFF44336),
                false,
                ""
            )

            BuyCoinCard(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                {
                    if (viewModel.wallet.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "charge_20k",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "خرید ناموفق. دوباره رو همین محصول کلیک کن تا به موجودیت اضافه بشه",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.increaseWallet(context, lifecycleOwner, 20000)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        purchaseEntity.purchaseToken
                                    purchaseDialogState.show()
                                }, {})
                            })
                    } else {
                        Toast.makeText(
                            context,
                            "اول باید به اینترنت وصل باشی تا بتونی خرید کنی",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, "۲۰,۰۰۰ تومان",
                Color(0xFFE0E9FF),
                Color(0xFF3D5AFE),
                false,
                ""
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            BuyCoinCard(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                {
                    if (viewModel.wallet.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "charge_200k",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "خرید ناموفق. دوباره رو همین محصول کلیک کن تا به موجودیت اضافه بشه",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.increaseWallet(context, lifecycleOwner, 200000)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        purchaseEntity.purchaseToken
                                    purchaseDialogState.show()
                                }, {})
                            })
                    } else {
                        Toast.makeText(
                            context,
                            "اول باید به اینترنت وصل باشی تا بتونی خرید کنی",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                "۲۰۰,۰۰۰ تومان",
                Color(0xFFECFFD8),
                Color(0xFF7CB342),
                false,
                ""
            )

            BuyCoinCard(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                {
                    if (viewModel.wallet.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "charge_100k",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "خرید ناموفق. دوباره رو همین محصول کلیک کن تا به موجودیت اضافه بشه",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.increaseWallet(context, lifecycleOwner, 100000)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        purchaseEntity.purchaseToken
                                    purchaseDialogState.show()
                                }, {})
                            })
                    } else {
                        Toast.makeText(
                            context,
                            "اول باید به اینترنت وصل باشی تا بتونی خرید کنی",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, "۱۰۰,۰۰۰ تومان",
                Color(0xFFFFF4D6),
                Color(0xFFFFA726),
                false,
                ""
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            BuyCoinCard(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                {
                    if (viewModel.wallet.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "charge_500k",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "خرید ناموفق. دوباره رو همین محصول کلیک کن تا به موجودیت اضافه بشه",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.increaseWallet(context, lifecycleOwner, 500000)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        purchaseEntity.purchaseToken
                                    purchaseDialogState.show()
                                }, {})
                            })
                    } else {
                        Toast.makeText(
                            context,
                            "اول باید به اینترنت وصل باشی تا بتونی خرید کنی",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                "۵۰۰,۰۰۰ تومان",
                Color(0xFFE0F7FA),
                Color(0xFF0097A7),
                false,
                ""
            )

            BuyCoinCard(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                {
                    if (viewModel.wallet.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "charge_300k",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "خرید ناموفق. دوباره رو همین محصول کلیک کن تا به موجودیت اضافه بشه",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.increaseWallet(context, lifecycleOwner, 300000)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        purchaseEntity.purchaseToken
                                    purchaseDialogState.show()
                                }, {})
                            })
                    } else {
                        Toast.makeText(
                            context,
                            "اول باید به اینترنت وصل باشی تا بتونی خرید کنی",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, "۳۰۰,۰۰۰ تومان",
                Color(0xFFFFE6F0),
                Color(0xFFE91E63),
                false,
                ""
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            BuyCoinCard(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                {
                    if (viewModel.wallet.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "charge_2m",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "خرید ناموفق. دوباره رو همین محصول کلیک کن تا به موجودیت اضافه بشه",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.increaseWallet(context, lifecycleOwner, 2000000)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        purchaseEntity.purchaseToken
                                    purchaseDialogState.show()
                                }, {})
                            })
                    } else {
                        Toast.makeText(
                            context,
                            "اول باید به اینترنت وصل باشی تا بتونی خرید کنی",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                "۲,۰۰۰,۰۰۰ تومان",
                Color(0xFFF3E5F5),
                Color(0xFF7E57C2),
                true,
                "۵٪ تخفیف"
            )

            BuyCoinCard(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                {
                    if (viewModel.wallet.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "charge_1m",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "خرید ناموفق. دوباره رو همین محصول کلیک کن تا به موجودیت اضافه بشه",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.increaseWallet(context, lifecycleOwner, 1000000)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        purchaseEntity.purchaseToken
                                    purchaseDialogState.show()
                                }, {})
                            })
                    } else {
                        Toast.makeText(
                            context,
                            "اول باید به اینترنت وصل باشی تا بتونی خرید کنی",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, "۱,۰۰۰,۰۰۰ تومان",
                Color(0xFFE8EAF6),
                Color(0xFF3949AB),
                true,
                "۵٪ تخفیف"
            )

        }

        Text(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .clickable {
                    val telegramId = "amk_support"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/$telegramId"))
                    context.startActivity(intent)
                },
            text = "نیاز به پشتیبانی داری؟ کلیک کن",
            style = bodySmallCard,
            color = MaterialTheme.colorScheme.primary
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyCoinCard(
    modifier: Modifier = Modifier,
    onCardClicked: () -> Unit,
    mainTextCard: String,
    circleBackgroundColor: Color,
    iconColor: Color,
    hasOff: Boolean,
    offValue: String
) {
    Surface(
        shadowElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth(0.4f)
            .height(230.dp)
            .padding(vertical = 8.dp),
        onClick = { onCardClicked.invoke() },
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AddCartCircleIcon(
                ImageVector.vectorResource(R.drawable.ic_add_shopping_cart),
                circleBackgroundColor,
                iconColor
            )

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Text(
                    modifier = Modifier.padding(top = 18.dp, bottom = 4.dp),
                    text = mainTextCard,
                    style = bodyMediumCard,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "شارژ کیف پول",
                    style = bodySmallCard,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (hasOff) {
                    Box(
                        modifier =
                            Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.error,
                                    shape = RoundedCornerShape(size = 6.dp)
                                )
                                .padding(start = 8.dp, top = 3.dp, end = 8.dp, bottom = 4.dp)
                    ) {
                        Text(
                            text = offValue,
                            style = bodySmallCard,
                            color = MaterialTheme.colorScheme.onError
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun AddCartCircleIcon(
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(80.dp)
            .background(color = backgroundColor, shape = CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(36.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseDialog(infoDialogState: UseCaseState, bodyText: String) {
    CoreDialog(
        state = infoDialogState,
        selection = CoreSelection(
            withButtonView = false,
        ),
        header = Header.Custom {
            Text(
                text = "خرید موفق",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                style = bodyLargeCard,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End
            )
        },
        onPositiveValid = true,
        body = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "توکن: $bodyText",
                textAlign = TextAlign.Right,
                style = bodyMediumCard,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    )
}