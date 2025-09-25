package com.amk.sakoo.di

import com.amk.sakoo.model.net.createApiService
import com.amk.sakoo.model.repository.addOrderRepostiory.AddOrderServiceRepository
import com.amk.sakoo.model.repository.addOrderRepostiory.AddOrderServiceRepositoryImpl
import com.amk.sakoo.model.repository.orderStatusRepository.OrderStatusRepository
import com.amk.sakoo.model.repository.orderStatusRepository.OrderStatusRepositoryImpl
import com.amk.sakoo.model.repository.serviceItemsRepository.ServiceItemsRepository
import com.amk.sakoo.model.repository.serviceItemsRepository.ServiceItemsRepositoryImpl
import com.amk.sakoo.ui.features.detailScreen.DetailScreenViewModel
import com.amk.sakoo.ui.features.homeScreen.HomeScreenViewModel
import com.amk.sakoo.ui.features.orderScreen.OrderScreenViewModel
import com.amk.sakoo.ui.features.platformServicesScreen.PlatformServicesViewModel
import com.amk.sakoo.ui.features.profileScreen.AccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {

    single { createApiService() }

    single<ServiceItemsRepository> { ServiceItemsRepositoryImpl(get()) }

    single<AddOrderServiceRepository> { AddOrderServiceRepositoryImpl(get()) }

    single<OrderStatusRepository> { OrderStatusRepositoryImpl(get()) }

    viewModel {
        PlatformServicesViewModel(
            servicesRepository = get()
        )
    }

    viewModel { HomeScreenViewModel(allServiceItems = get()) }

    viewModel { DetailScreenViewModel(get(), get()) }

    viewModel { OrderScreenViewModel(get()) }

    viewModel { AccountViewModel() }
}