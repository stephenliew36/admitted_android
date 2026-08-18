package com.realtouchapp.admitted.storage.datastore

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
    作者: RobertChou
    時間: 2023/2/5 下午 02:17
    信箱: didi31139@gmail.com
    說明:
*/
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providerUpdateTimeStore(@ApplicationContext appContext: Context):UpdateTimeStore{
        return UpdateTimeStore(appContext)
    }

    @Provides
    @Singleton
    fun providerAdmittedDataStore(@ApplicationContext appContext: Context):AdmittedDataStore{
        return AdmittedDataStore(appContext)
    }
}