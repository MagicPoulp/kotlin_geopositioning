package com.example.geopositioning.http


import com.example.geopositioning.BuildConfig
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper


// https://www.geeksforgeeks.org/retrofit-with-kotlin-coroutine-in-android/
// https://github.com/AsyncHttpClient/async-http-client/tree/master/extras/retrofit2
object RetrofitHelper {

    fun getInstance(baseUrl: String): Retrofit {

        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 10

        val builder = OkHttpClient().newBuilder()
        builder.dispatcher(dispatcher)
        builder.readTimeout(10, TimeUnit.SECONDS)
        builder.connectTimeout(10, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BASIC
            builder.addInterceptor(interceptor)
        }

        /*
        // to add a header
        builder.addInterceptor { chain: Interceptor.Chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader(requestHeader.first, requestHeader.second)
                .build()
            chain.proceed(request)
        }*/

        val client = builder.build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
            .build()
    }
}
