package com.amk.followerbegir.util

sealed class MyScreens(val route: String) {

    object MainScreen : MyScreens("mainScreen")
    object ProfileScreen : MyScreens("profileScreen")
    object OrderScreen : MyScreens("orderScreen")
    object LoginScreen : MyScreens("loginScreen")
    object ShopScreen : MyScreens("shopScreen")
    object DetailScreen : MyScreens("detail/{serviceId}") {
        fun createRoute(serviceId: String) = "detail/$serviceId"
    }
}
