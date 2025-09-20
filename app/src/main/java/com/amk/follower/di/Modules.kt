package com.amk.follower.di

import com.amk.follower.model.net.createApiService
import com.amk.follower.model.repository.addOrderRepostiory.AddOrderServiceRepository
import com.amk.follower.model.repository.addOrderRepostiory.AddOrderServiceRepositoryImpl
import com.amk.follower.model.repository.orderStatusRepository.OrderStatusRepository
import com.amk.follower.model.repository.orderStatusRepository.OrderStatusRepositoryImpl
import com.amk.follower.model.repository.serviceItemsRepository.ServiceItemsRepository
import com.amk.follower.model.repository.serviceItemsRepository.ServiceItemsRepositoryImpl
import com.amk.follower.ui.features.detailScreen.DetailScreenViewModel
import com.amk.follower.ui.features.homeScreen.HomeScreenViewModel
import com.amk.follower.ui.features.orderScreen.OrderScreenViewModel
import com.amk.follower.ui.features.profileScreen.AccountViewModel
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