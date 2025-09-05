package com.amk.rubikafollower.di

import com.amk.rubikafollower.model.net.createApiService
import com.amk.rubikafollower.model.repository.addOrderRepostiory.AddOrderServiceRepository
import com.amk.rubikafollower.model.repository.addOrderRepostiory.AddOrderServiceRepositoryImpl
import com.amk.rubikafollower.model.repository.orderStatusRepository.OrderStatusRepository
import com.amk.rubikafollower.model.repository.orderStatusRepository.OrderStatusRepositoryImpl
import com.amk.rubikafollower.model.repository.serviceItemsRepository.ServiceItemsRepository
import com.amk.rubikafollower.model.repository.serviceItemsRepository.ServiceItemsRepositoryImpl
import com.amk.rubikafollower.ui.features.detailScreen.DetailScreenViewModel
import com.amk.rubikafollower.ui.features.homeScreen.HomeScreenViewModel
import com.amk.rubikafollower.ui.features.orderScreen.OrderScreenViewModel
import com.amk.rubikafollower.ui.features.profileScreen.AccountViewModel
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