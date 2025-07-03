package com.amk.followerbegir.di

import com.amk.followerbegir.model.net.createApiService
import com.amk.followerbegir.model.repository.addOrderRepostiory.AddOrderServiceRepository
import com.amk.followerbegir.model.repository.addOrderRepostiory.AddOrderServiceRepositoryImpl
import com.amk.followerbegir.model.repository.orderStatusRepository.OrderStatusRepository
import com.amk.followerbegir.model.repository.orderStatusRepository.OrderStatusRepositoryImpl
import com.amk.followerbegir.model.repository.serviceItemsRepository.ServiceItemsRepository
import com.amk.followerbegir.model.repository.serviceItemsRepository.ServiceItemsRepositoryImpl
import com.amk.followerbegir.ui.features.detailScreen.DetailScreenViewModel
import com.amk.followerbegir.ui.features.homeScreen.HomeScreenViewModel
import com.amk.followerbegir.ui.features.orderScreen.OrderScreenViewModel
import com.amk.followerbegir.ui.features.profileScreen.AccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {

    single { createApiService() }

    single<ServiceItemsRepository> { ServiceItemsRepositoryImpl(get()) }

    single<AddOrderServiceRepository> { AddOrderServiceRepositoryImpl(get()) }

    single<OrderStatusRepository> { OrderStatusRepositoryImpl(get()) }

    viewModel { HomeScreenViewModel(allServiceItems = get()) }

    viewModel { DetailScreenViewModel(get(), get()) }

    viewModel { OrderScreenViewModel(get()) }

    viewModel { AccountViewModel() }
}