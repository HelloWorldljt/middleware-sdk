package com.xiangshang360.middleware.sdk.serial.redis;

import com.xiangshang360.middleware.sdk.serial.SerialNumberEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * 双机房场景自增序列生成器
 * @author duanz
 * @date 2019-08-23
 */
public class DistributeRedisSerialNumberSupport extends RedisSerialNumberSupport {
    private String dataCenterCode;

    public DistributeRedisSerialNumberSupport(String dataCenterCode) {
        super();
        if(StringUtils.isEmpty(dataCenterCode)){
            throw new RuntimeException("dataCenterCode must not null");
        }
        this.dataCenterCode = dataCenterCode;
    }

    @Override
    public String generate(SerialNumberEnum enumType, int length) {
        if (length < MIN_NUMBER_LEN) {
            length = MIN_NUMBER_LEN;
        }
        int prefix = enumType.getPrefix();
        checkPrefix(prefix);
        String redisServerDate = getRedisServerDate();
        StringBuilder sb = new StringBuilder();
        // 101
        sb.append(dataCenterCode);
        sb.append(prefix);
        // 获取Redis当前日期.20170425
        sb.append(redisServerDate);
        // 000000001
        sb.append(generateByIncrement(redisServerDate, enumType.name(), length));
        return sb.toString();
    }

    @Override
    public String generateDateBegin(SerialNumberEnum enumType, int length) {
        if (length < MIN_NUMBER_LEN) {
            length = MIN_NUMBER_LEN;
        }
        int prefix = enumType.getPrefix();
        checkPrefix(prefix);
        String redisServerDate = getRedisServerDate();
        StringBuilder sb = new StringBuilder();
        // 获取Redis当前日期.20170425
        sb.append(redisServerDate);
        sb.append(dataCenterCode);
        sb.append(prefix);
        // 000000001
        sb.append(generateByIncrement(redisServerDate, enumType.name(), length));
        return sb.toString();
    }

    @Override
    public long generate(SerialNumberEnum enumType) {
        throw new RuntimeException("not support");
    }

    @Override
    public long generateDateBegin(SerialNumberEnum enumType) {
        throw new RuntimeException("not support");
    }
}
