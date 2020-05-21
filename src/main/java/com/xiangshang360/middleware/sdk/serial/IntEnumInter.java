package com.xiangshang360.middleware.sdk.serial;

/**
 * Created by Administrator on 2017/3/20.
 */
public interface IntEnumInter<E extends Enum<E>> {

    int intValue();

    @Override
    String toString();

}