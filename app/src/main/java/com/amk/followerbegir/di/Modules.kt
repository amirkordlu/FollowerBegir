package com.amk.followerbegir.di

import com.amk.followerbegir.model.net.createApiService
import com.amk.followerbegir.model.repository.AddOrderServiceRepository
import com.amk.followerbegir.model.repository.AddOrderServiceRepositoryImpl
import com.amk.followerbegir.model.repository.ServiceItemsRepository
import com.amk.followerbegir.model.repository.ServiceItemsRepositoryImpl
import com.amk.followerbegir.ui.features.detailScreen.DetailScreenViewModel
import com.amk.followerbegir.ui.features.homeScreen.HomeScreenViewModel
import com.amk.followerbegir.ui.features.profileScreen.AccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {

    single { createApiService() }

    single<ServiceItemsRepository> { ServiceItemsRepositoryImpl(get()) }

    single<AddOrderServiceRepository> { AddOrderServiceRepositoryImpl(get()) }

    viewModel { HomeScreenViewModel(allServiceItems = get()) }

    viewModel { DetailScreenViewModel(get(), get()) }

    viewModel { AccountViewModel() }
}