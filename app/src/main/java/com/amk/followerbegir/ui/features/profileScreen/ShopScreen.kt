package com.amk.followerbegir.ui.features.profileScreen

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivityResultRegistryOwner
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amk.followerbegir.R
import com.amk.followerbegir.ui.theme.FollowerBegirTheme
import com.amk.followerbegir.ui.theme.Typography
import com.amk.followerbegir.ui.theme.bodySmallCard
import com.amk.followerbegir.util.MyScreens
import com.amk.followerbegir.util.NetworkChecker
import com.amk.followerbegir.util.RSA_KEY
import com.maxkeppeker.sheets.core.CoreDialog
import com.maxkeppeker.sheets.core.models.CoreSelection
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import dev.burnoo.cokoin.navigation.getNavController

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

@Composable
fun ShopScreen() {
    val navigation = getNavController()
    BackHandler(enabled = true) {
        navigation.navigate(MyScreens.MainScreen.route) {
            popUpTo(MyScreens.MainScreen.route) {
                inclusive = true
            }
        }
    }
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
                Log.v("LoginScreen", "Failed to connect: ${throwable.message}")
            },
            onDisconnected = {
                Log.v("LoginScreen", "Disconnected from Bazaar")
            }
        )
        // Get login and points
        viewModel.getBazaarLogin(context, lifecycleOwner)
        viewModel.loadPointsFromBazaar(context, lifecycleOwner)
    }


    PurchaseDialog(
        infoDialogState = purchaseDialogState,
        bodyText = purchaseInfo.value
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Color(0xFFF5F7FA)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, bottom = 8.dp, top = 12.dp)
                .background(
                    color = Color(0xFF7E84F9),
                    shape = RoundedCornerShape(size = 16.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Image(
                painterResource(R.drawable.ic_coin),
                null,
                modifier = Modifier
                    .size(44.dp)
                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp, end = 4.dp)
            )

            Text(
                text = when {
                    viewModel.isLoading.value -> {
                        "در حال بارگذاری..."
                    }

//                    viewModel.points.value == null -> {
//                        viewModel.addPoints(context, lifecycleOwner, 2)
//                        Toast.makeText(
//                            context,
//                            "به مناسبت ورود به برنامه 2 تا سکه مهمون ما باش :)",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        "2 امتیاز گرفتی"
//                    }

                    !NetworkChecker(context).isInternetConnected -> {
                        ":( اینترنت نداری"
                    }

                    else -> {
                        val currentPoints = viewModel.points.value
                        "موجودی کیف پول: $currentPoints"
                    }
                },
                style = Typography.bodyMedium,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 12.dp)
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
                    if (viewModel.points.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "25coin",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(context, "ناموفق", Toast.LENGTH_SHORT).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.addPoints(context, lifecycleOwner, 25)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        "token: ${purchaseEntity.purchaseToken}"
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
                }, R.drawable.ic_coin, "خرید 25 سکه", "119 هزار تومان", true, "٪3 تخفیف"
            )

            BuyCoinCard(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                {
                    if (viewModel.points.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "10coin",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(context, "ناموفق", Toast.LENGTH_SHORT).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.addPoints(context, lifecycleOwner, 10)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        "token: ${purchaseEntity.purchaseToken}"
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
                }, R.drawable.ic_coin, "خرید 10 سکه", "49 هزار تومان", false, ""
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
                    if (viewModel.points.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "100coin",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(context, "ناموفق", Toast.LENGTH_SHORT).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.addPoints(context, lifecycleOwner, 100)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        "token: ${purchaseEntity.purchaseToken}"
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
                }, R.drawable.ic_coin, "خرید 100 سکه", "399 هزار تومان", true, "٪18 تخفیف"
            )

            BuyCoinCard(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                {
                    if (viewModel.points.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "50coin",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(context, "ناموفق", Toast.LENGTH_SHORT).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.addPoints(context, lifecycleOwner, 50)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        "token: ${purchaseEntity.purchaseToken}"
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
                }, R.drawable.ic_coin, "خرید 50 سکه", "229 هزار تومان", true, "٪6 تخفیف"
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
                    if (viewModel.points.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "500coin",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(context, "ناموفق", Toast.LENGTH_SHORT).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.addPoints(context, lifecycleOwner, 500)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        "token: ${purchaseEntity.purchaseToken}"
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
                R.drawable.ic_coin,
                "خرید 500 سکه",
                "1 میلیون و 799 هزار تومان",
                true,
                "٪25 تخفیف"
            )

            BuyCoinCard(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                {
                    if (viewModel.points.value != null && viewModel.hasLogin.value) {
                        paymentViewModel.startPurchase(
                            "200coin",
                            "purchasePayload",
                            activityResultRegistry,
                            onFailure = {
                                Toast.makeText(context, "ناموفق", Toast.LENGTH_SHORT).show()
                            },
                            onSuccess = { purchaseEntity ->
                                viewModel.addPoints(context, lifecycleOwner, 200)
                                paymentViewModel.consumePurchase(purchaseEntity.purchaseToken, {
                                    purchaseInfo.value =
                                        "token: ${purchaseEntity.purchaseToken}"
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
                }, R.drawable.ic_coin, "خرید 200 سکه", "749 هزار تومان", true, "٪24 تخفیف"
            )

        }

        Text(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .clickable {
//                   sendEmail(context,"amir.kordlu@gmail.com")
                },
            text = "نیاز به پشتیبانی داری؟ کلیک کن",
            style = Typography.bodyMedium
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyCoinCard(
    modifier: Modifier = Modifier,
    onCardClicked: () -> Unit,
    cardImage: Int,
    mainTextCard: String,
    secondaryTextCard: String,
    hasOff: Boolean,
    offValue: String
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .fillMaxWidth(0.5f)
            .height(230.dp)
            .padding(vertical = 10.dp),
        onClick = { onCardClicked.invoke() },
        colors = CardDefaults.cardColors(Color(0xFF7E84F9))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(cardImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(54.dp)
            )

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = mainTextCard,
                    style = Typography.bodyMedium,
                    fontSize = 18.sp,
                    color = Color(0xFFFFFFFF)
                )

                Spacer(modifier = Modifier.padding(vertical = 6.dp))

                Text(
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                    text = secondaryTextCard,
                    style = Typography.bodyMedium,
                    color = Color(0xFFFFFFFF)
                )

                if (hasOff) {
                    Box(
                        modifier =
                            Modifier
                                .background(
                                    color = Color(0xFFEF5350),
                                    shape = RoundedCornerShape(size = 6.dp)
                                )
                                .padding(start = 8.dp, top = 3.dp, end = 8.dp, bottom = 4.dp)
                    ) {
                        Text(
                            text = offValue,
                            style = bodySmallCard
                        )

                    }
                }
            }
        }
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
                style = Typography.bodyLarge
            )
        },
        onPositiveValid = true,
        body = {
            Text(
                text = bodyText,
                textAlign = TextAlign.Right,
                style = Typography.bodyMedium
            )
        }
    )
}



