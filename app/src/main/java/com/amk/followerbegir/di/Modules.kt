package com.amk.followerbegir.di

import com.amk.followerbegir.model.net.createApiService
import com.amk.followerbegir.model.repository.ServiceItemsRepository
import com.amk.followerbegir.model.repository.ServiceItemsRepositoryImpl
import com.amk.followerbegir.ui.features.homeScreen.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {

    single { createApiService() }

    single<ServiceItemsRepository> { ServiceItemsRepositoryImpl(get()) }

    viewModel { HomeScreenViewModel(allServiceItems = get()) }

}