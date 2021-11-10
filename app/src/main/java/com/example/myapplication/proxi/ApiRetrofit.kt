package com.example.myapplication.proxi

import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*

interface ApiRetrofit {

    @GET
    fun getAllPost(@Url url:String): Observable<Response<String>>

}