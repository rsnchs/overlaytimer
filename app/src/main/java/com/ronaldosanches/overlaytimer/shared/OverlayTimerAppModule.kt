package com.ronaldosanches.overlaytimer.shared

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class OverlayTimerAppModule {

    @Provides
    fun providesAnalytics(@ApplicationContext context: Context) = FirebaseAnalytics.getInstance(context)
}