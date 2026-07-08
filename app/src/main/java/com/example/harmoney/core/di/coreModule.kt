package com.example.harmoney.core.di

import com.example.harmoney.core.session.SessionStateHolder
import org.koin.dsl.module

val coreModule = module {
    single {
        SessionStateHolder()
    }
}
