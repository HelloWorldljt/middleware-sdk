package com.xiangshang360.middleware.sdk.timechanger;

import com.xiangshang360.middleware.config.ConfigFactory;
import com.xiangshang360.middleware.redis.JedisPoolFactory;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.Date;

/**
 * 基于redis 时间时间修改
 * @author duanz
 * @date 2019-06-27
 */
public class RedisTimeChanger implements TimeChanger{

    private static final Logger LOG = LoggerFactory
            .getLogger(RedisTimeChanger.class);
    private JedisPool jedisPool;

    private static final String OFFSET_KEY="REDIS_TIME_CHANGER_KEY";

    private static final String CONFIG_KEY="time-changer.enable";

    private Boolean enable = false;

    public RedisTimeChanger() {
        try {
            enable= Boolean.valueOf(ConfigFactory.getString(CONFIG_KEY));
            this.jedisPool = JedisPoolFactory.getJedisPool();

        } catch (ConfigurationException | IOException e) {
            LOG.error(" init RedisTimeChanger error" , e);
        }

    }

    @Override
    public void setCurrentTimeAs(Date targetDate) {
        if(!enable){
            return;
        }
        try(Jedis jedis=jedisPool.getResource()){
            if(targetDate!=null){
                jedis.set(OFFSET_KEY, String.valueOf(targetDate.getTime()-System.currentTimeMillis()));
            }else {
                LOG.error("  RedisTimeChanger setCurrentTimeAs error,targetDate is null");
            }
        }catch (Exception e){
            LOG.error("  RedisTimeChanger setCurrentTimeAs error",e);
        }
    }

    @Override
    public Date getCurrentTime() {
        if(!enable){
            return new Date();
        }
        try(Jedis jedis=jedisPool.getResource()){
           String offsetStr= jedis.get(OFFSET_KEY);

           if(StringUtils.isNotEmpty(offsetStr)){

               return new Date(System.currentTimeMillis()+Long.valueOf(offsetStr));
           }

        }catch (Exception e){
            LOG.error("  RedisTimeChanger getCurrentTime error",e);
        }
        return new Date();
    }

    @Override
    public void reset() {
        if(!enable){
            return;
        }
        try(Jedis jedis=jedisPool.getResource()){
            jedis.del(OFFSET_KEY);
        }catch (Exception e){
            LOG.error("  RedisTimeChanger reset error",e);
        }
    }
}
