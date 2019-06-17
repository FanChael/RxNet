package com.hl.rxnettest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GitHubService {
    // 无需添加解析器- 返回原始ResponseBody
    @GET("users/{user}/repos")
    fun listReposResponseBody(@Path("user") user: String): Call<ResponseBody>

    // 添加ToStringConverterFactory解析器 - 返回字符串结果【本质：ResponseBody value -> value.string()】
    @GET("users/{user}/repos")
    fun listReposString(@Path("user") user: String): Call<String>

    // 添加ToStringConverterFactory解析器 - 返回字符串结果【本质：ResponseBody value -> value.string()】
    // 额外添加一个Body参数，触发requestBodyConverter回调; @GET/@DELTE这些不支持Body
    // @Body不能与@FormUrlEncoded共用，否则报错；而@Field偏偏是提交表单使用的，需要@FormUrlEncoded，So得出@Body和@Field也是不能同时使用的
    @POST("users/{user}/repos")
    fun listReposString(@Path("user") user: String, @Body data: String): Call<String>

    @POST("users/{user}/repos")
    fun listReposStringCustomCall(@Path("user") user: String, @Body data: String): CustomCall<String>

    // 添加GsonConverterFactory解析器 - 返回Json解析的对象列表
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Call<List<Repo>>
}