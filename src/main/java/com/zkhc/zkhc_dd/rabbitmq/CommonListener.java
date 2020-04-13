package com.zkhc.zkhc_dd.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkhc.zkhc_dd.core.common.constant.Const;
import com.zkhc.zkhc_dd.service.DDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Administrator on 2018/8/23.
 */
@Component
public class CommonListener {

    private static final Logger log= LoggerFactory.getLogger(CommonListener.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DDService ddService;

    /**
     * 监听消费消息
     * @param message
     */
    /*@RabbitListener(queues = Const.QUEUE_NAME,containerFactory = "singleListenerContainer")
    public void consumeMessage(@Payload byte[] message){
        try {
            //TODO：接收String
            String result=new String(message,"UTF-8");
            log.info("接收到消息： {} ",result);
            //ddProvider.send(result);
        }catch (Exception e){
            log.error("监听消费消息 发生异常： ",e.fillInStackTrace());
        }
    }*/

    /**
     * 监听消费消息-map多类型字段信息
     */
    @RabbitListener(queues = Const.QUEUE_NAME,containerFactory = "singleListenerContainer")
    public void consumeMessage(Map<String,Object> message){
        try {
            String method=String.valueOf(message.get("method"));
            String msg=String.valueOf(message.get("msg"));
            switch (method){
                case "send":
                    ddService.send(msg);
                    break;
                case "devSend":
                    ddService.devSend(msg);
                    break;
                case "testSend":
                    ddService.testSend(msg);
                    break;
                case "proSend":
                    ddService.proSend(msg);
                    break;
                case "sendAliMsg":
                    ddService.sendAliMsg(msg);
                    break;
                case "sendStartUp":
                    ddService.sendStartUp();
                    break;
                case "exception":
                    ddService.exception(msg);
                    break;
                case "sendDealMsg":
                    String id=String.valueOf(message.get("id"));
                    ddService.sendDealMsg(id,msg);
                    break;
            }
            log.info("接收到消息： {} ",message.toString());
        }catch (Exception e){
            log.error("监听消费消息 发生异常： ",e.fillInStackTrace());
        }
    }

}

















