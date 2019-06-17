package com.hl.rxnettest;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomCall<R> {
    public final Call<R> call;

    public CustomCall(Call<R> call) {
        this.call = call;
    }

    public R get() throws IOException {
        return (R)"获取数据呀！";//call.execute().body();
    }

    public void enqueue(final Callback<R> _call){
        call.enqueue(new Callback<R>() {
            @Override
            public void onResponse(Call<R> call, Response<R> response) {
                _call.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<R> call, Throwable t) {
                _call.onFailure(call, t);
            }
        });
    }
}
