package com.amk.followerbegir.ui.features.profileScreen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.amk.followerbegir.R
import com.amk.followerbegir.ui.theme.FollowerBegirTheme
import com.amk.followerbegir.ui.theme.Typography
import com.amk.followerbegir.util.MyScreens
import com.amk.followerbegir.util.NetworkChecker
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

    LaunchedEffect(Unit) {
        viewModel.getBazaarLogin(context, lifecycleOwner)
    }

    when {
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
            LaunchedEffect(Unit) {
                navigation.navigate(MyScreens.ShopScreen.route) {
                    popUpTo(MyScreens.ShopScreen.route) { inclusive = true }
                }
            }
        }

        else -> {
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
                        Toast.makeText(context, "شما قبلاً وارد شده‌اید", Toast.LENGTH_SHORT).show()
                    }
                }

                LoginWithAccounts()
            }
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
        colors = ButtonDefaults.buttonColors(Color(0xFF0EA960))
    ) {

        Icon(painterResource(R.drawable.ic_bazaar), null, modifier = Modifier.size(34.dp))

        Text(
            text = "ورود با بازار",
            style = Typography.bodyMedium,
            color = Color.White
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
            Divider(
                color = Color(151, 151, 151, 255),
                thickness = 0.5.dp,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .padding(end = 8.dp)
            )

            Text(
                text = "ورود با",
                color = Color(0xFF424242),
                style = Typography.bodySmall
            )

            Divider(
                color = Color(151, 151, 151),
                thickness = 0.5.dp,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.padding(4.dp))

        IconButton(onClick = { Toast.makeText(context, "بزودی!", Toast.LENGTH_SHORT).show() }) {
            Icon(painter = painterResource(R.drawable.ic_google), contentDescription = null)
        }

    }
}