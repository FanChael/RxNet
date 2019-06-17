package com.hl.rxnettest;

import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class CustomCallAdapterFactory extends CallAdapter.Factory{
    /**
     * Returns an instance which creates synchronous observables that do not operate on any scheduler
     * by default.
     */
    public static CustomCallAdapterFactory create() {
        return new CustomCallAdapterFactory();
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawObservableType = getRawType(returnType);
        // com.hl.rxnettest.CustomCall<java.lang.String>
        Log.e("test2", "get returnType=" + returnType);
        if (rawObservableType == CustomCall.class && returnType instanceof ParameterizedType) {
            // class com.hl.rxnettest.CustomCall
            Log.e("test2", "get rawObservableType=" + rawObservableType);
            Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
            // class java.lang.String
            Log.e("test2", "get observableType=" + observableType);
            return new CustomCallAdapter(observableType);
        }
        return null;
    }
}
