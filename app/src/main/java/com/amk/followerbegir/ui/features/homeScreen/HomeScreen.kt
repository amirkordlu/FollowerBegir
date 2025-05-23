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
import com.amk.followerbegir.util.MyScreens
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
    viewModel.getAllItemsService()

    val allowedServices = listOf("1652", "2051", "2052", "2053", "2072", "2086")
    val filteredServices = viewModel.servicesList.value.filter { it.service in allowedServices }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("HomeScreen")
        ServicesList(filteredServices)
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
    var showDialog by remember { mutableStateOf(false) }
    val navigation = getNavController()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(236.dp)
            .padding(top = 12.dp, start = 32.dp, end = 32.dp, bottom = 22.dp),
        shape = RoundedCornerShape(24.dp),
        color = DarkGray,
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.70f)),
        onClick = { showDialog = true }
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.End) {
            Text(text = response.name, style = Typography.bodyMedium, color = White)

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(text = "category: " + response.category, style = Typography.bodySmall, color = White)

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(text = response.rate.toString() + "  تومان", style = Typography.bodySmall, color = White)

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(response.service, style = Typography.bodyMedium, color = White)
        }
//
//        if (showDialog) {
//            AlertDialog(
//                onDismissRequest = { showDialog = false },
//                onConfirmation = { },
//                dialogTitle = appendTextDialog(convertHTMLToText(response.name)),
//                dialogText = convertHTMLToText(response.desc),
//                icon = Icons.Default.Info
//            )
//        }

    }
}
