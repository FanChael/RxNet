package com.hl.rxnettest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 布局都不依赖了
        // setContentView(R.layout.activity_main2)
        // setContentView(View(this))

        // 1. 创建Retroft实例对象
        var retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build();

        // 2. 创建GitHub请求服务
        var gitHubService = retrofit.create(GitHubService::class.java);

        // 3. 获取服务对应的某个请求实例
        var gitCall  = gitHubService.listRepos("FanChael");

        // 4.1 UI线程不能进行同步请求,ANR
        // var executeRst = gitCall.execute();
        // 4.2. 进行异步请求
        gitCall.enqueue(object : Callback<List<Repo>>{  // object的作用是调用内部匿名类
            override fun onResponse(call: Call<List<Repo>>?, response: Response<List<Repo>>?) {
                // {protocol=http/1.1, code=200, message=OK, url=https://api.github.com/users/FanChael/repos}
                Log.e("Main2Activity", "" + response)
                Log.e("Main2Activity", "" + (response?.body() ?: ""))
                var repoList = response?.body();
                for (item in repoList!!) {
                    // SuperStartElectronic subtitle display - 电子字幕展示-接机、演唱会、见面、展示专用.https://github.com/FanChael/SuperStart
                    Log.e("Main2Activity", item.name + item.description + item.html_url)
                }
            }
            override fun onFailure(call: Call<List<Repo>>?, t: Throwable?) {
            }
        })
        // 4.3 获取okhttp3.Request原始请求对象实例 - 用原始请求对象进行请求
        var request = gitCall.request()
        // --然后创建一个Call对象，用于网络请求
        var okHttpCall = OkHttpClient().newCall(request)
        // ---然后进行异步请求
        okHttpCall.enqueue(object: okhttp3.Callback{
            override fun onFailure(call: okhttp3.Call?, e: IOException?) {
            }

            override fun onResponse(call: okhttp3.Call?, response: okhttp3.Response?) {
                // [{"id":140240588,"node_id":"MDEwOlJlcG9zaXRvcnkxNDAyNDA1ODg=",....]
                Log.e("Main2Activity", "okhttp3: " + (response?.body()?.string() ?: ""))
            }

        }) // 我们采用Okhttp的方式进行请求调用，与Retrofit如出一辙，Retrofit进行了包装，简化了请求定义！

    }
}
