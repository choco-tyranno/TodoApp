package com.choco_tyranno.todoapp.di.modules

import com.choco_tyranno.todoapp.data.source.MyLoginDataSource
import com.choco_tyranno.todoapp.data.source.MyLoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {

    @Provides
    fun provideLoginRepository(): MyLoginRepository = MyLoginRepository.instance

    @Provides
    fun provideLoginDataSource(): MyLoginDataSource = MyLoginDataSource.instance
}