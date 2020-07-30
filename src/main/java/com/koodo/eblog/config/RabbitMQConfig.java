package com.koodo.eblog.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ES_QUEUE = "es_queue";
    public static final String ES_EXCHANGE = "es_exchange";
    public static final String ES_BIND_KEY = "es_exchange";

    //消息队列
    @Bean
    public Queue esQueue() {
        return new Queue(ES_QUEUE);
    }

    //交换机
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(ES_EXCHANGE);
    }

    //绑定
    @Bean
    public Binding binding(Queue esQueue, DirectExchange exchange) {
        return BindingBuilder.bind(esQueue).to(exchange).with(ES_BIND_KEY);
    }


}
