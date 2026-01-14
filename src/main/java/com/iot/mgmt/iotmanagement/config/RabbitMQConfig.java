package com.iot.mgmt.iotmanagement.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private final String eventExchange;
    private final String eventQueue;
    private final String routingKey;

    public RabbitMQConfig(@Value("${iot.rabbitmq.exchange}") String eventExchange,
                          @Value("${iot.rabbitmq.queue}") String eventQueue,
                          @Value("${iot.rabbitmq.routing-key}") String routingKey) {
        this.eventExchange = eventExchange;
        this.eventQueue = eventQueue;
        this.routingKey = routingKey;
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(eventExchange);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(eventQueue).build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
