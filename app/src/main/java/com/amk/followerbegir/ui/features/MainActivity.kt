package com.amk.followerbegir.ui.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amk.followerbegir.di.myModules
import com.amk.followerbegir.ui.features.mainScreen.MainScreen
import com.amk.followerbegir.ui.theme.FollowerBegirTheme
import com.amk.followerbegir.util.MyScreens
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.navigation.KoinNavHost
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Koin(appDeclaration = {
                androidContext(this@MainActivity)
                modules(myModules)
            }) {
                FollowerBegirTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        FollowerBegirUI()
                    }
                }
            }
        }
    }
}

@Composable
fun FollowerBegirUI() {
    val navController = rememberNavController()
    KoinNavHost(
        navController = navController, startDestination = MyScreens.MainScreen.route
    ) {

        composable(MyScreens.MainScreen.route) {
            MainScreen()
        }

        composable(MyScreens.AccountScreen.route) {
            MainScreen()
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FollowerBegirTheme {
        FollowerBegirUI()
    }
}