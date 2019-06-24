package com.hl.rxnettest.design_pattern.adapter_pattern;

public class ObjectAdapter implements CustomNeed{
    private NeedAdapter needAdapter;

    public ObjectAdapter(NeedAdapter needAdapter){
        this.needAdapter = needAdapter;
    }

    public void havaSomething(){
        this.needAdapter.havaSomething();
    }


    @Override
    public void needEverything() {
        //  提供给用户所有选择，不然不够用户眼睛看！
        System.out.println("对象适配器： 随便你选择！");
    }
}
