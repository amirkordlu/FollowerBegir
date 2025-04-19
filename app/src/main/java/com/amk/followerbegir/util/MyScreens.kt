package com.amk.followerbegir.util

sealed class MyScreens(val route: String) {

    object MainScreen : MyScreens("mainScreen")
    object AccountScreen : MyScreens("accountScreen")

}
