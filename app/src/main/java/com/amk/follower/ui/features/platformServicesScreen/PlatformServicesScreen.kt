package com.amk.follower.ui.features.platformServicesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amk.follower.R
import com.amk.follower.model.data.ServiceItemsResponse
import com.amk.follower.ui.features.orderScreen.ErrorSection
import com.amk.follower.ui.features.orderScreen.NoInternetSection
import com.amk.follower.ui.theme.bodyMediumCard
import com.amk.follower.ui.theme.bodySmallCard
import com.amk.follower.util.*
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Composable
fun PlatformServicesScreen(platformId: String) {
    val viewModel = getNavViewModel<PlatformServicesViewModel>()
    val services = viewModel.servicesList.value
    val isLoading = viewModel.isLoading.value
    val isError = viewModel.isError.value
    var selectedCategory by remember { mutableStateOf("همه") }
    val context = LocalContext.current

    LaunchedEffect(key1 = platformId) {
        viewModel.getPlatformServices(platformId)
    }

    val categories = services.map { it.category }.distinct()
    val finalFilteredServices = if (selectedCategory == "همه") {
        services
    } else {
        services.filter { it.category == selectedCategory }
    }

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
            NoInternetSection { viewModel.getPlatformServices(platformId) }
        }
        if (categories.isNotEmpty() && !isLoading) {
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
                ErrorSection { viewModel.getPlatformServices(platformId) }
            }
            services.isEmpty() && !isLoading -> {
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
                val isSelected = selectedCategory == "همه"
                FilterChip(
                    modifier = Modifier.height(34.dp),
                    selected = isSelected,
                    onClick = { onCategorySelected("همه") },
                    shape = CircleShape,
                    label = {
                        Text(
                            text = "همه",
                            style = bodySmallCard,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        containerColor = Color.Transparent
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = MaterialTheme.colorScheme.outline,
                        selectedBorderColor = Color.Transparent
                    )
                )
            }
            items(categories) { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    modifier = Modifier.height(34.dp),
                    selected = isSelected,
                    onClick = { onCategorySelected(category) },
                    shape = CircleShape,
                    label = {
                        Text(
                            text = category,
                            style = bodySmallCard,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        containerColor = Color.Transparent
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = MaterialTheme.colorScheme.outline,
                        selectedBorderColor = Color.Transparent
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
            Text(
                "سرویسی در این دسته‌بندی یافت نشد.",
                style = bodyMediumCard,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
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

    val serviceRateWithProfit = (response.rate * PROFIT_PERCENT).toInt()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp,
        onClick = { navigation.navigate(MyScreens.DetailScreen.createRoute(response.service)) }
    ) {
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
                Text(
                    text = response.name,
                    style = bodyMediumCard.copy(textDirection = TextDirection.Rtl),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "هر هزار تا",
                    style = bodySmallCard,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold, fontSize = 18.sp
                            )
                        ) {
                            append(
                                serviceRateWithProfit.formatBalanceWithCommas().toPersianDigits()
                            )
                        }
                        withStyle(style = SpanStyle()) {
                            append(" تومان")
                        }
                    },
                    style = bodySmallCard.copy(textDirection = TextDirection.Rtl),
                    color = MaterialTheme.colorScheme.primary
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
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(76.dp)
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Text(
            text = "سرویسی یافت نشد",
            style = bodyMediumCard,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            text = "ممکنه اختلال لحظه‌ای باشه، یکم دیگه دوباره تلاش کن",
            style = bodyMediumCard,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}