package com.xusy.springbt.rabbitMq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author xsy
 * @create 2020-01-06 15:28
 * @desc 消息接收端
 **/
@Service
public class MQReceiver {
    @Autowired
    @Qualifier("jsonMapper")
    private ObjectMapper jsonMapper;
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        log.info("receive direct queue message:" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message) {
        log.info("receive topic topic.queue1 message:" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message) {
        log.info("receive topic topic.queue2 message:" + message);
    }

    @RabbitListener(queues = MQConfig.FANOUT_QUEUE)
    public void receiveFanout(String message) {
        log.info("receive fanout fanout.queue message:" + message);
    }

    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void receiveHeaderQueue(byte[] message) {
        log.info("receive header header.queue message:" + new String(message));
    }

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receiveMiaoSha(String message) throws IOException {
        log.info("receive direct miaosha.queue message:" + message);
        MiaoshaMessage miaoshaMessage = jsonMapper.readValue(message, MiaoshaMessage.class);
        log.info(miaoshaMessage.toString());
    }
}
