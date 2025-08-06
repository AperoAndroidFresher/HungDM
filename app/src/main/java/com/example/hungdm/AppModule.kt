package com.example.hungdm

import androidx.room.Room
import com.example.hungdm.data.db.AppDatabase
import com.example.hungdm.data.db.dao.PlaylistDao
import com.example.hungdm.data.db.dao.UserDao
import org.koin.androidx.viewmodel.dsl.viewModel
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.repo.PlaylistRepository
import com.example.hungdm.repo.PlaylistRepositoryImpl
import com.example.hungdm.repo.UserRepository
import com.example.hungdm.repo.UserRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "appdb"
        ).fallbackToDestructiveMigration().build()
    }
    single<UserDao> { get<AppDatabase>().userDao() }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<PlaylistDao> { get<AppDatabase>().playlistDao() }
    single<PlaylistRepository> { PlaylistRepositoryImpl(get()) }
    viewModel { MviViewModel(get(), get()) }
}