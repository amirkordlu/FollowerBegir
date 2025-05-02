package com.amk.followerbegir.util

sealed class MyScreens(val route: String) {

    object MainScreen : MyScreens("mainScreen")
    object ProfileScreen : MyScreens("profileScreen")
    object OrderScreen : MyScreens("orderScreen")

}
