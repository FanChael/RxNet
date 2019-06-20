package com.hl.rxnettest

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.IntegerRes
import android.util.Log
import android.widget.Toast
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main2.*
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import okhttp3.Request
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 布局都不依赖了 - 有时候可以打开用下控件啥的
        setContentView(R.layout.activity_main2)
        // setContentView(View(this))
/*
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
*/
        // lambdas的搞法..Flowable为何物？
        /*Flowable.just("hello world", "hello world2", "hello world3")
                .subscribe(System.out::println)
        // 不支持java8的lambdas的用法
        Flowable.just("hello world all")
                .subscribe(object: Consumer<String> {
                    override fun accept(t: String) {
                        System.out.println(t);
                    }
                })*/

        // 上游Flowable
        /*
        var upstream = Flowable.create(object : FlowableOnSubscribe<Integer> {
            override fun subscribe(emitter: FlowableEmitter<Integer>) {
                for (index in 1..10000) {
                    emitter.onNext(Integer(index))
                }
                emitter.onComplete()
            }
        }, BackpressureStrategy.BUFFER)
        // 为上游指定一个线程 - 又比如 -> .subscribeOn(Schedulers.io())
        upstream.subscribeOn(Schedulers.newThread()) // Now, 不懂
        // 为下游指定一个线程 - 比如Android指定AndroidUI线程 -> .observeOn(AndroidSchedulers.mainThread())
        upstream.observeOn(AndroidSchedulers.mainThread())
        upstream.subscribe(object : Subscriber<Integer> {
            override fun onComplete() {
                Log.e("rxjava", "onComplete");
                Toast.makeText(this@Main2Activity, "OK", Toast.LENGTH_SHORT).show()
            }

            override fun onSubscribe(s: Subscription?) {
                // 发起请求尼 - 跳过去看源码不懂，反正就是：只有调用了这个方法上游才会发送事件
                // n the strictly positive number of elements to requests to the upstream - 需要一个严格的正数去请求上游发送数据？
                s?.request(java.lang.Long.MAX_VALUE) // https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA%3D%3D&mid=2247484711&idx=1&sn=c3837b7cad21f0a69d7dccd1aaaf7721&chksm=96cda46aa1ba2d7ce145472449e5a832cd3ac0bc1f766fe09f1ce68ca46c16bab645e507f0b5
            }

            override fun onNext(t: Integer?) {
                // 一秒钟处理一条
                Thread.sleep(1000)
                Log.e("rxjava", "upstream's value=" + t);
            }

            override fun onError(t: Throwable?) {
            }

        })*/

        /*
        由于在RxJava在2.x以上版本，api改动还是比较大的.
        其中订阅时有两个Api : subscribe和subscribeWith,很多人可能不太明白应该使用哪个
        我的理解就是subscribeWith中会把方法参数返回回去接收的是ResourceSubscriber,
        而ResourceSubscriber实现了Disposable接口所以,一般subscribeWith用到使用Rx请求接口的这种情况,订阅后把请求参数返回回去,可以添加到CompositeDisposable中方便绑定Activity生命周期取消
        其实subscribe中除了重载参数是Observer的其他也都返回了Dispose对象,至于为什么这个方法没有返回暂时也不知道作者怎么想的.
        因为它返回值是void所以在请求接口时最好还是使用subscribeWith,添加订阅关系更方便了
         */
        // 别引错包了，是io.reactivex，不是rx.Observable
        /*var d = Observable.just("Hello world!")
                .delay(1, TimeUnit.SECONDS)
                .subscribeWith(object : DisposableObserver < String >() {
                    override fun onStart() {
                        Log.e("rxjava", "Start");
                    }
                    override fun onNext(t: String) {
                        Log.e("rxjava", "" + t);
                    }
                    override fun onError(t: Throwable) {
                        t.printStackTrace();
                    }
                    override fun onComplete() {
                        Log.e("rxjava", "Done");
                    }
                });
         Thread.sleep(500);
        // the sequence can now be disposed via dispose()
        // d.dispose();*/

        /*
        // 1. 创建一个被观察者 https://www.jianshu.com/p/05014add5fb5
        val observable = Observable.create(ObservableOnSubscribe<String> { emitter ->
            Log.d("observable", "observable")
            emitter.onNext("a")
            emitter.onNext("b")
            emitter.onNext("c")
            emitter.onComplete()
        })

        // 2. 创建一个订阅者(观察者)
        val observer = object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                Log.e("observable", "onSubscribe")
            }

            override fun onNext(s: String) {
                Log.e("observable", s)
            }

            override fun onError(e: Throwable) {
                Log.e("observable", "onError")
            }

            override fun onComplete() {
                Log.e("observable", "onComplete")
            }
        }

        // 订阅收消息(关联观察者与被观察者)
        observable.subscribe(observer);

        // 简单点也可以Consumer作为观察者 - 很多状态可能就没有了
        observable.subscribe(object : Consumer<String> {
            override fun accept(t: String?) {
                // "a" "b" "c"
                Log.e("observable accept", t)
            }
        })


        // 1. 默认是在当前的主线程(UI线程中) - 结果都是： thread'name=main
        val observable2 = Observable.create(ObservableOnSubscribe<String> { emitter ->
            Log.e("observable", "thread'name=" + Thread.currentThread().name)
            emitter.onNext("Hello I'm comming.")
            emitter.onComplete()
        })
        observable2.subscribe(object : Consumer<String> {
            override fun accept(t: String?) {
                // main线程中，可以进行ui操作
                Toast.makeText(this@Main2Activity, "OK", Toast.LENGTH_SHORT).show()
                Log.e("observable", "thread'name=" + Thread.currentThread().name)
            }
        })*/

        // 将处理放到一个子线程中去进行处理...我们可能要进行网络请求
        Thread(Runnable {
            /*
            // 2. 默认是在当前的子线程中 - 结果都是： thread'name=Thread-2
            val observable3 = Observable.create(ObservableOnSubscribe<String> { emitter ->
                Log.e("observable", "thread'name=" + Thread.currentThread().name)
                emitter.onNext("Hello I'm comming.")
                emitter.onComplete()
            })
            observable3.subscribe(object : Consumer<String> {
                override fun accept(t: String?) {
                    // 不能操作UI - 非main(UI)线程
                    // Toast.makeText(this@Main2Activity, "OK", Toast.LENGTH_SHORT).show()
                    Log.e("observable", "thread'name=" + Thread.currentThread().name)
                }
            })

            // 3. 默认是在当前的子线程中 - 结果都是： thread'name=Thread-2
            val observable4 = Observable.create(ObservableOnSubscribe<String> { emitter ->
                Log.e("observable", "thread'name=" + Thread.currentThread().name)
                emitter.onNext("Hello I'm comming.")
                emitter.onComplete()
            })
            observable4.subscribeOn(Schedulers.newThread()) // 指定上游是一个新的线程
                    .observeOn(AndroidSchedulers.mainThread())  // 指定下游是一个Android 主线程/UI线程
                    .subscribe(object : Consumer<String> {
                        override fun accept(t: String?) {
                            // 切换到main(UI)线程
                            Toast.makeText(this@Main2Activity, "OJBK", Toast.LENGTH_SHORT).show()
                            Log.e("observable", "thread'name=" + Thread.currentThread().name)
                        }
                    })
            observable4.subscribeOn(Schedulers.io()) // 指定上游是一个新的线程 - 从线程池中获取线程，减少创建线程的相关开销
                    .observeOn(AndroidSchedulers.mainThread())  // 指定下游是一个Android 主线程/UI线程
                    .subscribe { t ->
                        // 切换到main(UI)线程
                        Toast.makeText(this@Main2Activity, t, Toast.LENGTH_SHORT).show()
                        Log.e("observable", "thread'name=" + Thread.currentThread().name)
                    }
            */
        }).start()

        // 来个网络请求案例压压惊
        // 1. 创建Retroft实例对象
        var retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                // 别忘记了加支持RxJava2的适配器:旧版的别了呀 https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://api.github.com/")
                .build();

        // 2. 创建GitHub请求服务
        var gitHubService = retrofit.create(GitHubService::class.java);

        // 3. 获取服务对应的某个请求实例
        var gitCall = gitHubService.listReposStringRxJavaObservable("FanChael");

        // 4. RxJava请求走起
        var ds: Disposable? = null
        gitCall.subscribeOn(Schedulers.io()) // 指定上游一个子线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定下游UI/主main线程
                // 创建一个动态键的 observable 映射。 如果你不但想对一个特定项的更改做出反应，而且对添加或删除该项也做出反应的话，那么 observable 映射会非常有用
                //                .map { t ->
                //                    // lambda表达式样式呀
                //                    Log.e("observable", "map thread'name=" + Thread.currentThread().name)
                //                    var userList: MutableList<Repo> = ArrayList()
                //                    for (item in t) {
                //                        if (item.name?.contains("banner")!!) {
                //                            userList.add(item)
                //                        }
                //                    }
                //                    userList
                //                }
                .flatMap(object: Function < List<Repo>, ObservableSource <List<Repo>> >{
                    override fun apply(t: List<Repo>): ObservableSource<List<Repo>> {
                        Log.e("observable", "flatMap thread'name=" + Thread.currentThread().name)
                        // 中间也可以进行一些处理，不过flatmap可以将事件序列中的元素进行整合加工，返回一个新的被观察者
                        return Observable.fromArray(t)
                    }
                })
                .subscribe(object : Observer<List<Repo>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        // 保存起来，后面可以做一些取消处理
                        ds = d;
                    }

                    override fun onNext(t: List<Repo>) {
                        // UI线程规定不能进行耗时的操作，但是不一定耗时都会引起ANR；只要不影响UI渲染的卡顿，不一定会引起ANR
                        // Thread.sleep(100000)
                        /* UI线程中不能进行 okhttp同步请求
                        val client = OkHttpClient.Builder()
                                .build();
                        val request = Request.Builder().url("http://www.baidu.com").get().build()
                        val requestCall = client.newCall(request)
                        val response = requestCall.execute()
                        if (response.isSuccessful()) {
                            val json = response.body()?.string();
                            Log.e("observable", "json=" + json)
                        } */

                        Toast.makeText(this@Main2Activity, "OJBK", Toast.LENGTH_SHORT).show()
                        Log.e("observable", "thread'name=" + Thread.currentThread().name)
                        for (item in t) {
                            // SuperStartElectronic subtitle display - 电子字幕展示-接机、演唱会、见面、展示专用.https://github.com/FanChael/SuperStart
                            Log.e("observable", item.name + item.description + item.html_url)
                        }
                    }

                    override fun onError(e: Throwable) {
                    }
                })
        // 可以做取消请求处理
        //        if (null != ds && !ds!!.isDisposed) {
        //            ds!!.dispose()
        //        }
    }
}
