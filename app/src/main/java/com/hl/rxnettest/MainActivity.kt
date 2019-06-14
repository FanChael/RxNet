package com.hl.rxnettest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 2019.06.13 - 2019.06.xx - 主要是本地实践使用Rx，暂时未开始封装到库
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()

        // Retrofit使用学习
        // 创建Retrofit请求对象
        var retrofit: Retrofit = Retrofit.Builder()
                // 设置数据解析器`
                // 无需解析 -> 方式0
                // Json解析 -> 方式1
                // .addConverterFactory(GsonConverterFactory.create())
                // Json解析 -> 方式1-自定义解析
                // .addConverterFactory(MGsonConverterFactory(Gson()))
                // 字符串解析 -> 方式2
                .addConverterFactory(ToStringConverterFactory())
                // AdapterFactory????
                //.addCallAdapterFactory()
                // 设置网络请求地址
                .baseUrl("https://api.github.com/")
                .build()
        // 创建GitHubService请求服务
        // person::class 和Person::class都是获取kotlin的KClass -> 先获取到kotlin的KClass然后再获取javaClass
        // 说明： https://blog.csdn.net/fly7632785/article/details/79863049
        var gitHubService = retrofit.create(GitHubService::class.java)

        // UI线程中同步执行错误提示 - Caused by: android.os.NetworkOnMainThreadException
        // var repos = gitHubService.listRepos("FanChael")
        // var response = repos.execute()

        /*
        // 方式0： 创建网络请求接口实例 - 返回ResponseBody结果,response.body()?.string()自己进行解析
        var reposResponseBody = gitHubService.listReposResponseBody("FanChael")
        reposResponseBody.enqueue(object : Callback<ResponseBody>{ // object的作用是调用内部匿名类
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                // Returns true if the code is in [200..300)
                if (response.isSuccessful) {
                    // response.body() -> ResponseBody
                    Log.e("reposResponseBody-s", response.body()?.string())
                } else {
                    Log.e("reposResponseBody-s-f", "请求错误了！")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable){
                Log.e("reposResponseBody-f", t.message)
            }
        })*/

        // 方式1： 创建网络请求接口实例 - 返回字符串结果
        // var reposString = gitHubService.listReposString("FanChael")
        var reposString = gitHubService.listReposString("FanChael", "s-requestBodyConverter")
        reposString.enqueue(object : Callback<String>{ // object的作用是调用内部匿名类
            override fun onResponse(call: Call<String>, response: Response<String>){
                if (response.isSuccessful) {
                    // response.body() -> String
                    // Log.e("reposString-onResponse", response.body())
                } else {
                    Log.e("reposString-onResponse", "请求错误了！")
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable){
                Log.e("reposString-onFailure", t.message)
            }
        })

        /*
        // 方式2： 创建网络请求接口实例 - Gson解析后返回对象列表
        var repos = gitHubService.listRepos("FanChael")
        // 发起请求，UI线程需要异步请求
        repos.enqueue(object : Callback<List<Repo>>{ // object的作用是调用内部匿名类
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>){
                if (response.isSuccessful) {
                    // response.body() -> List<Repo>
                    Log.e("repos-onResponse", response.body()?.get(0)?.name)
                } else {
                    Log.e("repos-onResponse", "请求错误了！")
                }
            }
            override fun onFailure(call: Call<List<Repo>>, t: Throwable){
                Log.e("repos-onFailure", t.message)
            }
        })*/
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
