package com.hl.rxnettest;

import android.support.annotation.Nullable;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class MGsonConverterFactory extends Converter.Factory {
    private final Gson gson;

    public MGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new Converter<ResponseBody, List<Repo>>() {
            @Nullable
            @Override
            public List<Repo> convert(ResponseBody value) throws IOException {
                // 获取返回的字符串结果
                String result = value.string();
                // 用Gson转换为对象列表 - 这里展示 Converter.FactoryJson转换数据到对象的过程
                // -至于如何做到所有的对象，对象列表的转换，那就需要进一步去实现，同时这个type对应的对象的类型该如何运用还得进一步深入解读
                List<Repo> repoList = gson.fromJson(result, new TypeToken<List<Repo>>(){}.getType());
                // 返回转换后的对象列表 - GsonConverterFactory.java源码感觉有点难妮，自己太忒么菜了！
                return repoList;
            }
        };
    }
}
