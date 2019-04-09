package com.wy.local.retrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * 版本：V1.2.5
 * 时间： 2018/6/15 14:08
 * 创建人：laoqb
 * 作用：
 */
public class Retrofit {

    Map<Method,ServiceMethod> serviceMethodMapCache = new HashMap<>();
    Call.Factory callFactroy;
    HttpUrl baseUrl;

    public Retrofit(Builder builder) {
        callFactroy = builder.callFactroy;
        baseUrl = builder.baseUrl;
    }

    HttpUrl baseUrl(){
        return baseUrl;
    }

    Call.Factory callFactroy(){
        return callFactroy;
    }

    public <T> T create(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ServiceMethod serviceMethod = loadServiceMethod(method);
                return new OkHttpCall(serviceMethod,args);
            }
        });
    }

    private ServiceMethod loadServiceMethod(Method method) {
        ServiceMethod serviceMethod = serviceMethodMapCache.get(method);
        if(null == serviceMethod){
            serviceMethod = new ServiceMethod.Builder(this, method).build();
            serviceMethodMapCache.put(method,serviceMethod);
        }
        return serviceMethod;
    }

    public static class Builder {
        Call.Factory callFactroy;
        HttpUrl baseUrl;

        public Builder baseUrl(HttpUrl baseUrl){
            this.baseUrl = baseUrl;
            return this;
        }
        public Builder baseUrl(String baseUrl){
            this.baseUrl = HttpUrl.parse(baseUrl);
            return this;
        }
        public Builder callFactroy(Call.Factory callFactroy){
            this.callFactroy = callFactroy;
            return this;
        }
        public Retrofit build(){
            if( null == baseUrl){
                throw new IllegalArgumentException("baseUrl is required");
            }
            if(null == callFactroy){
                callFactroy = new OkHttpClient();
            }
            return new Retrofit(this);
        }
    }


}
