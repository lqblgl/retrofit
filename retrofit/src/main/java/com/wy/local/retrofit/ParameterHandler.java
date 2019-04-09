package com.wy.local.retrofit;

/**
 * 版本：V1.2.5
 * 时间： 2018/6/15 14:44
 * 创建人：laoqb
 * 作用：
 */
public abstract class ParameterHandler {
    abstract void apply(ServiceMethod serviceMethod,String value);

    static class Query extends ParameterHandler{

        private final String name;

        public Query(String name) {
            this.name = name;
        }

        @Override
        void apply(ServiceMethod serviceMethod, String value) {
            serviceMethod.addQueryParameter(name,value);
        }
    }

    static class Field extends ParameterHandler{

        private final String name;

        public Field(String name) {
            this.name = name;
        }

        @Override
        void apply(ServiceMethod serviceMethod, String value) {
            serviceMethod.addFieldParameter(name,value);
        }
    }
}
