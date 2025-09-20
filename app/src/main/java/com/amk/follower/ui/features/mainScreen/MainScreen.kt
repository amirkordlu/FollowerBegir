package com.amk.follower.ui.features.mainScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import com.amk.follower.ui.features.aboutScreen.AboutScreen
import com.amk.follower.ui.features.detailScreen.DetailScreen
import com.amk.follower.ui.features.faqScreen.FaqScreen
import com.amk.follower.ui.features.homeScreen.HomeScreen
import com.amk.follower.ui.features.orderScreen.OrderScreen
import com.amk.follower.ui.features.profileScreen.ProfileScreen
import com.amk.follower.ui.features.profileScreen.ShopScreen
import com.amk.follower.ui.features.supportScreen.SupportScreen
import com.amk.follower.ui.theme.FollowerBegirTheme
import com.amk.follower.ui.theme.navigationBarTextStyle
import com.amk.follower.util.MyScreens
import com.amk.follower.util.navigationBarItems
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
                            Text(
                                text = item.title,
                                style = navigationBarTextStyle,
                                color = MaterialTheme.colorScheme.onSurface
                            )
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
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
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
            composable(MyScreens.FaqScreen.route) {
                FaqScreen()
            }
            composable(MyScreens.SupportScreen.route) {
                SupportScreen()
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

