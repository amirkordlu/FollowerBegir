package com.amk.sakoo.util

sealed class MyScreens(val route: String) {

    object MainScreen : MyScreens("mainScreen")
    object ProfileScreen : MyScreens("profileScreen")
    object OrderScreen : MyScreens("orderScreen")
    object ShopScreen : MyScreens("shopScreen")
    object AboutScreen : MyScreens("aboutScreen")
    object FaqScreen : MyScreens("faqScreen")
    object SupportScreen : MyScreens("supportScreen")
    object NotificationScreen : MyScreens("notificationScreen")
    object DetailScreen : MyScreens("detail/{serviceId}") {
        fun createRoute(serviceId: String) = "detail/$serviceId"
    }
    object PlatformServicesScreen : MyScreens("platform_services/{platformId}") {
        fun createRoute(platformId: String) = "platform_services/$platformId"
    }
}
