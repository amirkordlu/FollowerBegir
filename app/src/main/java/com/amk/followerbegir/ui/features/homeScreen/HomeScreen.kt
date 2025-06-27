package com.amk.followerbegir.ui.features.homeScreen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amk.followerbegir.model.data.ServiceItemsResponse
import com.amk.followerbegir.ui.theme.FollowerBegirTheme
import com.amk.followerbegir.ui.theme.Typography
import com.amk.followerbegir.ui.theme.textFieldStyle
import com.amk.followerbegir.util.MyScreens
import com.amk.followerbegir.util.allowedServices
import com.amk.followerbegir.util.appendTextDialog
import com.amk.followerbegir.util.convertHTMLToText
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FollowerBegirTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeScreen()
        }
    }
}

@Composable
fun HomeScreen() {
    val viewModel = getNavViewModel<HomeScreenViewModel>()
    val services = viewModel.servicesList.value
    val isLoading = viewModel.isLoading.value
    val isError = viewModel.isError.value

    LaunchedEffect(key1 = Unit) {
        if (services.isEmpty()) {
            viewModel.getAllItemsService()
        }
    }

    val filteredServices = services.filter { it.service in allowedServices }

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        isError -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("خطا در دریافت اطلاعات از سرور", style = textFieldStyle)
            }
        }

        filteredServices.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("سرویسی یافت نشد")
            }
        }

        else -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ServicesList(filteredServices)
            }
        }
    }
}

@Composable
fun ServicesList(services: List<ServiceItemsResponse>) {
    LazyColumn(
        modifier = Modifier.padding(top = 2.dp, bottom = 10.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
    ) {
        items(services.size) {
            ItemCard(services[it])
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCard(response: ServiceItemsResponse) {
    val navigation = getNavController()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(236.dp)
            .padding(top = 12.dp, start = 32.dp, end = 32.dp, bottom = 22.dp),
        shape = RoundedCornerShape(24.dp),
        color = DarkGray,
        border = BorderStroke(0.5.dp, White.copy(alpha = 0.70f)),
        onClick = { navigation.navigate(MyScreens.DetailScreen.createRoute(response.service)) }
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.End) {
            Text(text = response.name, style = Typography.bodyMedium, color = White)

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = "category: " + response.category,
                style = Typography.bodySmall,
                color = White
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = response.rate.toString() + "  تومان",
                style = Typography.bodySmall,
                color = White
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(response.service, style = Typography.bodyMedium, color = White)
        }

    }
}
