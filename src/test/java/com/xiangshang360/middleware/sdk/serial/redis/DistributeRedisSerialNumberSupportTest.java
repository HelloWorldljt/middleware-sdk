package com.xiangshang360.middleware.sdk.serial.redis;

import com.xiangshang360.middleware.sdk.serial.SerialNumberEnum;
import org.junit.Test;

import static org.junit.Assert.*;

public class DistributeRedisSerialNumberSupportTest {

    @Test
    public void generate() {
        DistributeRedisSerialNumberSupport serialNumberSupport = new DistributeRedisSerialNumberSupport("sb");
        String serial = serialNumberSupport.generate(SerialNumberEnum.ACT_BUY_FREEZE_ACCOUNT_SEQ,10);
        System.out.println(serial);
    }

    @Test
    public void generateDateBegin() {
        DistributeRedisSerialNumberSupport serialNumberSupport = new DistributeRedisSerialNumberSupport("sb");
        String serial = serialNumberSupport.generateDateBegin(SerialNumberEnum.ACT_BUY_FREEZE_ACCOUNT_SEQ,10);
        System.out.println(serial);
    }
}