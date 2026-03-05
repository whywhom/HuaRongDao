package com.huarongdao.di

import org.koin.core.module.Module

/** Each platform provides its own database + datastore Koin bindings. */
expect fun createPlatformModule(): Module
