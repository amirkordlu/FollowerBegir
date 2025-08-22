package com.amk.followerbegir.ui.features.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amk.followerbegir.R
import com.amk.followerbegir.model.data.ServiceItemsResponse
import com.amk.followerbegir.ui.features.orderScreen.ErrorSection
import com.amk.followerbegir.ui.features.orderScreen.NoInternetSection
import com.amk.followerbegir.ui.theme.FollowerBegirTheme
import com.amk.followerbegir.ui.theme.LightColorScheme
import com.amk.followerbegir.ui.theme.bodyMediumCard
import com.amk.followerbegir.ui.theme.bodySmallCard
import com.amk.followerbegir.util.MyScreens
import com.amk.followerbegir.util.NetworkChecker
import com.amk.followerbegir.util.allowedServices
import com.amk.followerbegir.util.formatBalanceWithCommas
import com.amk.followerbegir.util.iconColorPairs
import com.amk.followerbegir.util.toPersianDigits
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
    var selectedCategory by remember { mutableStateOf("همه") }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        if (services.isEmpty()) {
            viewModel.getAllItemsService()
        }
    }

    val baseFilteredServices = services.filter { it.service in allowedServices }
    val categories = baseFilteredServices.map { it.category }.distinct()
    val finalFilteredServices = if (selectedCategory == "همه") {
        baseFilteredServices
    } else {
        baseFilteredServices.filter { it.category == selectedCategory }
    }

    // Get random color for icons
    val shuffledColorPairs = remember(finalFilteredServices) {
        val colorPalette = iconColorPairs.shuffled()
        (0 until finalFilteredServices.size).map { index ->
            colorPalette[index % colorPalette.size]
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!NetworkChecker(context).isInternetConnected) {
            NoInternetSection { viewModel.getAllItemsService() }
        }
        if (categories.isNotEmpty() && !isLoading) {
//            Text(
//                text = "محصولات",
//                style = bodyLargeCard,
//                modifier = Modifier
//                    .align(Alignment.End)
//                    .padding(end = 16.dp)
//            )
            CategoryChips(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                })
        }
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            isError -> {
                ErrorSection { viewModel.getAllItemsService() }
            }

            baseFilteredServices.isEmpty() -> {
                EmptyServiceSection()
            }

            else -> {
                ServicesList(
                    services = finalFilteredServices, colors = shuffledColorPairs
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChips(
    categories: List<String>, selectedCategory: String, onCategorySelected: (String) -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    modifier = Modifier.height(34.dp),
                    selected = selectedCategory == "همه",
                    onClick = { onCategorySelected("همه") },
                    shape = CircleShape,
                    label = {
                        Text(
                            text = "همه",
                            style = bodySmallCard,
                            color = if (selectedCategory == "همه") White else Color.Black
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        containerColor = Color.Transparent
                    )
                )
            }
            items(categories) { category ->
                FilterChip(
                    modifier = Modifier.height(34.dp),
                    selected = selectedCategory == category,
                    onClick = { onCategorySelected(category) },
                    shape = CircleShape,
                    label = {
                        Text(
                            text = category,
                            style = bodySmallCard,
                            color = if (selectedCategory == category) White else Color.Black
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        containerColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
fun ServicesList(
    services: List<ServiceItemsResponse>, colors: List<Pair<Color, Color>>
) {
    if (services.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("سرویسی در این دسته‌بندی یافت نشد.", style = bodyMediumCard)
        }
    } else {
        LazyColumn(
            modifier = Modifier.padding(top = 10.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(services) { index, serviceItem ->
                ItemCard(serviceItem, colors[index])
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCard(
    response: ServiceItemsResponse, colors: Pair<Color, Color>
) {
    val navigation = getNavController()
    val (backgroundColor, iconColor) = colors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightColorScheme.background),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = { navigation.navigate(MyScreens.DetailScreen.createRoute(response.service)) }) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(52.dp)
                        .background(color = backgroundColor, shape = RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add_shopping_cart),
                        contentDescription = null,
                        tint = iconColor
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(text = response.name, style = bodyMediumCard, fontWeight = FontWeight.Bold)
                Text(text = "شروع قیمت از", style = bodySmallCard, color = DarkGray)
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold, fontSize = 18.sp
                            )
                        ) {
                            append(response.rate.formatBalanceWithCommas().toPersianDigits())
                        }
                        withStyle(style = SpanStyle()) {
                            append(" تومان")
                        }
                    },
                    style = bodySmallCard.copy(textDirection = TextDirection.Rtl),
                    color = Color(0xFF3D5AFE)
                )
            }
        }
    }
}

@Composable
fun EmptyServiceSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_error),
            null,
            tint = DarkGray,
            modifier = Modifier.size(76.dp)
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Text(
            text = "سرویسی یافت نشد",
            style = bodyMediumCard,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            text = "ممکنه اختلال لحظه‌ای باشه، یکم دیگه دوباره تلاش کن",
            style = bodyMediumCard,
            color = DarkGray,
            textAlign = TextAlign.Center
        )
    }
}