package com.soulware.tcompro.features.finances.di

import com.soulware.tcompro.features.finances.data.repository.FinanceRepositoryImpl
import com.soulware.tcompro.features.finances.domain.repository.FinanceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface FinanceRepositoryModule {

    @Binds
    fun bindFinanceRepository(impl: FinanceRepositoryImpl): FinanceRepository
}
