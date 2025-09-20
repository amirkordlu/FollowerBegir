package com.amk.follower.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

val navigationBarItems = listOf(
    BottomNavigationItem(
        "سفارشات",
        Icons.Filled.ShoppingCart,
        Icons.Outlined.ShoppingCart,
        true
    ),
    BottomNavigationItem(
        "خانه",
        Icons.Filled.Home,
        Icons.Outlined.Home,
        false
    ),
    BottomNavigationItem(
        "پروفایل",
        Icons.Filled.Person,
        Icons.Outlined.Person,
        false
    ),
)