package com.xusy.springbt.rabbitMq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xusy.springbt.domain.MiaoshaUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.*;

/**
 * 如果报403没有权限访问的错误，请到rabbitMq management为用户添加交换机权限：
 * Set permissions——>Topic permissions
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitMQTest {

    @Autowired
    private MQSender mqSender;

    @Test
    public void send() throws JsonProcessingException {
        mqSender.sendDirect("直连模式，队列名称：'queue'");
    }

    @Test
    public void sendTopic() throws JsonProcessingException {
        mqSender.sendTopic("主题模式，交换机名称：'topicExchage'");
    }

    @Test
    public void sendFanout() throws JsonProcessingException {
        mqSender.sendFanout("广播模式，交换机名称：'fanoutxchage'");
    }

    @Test
    public void sendHeader() throws JsonProcessingException {
        mqSender.sendHeader("header模式，交换机名称：'headersExchage'");
    }

    @Test
    public void sendMiaoshaMessage() throws JsonProcessingException {
        MiaoshaMessage mm = new MiaoshaMessage().setGoodsId(111).setUser(new MiaoshaUser().setId(2222l).setNickname("冷萌"));
        mqSender.sendDirectMiaoshaMessage(mm);
    }
}