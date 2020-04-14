package com.zkhc.zkhc_dd.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkhc.zkhc_dd.core.common.constant.Const;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class DDProvider {

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

    private void sendMq(String method,String message){
        Map<String,Object> dataMap= new HashMap();
        dataMap.put("method",method);
        dataMap.put("msg",message);
        try {
            Message msg=MessageBuilder.withBody(objectMapper.writeValueAsBytes(dataMap)).build();
            init().convertAndSend(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMq(String method){
        Map<String,Object> dataMap= new HashMap();
        dataMap.put("method",method);
        try {
            Message msg=MessageBuilder.withBody(objectMapper.writeValueAsBytes(dataMap)).build();
            init().convertAndSend(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMq(String method,String id,String message){
        Map<String,Object> dataMap= new HashMap();
        dataMap.put("method",method);
        dataMap.put("msg",message);
        dataMap.put("id",id);
        try {
            Message msg=MessageBuilder.withBody(objectMapper.writeValueAsBytes(dataMap)).build();
            init().convertAndSend(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exception(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        exception.printStackTrace(pw);
        String stackTraceString = sw.getBuffer().toString();
        stackTraceString=stackTraceString.replaceAll("\\\\","/");
        sendMq("exception",stackTraceString);
    }

    public void exception(String exception) {
        sendMq("exception",exception);
    }

    public void send(String message){
        sendMq("send",message);
    }

    public void devSend(String message) {
        sendMq("devSend",message);
    }

    public void testSend(String message) {
        sendMq("testSend",message);
    }

    public void proSend(String message) {
        sendMq("proSend",message);
    }

    public void sendAliMsg(String message) {
        sendMq("sendAliMsg",message);
    }

    public void sendDealMsg(String id,String message) {
        sendMq("sendDealMsg",id,message);
    }

    public void sendStartUp() {
        sendMq("sendStartUp");
    }
}
