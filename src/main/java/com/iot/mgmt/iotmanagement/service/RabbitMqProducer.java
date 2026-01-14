package com.iot.mgmt.iotmanagement.service;

import com.iot.mgmt.iotmanagement.model.SensorEventDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqProducer implements Producer {

    private final RabbitTemplate rabbitTemplate;
    private final String eventExchange;
    private final String routingKey;

    public RabbitMqProducer(RabbitTemplate rabbitTemplate,
                            @Value("${iot.rabbitmq.exchange}") String eventExchange,
                            @Value("${iot.rabbitmq.routing-key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.eventExchange = eventExchange;
        this.routingKey = routingKey;
    }

    @Override
    public void sendMessage(SensorEventDto message) {
        rabbitTemplate.convertAndSend(
                eventExchange,
                routingKey,
                message
        );
    }

}
