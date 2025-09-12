package com.amk.rubikafollower.util

sealed class MyScreens(val route: String) {

    object MainScreen : MyScreens("mainScreen")
    object ProfileScreen : MyScreens("profileScreen")
    object OrderScreen : MyScreens("orderScreen")
    object ShopScreen : MyScreens("shopScreen")
    object AboutScreen : MyScreens("aboutScreen")
    object FaqScreen : MyScreens("faqScreen")
    object DetailScreen : MyScreens("detail/{serviceId}") {
        fun createRoute(serviceId: String) = "detail/$serviceId"
    }
}
