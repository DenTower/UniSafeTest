package org.example.project.di

import org.example.project.data.ApiService
import org.example.project.data.ShoppingRepositoryImpl
import org.example.project.data.createHttpClient
import org.example.project.domain.ShoppingRepository
import org.example.project.domain.usecase.AddToShopListUseCase
import org.example.project.domain.usecase.CreateShopListUseCase
import org.example.project.domain.usecase.CrossItemOffUseCase
import org.example.project.domain.usecase.GetAllShopListsUseCase
import org.example.project.domain.usecase.GetShopListUseCase
import org.example.project.domain.usecase.LoginUseCase
import org.example.project.domain.usecase.MoveItemUseCase
import org.example.project.domain.usecase.RegisterUseCase
import org.example.project.domain.usecase.RemoveItemFromShopListUseCase
import org.example.project.domain.usecase.RemoveShopListUseCase
import org.example.project.domain.usecase.UpdateItemShopListUseCase
import org.example.project.presentation.viewmodel.ShoppingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {

    single { createHttpClient() }

    single { ApiService(get()) }

    single<ShoppingRepository> {
        ShoppingRepositoryImpl(get())
    }
}

val domainModule = module {
    factory { RegisterUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { GetAllShopListsUseCase(get()) }
    factory { GetShopListUseCase(get()) }
    factory { CreateShopListUseCase(get()) }
    factory { RemoveShopListUseCase(get()) }
    factory { AddToShopListUseCase(get()) }
    factory { UpdateItemShopListUseCase(get()) }
    factory { RemoveItemFromShopListUseCase(get()) }
    factory { CrossItemOffUseCase(get()) }
    factory { MoveItemUseCase(get()) }
}

val viewModelModule = module {

    viewModel {
        ShoppingViewModel(
            registerUseCase = get(),
            loginUseCase = get(),
            getAllShopListsUseCase = get(),
            getShopListUseCase = get(),
            createShopListUseCase = get(),
            removeShopListUseCase = get(),
            addToShopListUseCase = get(),
            updateItemShopListUseCase = get(),
            removeItemFromShopListUseCase = get(),
            crossItemOffUseCase = get(),
            moveItemUseCase = get()
        )
    }
}