package com.wy.local.retrofit.api.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 版本：V1.2.5
 * 时间： 2018/6/15 14:04
 * 创建人：laoqb
 * 作用：
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GET {
    String value();
}
