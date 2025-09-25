package com.amk.sakoo.ui.features.profileScreen

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.amk.sakoo.R
import com.amk.sakoo.ui.features.orderScreen.NoInternetSection
import com.amk.sakoo.ui.theme.FollowerBegirTheme
import com.amk.sakoo.ui.theme.bodyMediumCard
import com.amk.sakoo.ui.theme.bodySmallCard
import com.amk.sakoo.ui.theme.customColors
import com.amk.sakoo.util.MyScreens
import com.amk.sakoo.util.NetworkChecker
import com.amk.sakoo.util.formatBalanceWithCommas
import com.amk.sakoo.util.toPersianDigits
import com.farsitel.bazaar.BazaarClientProxy
import com.farsitel.bazaar.core.BazaarSignIn
import com.farsitel.bazaar.core.model.BazaarSignInOptions
import com.farsitel.bazaar.core.model.SignInOption
import dev.burnoo.cokoin.navigation.getNavController

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    FollowerBegirTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ProfileScreen()
        }
    }
}

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val viewModel: AccountViewModel = viewModel()
    val lifecycleOwner = LocalLifecycleOwner.current
    val navigation = getNavController()

    val isCheckingLogin = viewModel.isLoginCheckInProgress.value
    val isLoggedIn = viewModel.hasLogin.value

    val isConnected = NetworkChecker(context).isInternetConnected

    LaunchedEffect(isLoggedIn) {
        viewModel.getBazaarLogin(context, lifecycleOwner)
        if (isLoggedIn) {
            viewModel.loadUserData(context, lifecycleOwner)
        }
    }

    if (!isConnected) {
        NoInternetSection {
            viewModel.getBazaarLogin(context, lifecycleOwner)
            viewModel.loadUserData(context, lifecycleOwner)
        }
    } else {

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

            isCheckingLogin -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            isLoggedIn -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 14.dp, end = 20.dp, bottom = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProfileCard(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp),
                            backgroundColor = MaterialTheme.customColors.profileBalanceCard,
                            iconColor = MaterialTheme.customColors.profileBalanceIcon,
                            textColor = MaterialTheme.colorScheme.onSurface,
                            icon = R.drawable.ic_wallet_transparent,
                            text = when {
                                viewModel.isLoading.value -> "..."
                                else -> {
                                    val currentPoints =
                                        viewModel.wallet.value.formatBalanceWithCommas()
                                            .toPersianDigits()
                                    "$currentPoints تومان"
                                }
                            }
                        ) {}

                        ProfileCard(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp),
                            backgroundColor = MaterialTheme.customColors.profileAddBalanceCard,
                            iconColor = MaterialTheme.customColors.profileAddBalanceIcon,
                            textColor = MaterialTheme.colorScheme.onSurface,
                            icon = R.drawable.ic_add_wallet_transparent,
                            text = "افزایش موجودی"
                        ) {
                            navigation.navigate(MyScreens.ShopScreen.route) {
                                popUpTo(MyScreens.ShopScreen.route) { inclusive = true }
                            }
                        }
                    }

                    Column {
                        ProfileListItem("ارسال نظر", R.drawable.ic_star) {
                            val intent = Intent(Intent.ACTION_EDIT)
                            intent.setData("bazaar://details?id=${context.packageName}".toUri())
                            intent.setPackage("com.farsitel.bazaar")
                            startActivity(context, intent, null)
                        }
                        ProfileListItem("سوالات متداول", R.drawable.ic_faq) {
                            navigation.navigate(MyScreens.FaqScreen.route)
                        }
                        ProfileListItem("پشتیبانی", R.drawable.ic_support) {
                            navigation.navigate(MyScreens.SupportScreen.route)
                        }
                        ProfileListItem("درباره ما", R.drawable.ic_about) {
                            navigation.navigate(MyScreens.AboutScreen.route)
                        }
                    }
                }
            }

            else -> {
                var showPrivacyDialog by remember { mutableStateOf(false) }

                if (showPrivacyDialog) {
                    AlertDialog(
                        onDismissRequest = { showPrivacyDialog = false },
                        title = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "حریم خصوصی",
                                style = bodyMediumCard,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Right,
                            )
                        },
                        text = {
                            Text(
                                text = "ما در این برنامه احترام ویژه\u200Cای برای اطلاعات شخصی شما قائل هستیم. برای ورود و ثبت\u200Cنام، از حساب کاربری بازار شما استفاده می\u200Cشود و تمامی اطلاعات مربوط به موجودی حساب، تراکنش\u200Cها و سفارشات در سرورهای امن بازار ذخیره و مدیریت می\u200Cگردد.\n" +
                                        "\n" +
                                        "اطمینان داشته باشید که اطلاعات شما تحت حفاظت کامل قرار دارد و به هیچ عنوان با اشخاص ثالث به اشتراک گذاشته نمی\u200Cشود. استفاده از داده\u200Cهای شما صرفاً در راستای ارائه بهتر خدمات داخل برنامه خواهد بود.",
                                style = bodySmallCard.copy(textDirection = TextDirection.Rtl),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Right,
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = { showPrivacyDialog = false }) {
                                Text(
                                    "متوجه شدم", style = bodySmallCard,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Right,
                                )
                            }
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoginAnimation()

                    val bazaarSignInLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult()
                    ) { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            viewModel.handleSignInResult(result.data)
                        }
                    }

                    BazaarButton {
                        if (!viewModel.hasLogin.value) {
                            val signInOption =
                                BazaarSignInOptions.Builder(SignInOption.DEFAULT_SIGN_IN).build()
                            val client = BazaarSignIn.getClient(context, signInOption)
                            val intent = client.getSignInIntent()
                            bazaarSignInLauncher.launch(intent)
                        } else {
                            Toast.makeText(context, "شما قبلاً وارد شده‌اید", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    LoginWithAccounts()

                    TextButton(onClick = { showPrivacyDialog = true }) {
                        Text(
                            text = "قوانین و حریم خصوصی",
                            style = bodySmallCard,
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    iconColor: Color,
    icon: Int,
    text: String,
    textColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        onClick = { onClick.invoke() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = text, style = bodyMediumCard.copy(color = textColor))
        }
    }
}

@Composable
fun ProfileListItem(
    text: String,
    icon: Int,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .padding(vertical = 8.dp, horizontal = 14.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp,
        onClick = { onClick.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = 8.dp))

            Text(
                text = text,
                style = bodySmallCard,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Right
            )
        }
    }
}

@Composable
fun BazaarButton(signInClicked: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(top = 8.dp, bottom = 16.dp)
            .height(56.dp),
        onClick = { signInClicked.invoke() },
        shape = RoundedCornerShape(36.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(painterResource(R.drawable.ic_bazaar), null, modifier = Modifier.size(34.dp))
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text(
            text = "ورود با بازار",
            style = bodyMediumCard,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun LoginAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.signup_animation)
    )
    LottieAnimation(
        modifier = Modifier
            .size(270.dp)
            .padding(top = 16.dp, bottom = 36.dp),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}

@Composable
fun LoginWithAccounts() {
    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(bottom = 50.dp, top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .padding(end = 8.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Text(
                text = "ورود با",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = bodySmallCard
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(start = 8.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))

        IconButton(onClick = {
            Toast.makeText(context, "به‌زودی!", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_google),
                contentDescription = null
            )
        }
    }
}