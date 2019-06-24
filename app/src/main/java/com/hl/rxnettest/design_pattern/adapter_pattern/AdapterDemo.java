package com.hl.rxnettest.design_pattern.adapter_pattern;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AdapterDemo {
    public static void main(String[] args){
        // 类适配器
        Adapter adapter = new Adapter();
        adapter.havaSomething();
        adapter.needEverything();

        // 对象适配器
        ObjectAdapter objectAdapter = new ObjectAdapter(new NeedAdapter());
        objectAdapter.havaSomething();
        objectAdapter.needEverything();

        // 看看Android的BaseApdater
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }
        };
    }
}
