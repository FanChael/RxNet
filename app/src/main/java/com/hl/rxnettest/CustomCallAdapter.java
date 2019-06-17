package com.hl.rxnettest;

import android.util.Log;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;

class CustomCallAdapter implements CallAdapter<R, Object> {
    private final Type responseType;

    public CustomCallAdapter(Type responseType){
        Log.e("test2", "CustomCallAdapter responseType=" + responseType);
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public CustomCall<R> adapt(Call<R> call) {
        Log.e("test2", "CustomCallAdapter adapt");
        return new CustomCall<>(call);
    }
}
