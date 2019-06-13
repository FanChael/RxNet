package com.hl.rxnettest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    // 无需添加解析器- 返回原始ResponseBody
    @GET("users/{user}/repos")
    fun listReposResponseBody(@Path("user") user: String): Call<ResponseBody>

    // 添加ToStringConverterFactory解析器 - 返回字符串结果【本质：ResponseBody value -> value.string()】
    @GET("users/{user}/repos")
    fun listReposString(@Path("user") user: String): Call<String>

    // 添加GsonConverterFactory解析器 - 返回Json解析的对象列表
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Call<List<Repo>>
}