package com.xiangshang360.middleware.sdk.queue.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * key 生成器
 *
 * @author 段子彧
 * @date 20180914
 */
public class KeyGenerator {
    private static final Logger LOG = LoggerFactory
            .getLogger(KeyGenerator.class);

    /**
     * redis连接池
     */
    private JedisPool jedisPool;

    /**
     * 队列在redis中的名称
     */
    private String queueRedisName;


    private static final String ORI_POSITION_SUFFIX = "_ORI_POSITION";

    private static final String TIMESTAMP_SUFFIX = "_TIMESTAMP";

    private static final String POSITION_PROGRESS_SUFFIX = "_POSITION_PROGRESS";

    private static final String AMOUNT_SUFFIX = "_AMOUNT";


    public KeyGenerator(JedisPool jedisPool, String queueRedisName) {
        this.jedisPool = jedisPool;
        this.queueRedisName = queueRedisName;
    }


    /**
     * 根据业务id获取key
     *
     * @param unitId
     * @param redisPriorityQueueKeyEnum
     * @return
     */
    public String getKey(String unitId, RedisPriorityQueueKeyEnum redisPriorityQueueKeyEnum) {

        switch (redisPriorityQueueKeyEnum) {
            case ID_KEY:
                return getId(unitId);
            case ADD_ID_KEY:
                return addId(unitId);
            case AMOUNT_KEY:
                return getAmountKey(getId(unitId));
            case PROGRESS_ID_KEY:
                return getProgressQueueId(getId(unitId));
            case PROGRESS_KEY:
                return getProgressKey(getId(unitId));
            case ORI_POSITION_KEY:
                return getPositionKey(getId(unitId));
            default:
                throw new RuntimeException("unsupported keyENum");
        }


    }

    private String addId(String unitId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String redisId = queueRedisName + "_" + unitId;
            String timestampKey = redisId + TIMESTAMP_SUFFIX;
            String timeStr;

            Long now = System.currentTimeMillis();
            timeStr = String.valueOf(now);
            jedis.set(timestampKey, timeStr);

            return timeStr + "_" + queueRedisName + "_" + unitId;
        } catch (Exception e) {
            LOG.error("queue:{} addId error,{}", queueRedisName, unitId, e);
            return null;
        }
    }

    /**
     * 根据队列中的id获取key
     *
     * @param id
     * @param redisPriorityQueueKeyEnum
     * @return
     */
    public String getKeyById(String id, RedisPriorityQueueKeyEnum redisPriorityQueueKeyEnum) {
        switch (redisPriorityQueueKeyEnum) {

            case AMOUNT_KEY:
                return getAmountKey(id);
            case PROGRESS_ID_KEY:
                return getProgressQueueId(id);
            case PROGRESS_KEY:
                return getProgressKey(id);
            case ORI_POSITION_KEY:
                return getPositionKey(id);
            default:
                throw new RuntimeException("unsupported keyENum");
        }
    }

    /**
     * 转化为redisKey
     *
     * @param unitId 业务id
     * @return
     */
    private String getId(String unitId) {

        try (Jedis jedis = jedisPool.getResource()) {
            String redisId = queueRedisName + "_" + unitId;
            String timestampKey = redisId + TIMESTAMP_SUFFIX;
            String timeStr = jedis.get(timestampKey);
            if (StringUtils.isEmpty(timeStr)) {
                return null;
            }

            return timeStr + "_" + queueRedisName + "_" + unitId;
        } catch (Exception e) {
            LOG.error("queue:{} getId error,{}", queueRedisName, unitId, e);
            return null;
        }


    }

    /**
     * 原始队列长度key
     *
     * @param id
     * @return
     */
    private String getPositionKey(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }

        return id + ORI_POSITION_SUFFIX;
    }

    /**
     * 进度key
     *
     * @param id
     * @return
     */
    private String getProgressKey(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }

        return id + POSITION_PROGRESS_SUFFIX;
    }

    /**
     * 金额KEY
     *
     * @param id
     * @return
     */
    private String getAmountKey(String id) {

        if (StringUtils.isEmpty(id)) {
            return null;
        }

        return id + AMOUNT_SUFFIX;
    }

    /**
     * 获取进度条id
     * 主要为了减小id长度，节省空间
     *
     * @param id 业务队列中的id
     * @return
     */
    private String getProgressQueueId(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return StringUtils.remove(id, "_" + queueRedisName);
    }
}
