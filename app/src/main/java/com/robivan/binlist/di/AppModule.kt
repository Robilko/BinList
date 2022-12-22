package com.robivan.binlist.di

import androidx.room.Room
import com.robivan.binlist.data.api.ApiService
import com.robivan.binlist.data.cache.DetailsCardDataBase
import com.robivan.binlist.data.mapper.DetailsCardMapper
import com.robivan.binlist.data.mapper.IDetailsCardMapper
import com.robivan.binlist.data.repository.LocalRepositoryImpl
import com.robivan.binlist.data.repository.RemoteRepositoryImpl
import com.robivan.binlist.domain.repository.ILocalRepository
import com.robivan.binlist.domain.repository.IRemoteRepository
import com.robivan.binlist.ui.MainViewModel
import com.robivan.binlist.utils.DATABASE_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ApiService.getInstance() }
    single {
        Room.databaseBuilder(
            androidApplication(),
            DetailsCardDataBase::class.java,
            DATABASE_NAME
        ).build()
    }
    single { get<DetailsCardDataBase>().detailsCardDao() }
    single<IDetailsCardMapper> { DetailsCardMapper() }
    single<IRemoteRepository> { RemoteRepositoryImpl(apiService = get(), mapper = get()) }
    single<ILocalRepository> { LocalRepositoryImpl(cache = get(), mapper = get()) }
    viewModel { MainViewModel(get(), get()) }
}