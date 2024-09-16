package com.v1.smartv1alculatorv1.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ModuleChat {
            val DOMAIN = "http://69.55.141.222:20019/v1/"

    val interceptor = Interceptor { chain ->
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("Authorization", "Bearer app-r5w8eiQLIoSsGnyfiZKIJvBi")
        chain.proceed(builder.build())
    }

    val okBuilder = OkHttpClient.Builder().addInterceptor(interceptor)
    val apiService: ChatService = Retrofit.Builder()
        .baseUrl(DOMAIN)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(okBuilder.build())
        .build()
        .create(ChatService::class.java)

}