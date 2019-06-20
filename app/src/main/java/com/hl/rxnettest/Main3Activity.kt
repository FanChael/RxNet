package com.hl.rxnettest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Main3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        // 老样子，再来过一遍Retrofit请求流程

        // 1. 创建Retrofit实例
        var retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        // 2. 创建一个请求服务实例
        var gitHubService = retrofit.create(GitHubService::class.java)

        // 3. 获取服务对应的请求实例
        var gitCall = gitHubService.listReposStringRxJavaObservable("FanChael")

        // 4.1 进行基本请求 - 指定上下游所在线程
        var dis = gitCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t ->
                    //lamba表達式走起
                    for (item in t!!) {
                        // SuperStartElectronic subtitle display - 电子字幕展示-接机、演唱会、见面、展示专用.https://github.com/FanChael/SuperStart
                        // Log.e("observable", item.name + item.description + item.html_url)
                    }
                }

        // 4.2 有时候，我们需要连续做两次请求，比如提交订单成功后，然后调起支付接口(至于后续支付结果干什么，暂时不管！)
        // 如果我们不采用map，flatmap等操作符来做连续请求的话，按照小萌新之前的写法就是在第一次onNext成功回调里面，再次发起一次subscribe请求，同样的请求代码还会再写一次！
        // 而RxJava已经考虑到了这一点，为我们准备了更方便的操作符。这里就拿flatmap为例？
        // --flatmp需要返回的是一个ObservableSource，这时候就返回上游的observable对象
        // let'start
        gitCall.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io()) // 由于请求结果还需要拿来再做一次网络请求，因此我们再次放到io下线程中处理
                .flatMap(object: Function<List<Repo>, ObservableSource<List<Repo>>>{
                    override fun apply(t: List<Repo>): ObservableSource<List<Repo>> {
                        // 在进行一次请求 - 小萌新就将就重复调用了
                        // TODO t可以拿来作为再次请求时的的参数
                        Log.e("observable apply1", t.get(0).name)
                        Log.e("observable", "flatMap thread'name=" + Thread.currentThread().name)
                        return gitHubService.listReposStringRxJavaObservable("FanChael")
                    }
                })
                .observeOn(Schedulers.io())  // 还可以指定一个io线程, 用map的形式进行预处理
                .map(object: Function<List<Repo>, List<Repo>>{
                    override fun apply(t: List<Repo>): List<Repo> {
                        Log.e("observable map", "flatMap thread'name=" + Thread.currentThread().name)
                        return t
                    }
                })
                .observeOn(Schedulers.io()) // 上一次请求的结果回调中，我们还是拿来在io线程中做第三次请求
                .flatMap(object: Function<List<Repo>, ObservableSource<List<Repo>>>{
                    override fun apply(t: List<Repo>): ObservableSource<List<Repo>> {
                        // 在进行一次请求 - 小萌新就将就重复调用了
                        // TODO t可以拿来作为再次请求时的的参数
                        Log.e("observable apply2", "ttt")
                        Log.e("observable", "flatMap thread'name=" + Thread.currentThread().name)
                        return gitHubService.listReposStringRxJavaObservable("FanChael")
                    }
                })
                .observeOn(Schedulers.io()) // 上一次请求的结果回调中，我们还是拿来在io线程中做预处理
                .flatMap(object: Function<List<Repo>, ObservableSource<List<Repo>>>{
                    override fun apply(t: List<Repo>): ObservableSource<List<Repo>> {
                        // 这里是io线程，可以做一些耗时操作，然后返给onNext的UI线程
                        Log.e("observable", "flatMap thread'name=" + Thread.currentThread().name)
                        // 原样的给返回去
                        return Observable.fromArray(t);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // 请求都完成后，切换到UI线程，将结果进行渲染
                .subscribe(object : Observer<List<Repo>>{
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: List<Repo>) {
                        Log.e("observable onNext", t[0].name)
                    }

                    override fun onError(e: Throwable) {
                    }
                })

    }
}
