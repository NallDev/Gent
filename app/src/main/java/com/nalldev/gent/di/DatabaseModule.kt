package com.nalldev.gent.di

import android.app.Application
import androidx.room.Room
import com.nalldev.gent.data.datasources.local.EventDb
import com.nalldev.gent.data.datasources.local.EventDao
import org.koin.dsl.module

fun provideDatabase(application: Application): EventDb =
    Room.databaseBuilder(
        application,
        EventDb::class.java,
        "event_db"
    )
        .fallbackToDestructiveMigration()
        .build()

fun provideDao(eventDb: EventDb): EventDao = eventDb.getEventDao()


val databaseModule= module {
    single { provideDatabase(get()) }
    single { provideDao(get()) }
}