package com.wy.local.retrofit;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 版本：V1.2.5
 * 时间： 2018/6/15 15:06
 * 创建人：laoqb
 * 作用：
 */
public class OkHttpCall implements Call{

    private final ServiceMethod serviceMethod;
    private final Object[] args;
    private final Call realCall;

    public OkHttpCall(ServiceMethod serviceMethod, Object[] args) {
        this.serviceMethod= serviceMethod;
        this.args = args;
        realCall = serviceMethod.toCall(args);
    }

    @Override
    public Request request() {
        return realCall.request();
    }

    @Override
    public Response execute() throws IOException {
        return realCall.execute();
    }

    @Override
    public void enqueue(Callback responseCallback) {
        realCall.enqueue(responseCallback);
    }

    @Override
    public void cancel() {
        realCall.cancel();
    }

    @Override
    public boolean isExecuted() {
        return realCall.isExecuted();
    }

    @Override
    public boolean isCanceled() {
        return realCall.isCanceled();
    }

    @Override
    public Call clone() {
        return realCall.clone();
    }
}
