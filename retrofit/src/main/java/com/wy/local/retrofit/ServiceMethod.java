package com.wy.local.retrofit;

import com.wy.local.retrofit.api.http.Field;
import com.wy.local.retrofit.api.http.GET;
import com.wy.local.retrofit.api.http.POST;
import com.wy.local.retrofit.api.http.Query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * 版本：V1.2.5
 * 时间： 2018/6/15 14:29
 * 创建人：laoqb
 * 作用：
 */
public class ServiceMethod {

    private final HttpUrl baseUrl;
    private final Call.Factory callFactroy;
    private final String relativeUrl;
    private final ParameterHandler[] parameterHandlers;
    private final String httpMethod;
    private HttpUrl.Builder urlBuilder;
    private FormBody.Builder formBuilder;

    public ServiceMethod(Builder builder) {
        baseUrl = builder.retrofit.baseUrl();
        callFactroy = builder.retrofit.callFactroy();
        relativeUrl = builder.relativeUrl;
        parameterHandlers = builder.parameterHandlers;
        httpMethod = builder.httpMethod;
    }

    public Call toCall(Object[] args) {
        for (int i = 0; i < parameterHandlers.length; i++) {
            parameterHandlers[i].apply(this, String.valueOf(args));
        }
        FormBody body = null;
        if(formBuilder != null){
            body = formBuilder.build();
        }
        if(null == urlBuilder){
            urlBuilder = baseUrl.newBuilder(relativeUrl);
        }
        Request request = new Request.Builder().method(httpMethod, body).url(urlBuilder.build()).build();
        return callFactroy.newCall(request);
    }

    public void addQueryParameter(String name,String value) {
         if(null == urlBuilder){
             urlBuilder = baseUrl.newBuilder(relativeUrl);
         }
        urlBuilder.addQueryParameter(name,value);
    }

    public void addFieldParameter(String name,String value) {
        if(null == formBuilder){
            formBuilder = new FormBody.Builder();
        }
        formBuilder.add(name,value);
    }

    static class Builder{
        private final Retrofit retrofit;
        private final Annotation[] methodAnnotations;
        private final Annotation[][] parameterAnnotations;
        private String httpMethod;
        private String relativeUrl;
        private ParameterHandler[] parameterHandlers;

        public Builder(Retrofit retrofit, Method method) {
            this.retrofit = retrofit;
            this.methodAnnotations = method.getAnnotations();
            parameterAnnotations = method.getParameterAnnotations();
        }
        public ServiceMethod build(){
            for (Annotation methodAnnotation : methodAnnotations) {
                parseMethodAnnotation(methodAnnotation);
            }
            parameterHandlers = new ParameterHandler[parameterAnnotations.length];
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] annotations = parameterAnnotations[i];
                for (Annotation annotation : annotations) {
                    if(annotation instanceof Query){
                        String name = ((Query) annotation).value();
                        parameterHandlers[i] = new ParameterHandler.Query(name);
                    }else if(annotation instanceof Field){
                        String name = ((Field) annotation).value();
                        parameterHandlers[i] = new ParameterHandler.Field(name);
                    }
                }
            }
            return new ServiceMethod(this);
        }

        private void parseMethodAnnotation(Annotation methodAnnotation) {
            if(methodAnnotation instanceof GET){
                this.httpMethod = "GET";
                this.relativeUrl = ((GET)(methodAnnotation)).value();
            }else if(methodAnnotation instanceof POST){
                this.httpMethod = "POST";
                this.relativeUrl = ((POST)(methodAnnotation)).value();
            }
        }
    }
}
