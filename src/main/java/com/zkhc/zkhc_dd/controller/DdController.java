package com.zkhc.zkhc_dd.controller;

import com.zkhc.zkhc_dd.core.common.constant.Const;
import com.zkhc.zkhc_dd.response.BaseResponse;
import com.zkhc.zkhc_dd.response.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/dd")
public class DdController {
    private static final Logger log= LoggerFactory.getLogger(DdController.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/sendMsg",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse sendMsg(@RequestBody String textMsg){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        JSONObject jo = JSONObject.parseObject(textMsg);
        String message=jo.getString("textMsg");
        try {
            log.info("待发送的消息： {} ",message);
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.setExchange(Const.EXCHANGE_NAME);
            rabbitTemplate.setRoutingKey(Const.ROUTING_KEY_NAME);
            Message msg=MessageBuilder.withBody(message.getBytes("UTF-8")).build();
            rabbitTemplate.convertAndSend(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

}
