package com.amk.followerbegir.ui.features.mainScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
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
import com.amk.followerbegir.ui.theme.FollowerBegirTheme
import com.amk.followerbegir.ui.theme.navigationBarTextStyle
import com.amk.followerbegir.util.navigationBarItems

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

@SuppressLint("AutoboxingStateCreation", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(1)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navigationBarItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            // navigation.navigate
                        },
                        label = {
                            Text(text = item.title, style = navigationBarTextStyle)
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.badgeCount != null) {
                                        Badge {
                                            Text(text = item.badgeCount.toString())
                                        }
                                    } else if (item.hasNews) {
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unSelectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) {

    }

}

