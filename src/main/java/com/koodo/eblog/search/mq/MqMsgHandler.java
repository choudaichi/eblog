package com.koodo.eblog.search.mq;

import com.koodo.eblog.config.RabbitMQConfig;
import com.koodo.eblog.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RabbitListener(queues = RabbitMQConfig.ES_QUEUE)
public class MqMsgHandler {

    @Autowired
    SearchService searchService;

    @RabbitHandler
    public void handler(PostMqIndexMsg message) {

        log.info("mq 收到一条消息---------->{}", message.toString());

        switch (message.getType()) {
            case PostMqIndexMsg.CREATE_OR_UPDATE:
                searchService.createOrUpdateIndex(message);
                break;
            case PostMqIndexMsg.REMOVE:
                searchService.removeIndex(message);
                break;
            default:
                log.error("没找到对应的消息类型，请注意！");
        }
    }

}
