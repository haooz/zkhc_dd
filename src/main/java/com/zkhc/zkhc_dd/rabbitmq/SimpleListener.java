package com.zkhc.zkhc_dd.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.zkhc.zkhc_dd.core.common.constant.Const;
import com.zkhc.zkhc_dd.service.DDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 手动确认
 */
@Component("simpleListener")
public class SimpleListener implements ChannelAwareMessageListener {

    private static final Logger log= LoggerFactory.getLogger(SimpleListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DDService ddService;

    @Override
    @RabbitListener(queues = Const.QUEUE_NAME,containerFactory = "singleListenerContainer")
    public void onMessage(Message message, Channel channel) throws Exception {
        long tag=message.getMessageProperties().getDeliveryTag();
        try {
            byte[] body=message.getBody();
            Map<String,Object> m=objectMapper.readValue(body,Map.class);
            String method=String.valueOf(m.get("method"));
            String msg=String.valueOf(m.get("msg"));
            switch (method) {
                case "sendStartUp":
                    ddService.sendStartUp();
                    break;
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
                case "exception":
                    ddService.exception(msg);
                    break;
                case "sendDealMsg":
                    String id = String.valueOf(m.get("id"));
                    ddService.sendDealMsg(id, msg);
                    break;
            }
            log.info("接收到消息： {} ",m.toString());
            channel.basicAck(tag, true);
        }catch (Exception e){
            log.error("消息监听发生异常：",e.fillInStackTrace());
            channel.basicReject(tag,false);
        }
    }
}
































