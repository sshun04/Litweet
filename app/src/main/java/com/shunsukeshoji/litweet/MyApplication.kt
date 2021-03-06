package com.shunsukeshoji.litweet

import android.app.Application
import com.google.gson.GsonBuilder
import com.shunsukeshoji.litweet.data.DropBoxService
import com.shunsukeshoji.litweet.data.impl.LocalCacheRepository
import com.shunsukeshoji.litweet.data.impl.RetrofitClientRepository
import com.shunsukeshoji.litweet.data.room.LocalDatabase
import com.shunsukeshoji.litweet.domain.use_case.MainActivityUseCase
import com.shunsukeshoji.litweet.presentation.main.MainActivityViewModel
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {

    private val apiModule = module {
        single<Retrofit> {
            val gson = GsonBuilder()
                .serializeNulls()
                .create()

            return@single Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl("https://dl.dropboxusercontent.com/")
                .build()
        }

        single {
            provideDropBoxService(get())
        }
    }

    private val localDbModule = module {
        single<LocalDatabase> {
            return@single LocalDatabase.getInstance(this@MyApplication)
        }
        factory { get<LocalDatabase>().roomDao() }

    }

    private val viewModelModule = module {
        viewModel { MainActivityViewModel() }
    }

    private val useCaseModule = module {
        single<RetrofitClientRepository> { RetrofitClientRepository(get()) }
        single { LocalCacheRepository(get()) }
        factory {
            MainActivityUseCase(
                retrofitClientRepository = get(),
                localCacheRepository = get()
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(
                listOf(
                    viewModelModule, useCaseModule, apiModule, localDbModule
                )
            )
        }
    }

    private fun provideDropBoxService(retrofit: Retrofit): DropBoxService {
        return retrofit.create(DropBoxService::class.java)
    }

}