package com.amk.followerbegir.ui.features.mainScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amk.followerbegir.ui.features.aboutScreen.AboutScreen
import com.amk.followerbegir.ui.features.detailScreen.DetailScreen
import com.amk.followerbegir.ui.features.homeScreen.HomeScreen
import com.amk.followerbegir.ui.features.orderScreen.OrderScreen
import com.amk.followerbegir.ui.features.profileScreen.ProfileScreen
import com.amk.followerbegir.ui.features.profileScreen.ShopScreen
import com.amk.followerbegir.ui.theme.FollowerBegirTheme
import com.amk.followerbegir.ui.theme.navigationBarTextStyle
import com.amk.followerbegir.util.MyScreens
import com.amk.followerbegir.util.navigationBarItems
import dev.burnoo.cokoin.navigation.KoinNavHost

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FollowerBegirTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val navigation = rememberNavController()

    var selectedItemIndex by rememberSaveable { mutableStateOf(1) }

    val items = navigationBarItems
    val screens = listOf(
        MyScreens.OrderScreen,
        MyScreens.MainScreen,
        MyScreens.ProfileScreen
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            val screen = screens[index]
                            navigation.navigate(screen.route) {
                                popUpTo(navigation.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = {
                            Text(text = item.title, style = navigationBarTextStyle)
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.badgeCount != null) {
                                        Badge { Text(item.badgeCount.toString()) }
                                    } else if (item.hasNews) {
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (selectedItemIndex == index)
                                        item.selectedIcon
                                    else item.unSelectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        KoinNavHost(
            navController = navigation,
            startDestination = MyScreens.MainScreen.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(MyScreens.MainScreen.route) {
                HomeScreen()
            }
            composable(MyScreens.OrderScreen.route) {
                OrderScreen()
            }
            composable(MyScreens.ProfileScreen.route) {
                ProfileScreen()
            }
            composable(MyScreens.ShopScreen.route) {
                ShopScreen()
            }
            composable(MyScreens.AboutScreen.route) {
                AboutScreen()
            }
            composable(
                route = MyScreens.DetailScreen.route,
                arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
            ) { backStackEntry ->
                val serviceId = backStackEntry.arguments?.getString("serviceId")
                DetailScreen(serviceId)
            }
        }
    }
}

