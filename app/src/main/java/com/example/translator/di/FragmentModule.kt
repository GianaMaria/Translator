package com.example.translator.di

import androidx.fragment.app.Fragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): Fragment
}