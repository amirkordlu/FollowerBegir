package com.amk.sakoo.ui.features.mainScreen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amk.sakoo.ui.features.aboutScreen.AboutScreen
import com.amk.sakoo.ui.features.detailScreen.DetailScreen
import com.amk.sakoo.ui.features.faqScreen.FaqScreen
import com.amk.sakoo.ui.features.homeScreen.HomeScreen
import com.amk.sakoo.ui.features.notificationScreen.NotificationScreen
import com.amk.sakoo.ui.features.orderScreen.OrderScreen
import com.amk.sakoo.ui.features.platformServicesScreen.PlatformServicesScreen
import com.amk.sakoo.ui.features.profileScreen.ProfileScreen
import com.amk.sakoo.ui.features.profileScreen.ShopScreen
import com.amk.sakoo.ui.features.supportScreen.SupportScreen
import com.amk.sakoo.ui.theme.FollowerBegirTheme
import com.amk.sakoo.ui.theme.navigationBarTextStyle
import com.amk.sakoo.util.MyScreens
import com.amk.sakoo.util.navigationBarItems
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
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val screensWithBottomBar = listOf(
        MyScreens.OrderScreen.route,
        MyScreens.MainScreen.route,
        MyScreens.ProfileScreen.route
    )

    val shouldShowBottomBar = currentDestination?.hierarchy?.any { dest ->
        screensWithBottomBar.contains(dest.route)
    } == true

    val items = navigationBarItems
    val screens = listOf(
        MyScreens.OrderScreen,
        MyScreens.MainScreen,
        MyScreens.ProfileScreen
    )

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        val screen = screens[index]
                        val isSelected =
                            currentDestination.hierarchy.any { it.route == screen.route }
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
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
                                        imageVector = if (isSelected)
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
        }
    ) { paddingValues ->
        KoinNavHost(
            navController = navController,
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
            composable(MyScreens.NotificationScreen.route) {
                NotificationScreen()
            }
            composable(
                route = MyScreens.PlatformServicesScreen.route,
                arguments = listOf(navArgument("platformId") { type = NavType.StringType })
            ) { backStackEntry ->
                val platformId = backStackEntry.arguments?.getString("platformId")
                requireNotNull(platformId) { "platformId parameter wasn't found." }
                PlatformServicesScreen(platformId = platformId)
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

