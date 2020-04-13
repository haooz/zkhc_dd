package com.zkhc.zkhc_dd.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkhc.zkhc_dd.core.common.constant.Const;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Component
public class DDProvider {
    @Autowired
    private DDService ddService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private RabbitTemplate init(){
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setExchange(Const.EXCHANGE_NAME);
        rabbitTemplate.setRoutingKey(Const.ROUTING_KEY_NAME);
        return rabbitTemplate;
    }

    /*public void exception(Exception exception) {
        ddService.exception(exception);
    }*/

    public void exception(String exception) {
        ddService.exception(exception);
    }

    public void send(String message){
        Map<String,Object> dataMap= new HashMap();
        dataMap.put("method","send");
        dataMap.put("msg",message);
        try {
            Message msg=MessageBuilder.withBody(objectMapper.writeValueAsBytes(dataMap)).build();
            init().convertAndSend(msg);
            //init().convertAndSend(MessageBuilder.withBody(message.getBytes("UTF-8")).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void devSend(String message) {
        ddService.devSend(message);
    }

    public void testSend(String message) {
        ddService.testSend(message);
    }

    public void proSend(String message) {
        ddService.proSend(message);
    }

    public void sendAliMsg(String message) {
        ddService.sendAliMsg(message);
    }

    public void sendDealMsg(String id,String message) {
        ddService.sendDealMsg(id,message);
    }

    public void sendStartUp() {
        ddService.sendStartUp();
    }
}
