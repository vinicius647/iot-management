package com.iot.mgmt.centralservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.mgmt.centralservice.model.Constants;
import com.iot.mgmt.centralservice.model.SensorEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class SensorListener {

    @Autowired
    private ObjectMapper objectMapper;
    private final Integer thresholdTemperature;
    private final Integer thresholdHumidity;

    public SensorListener(@Value("${iot.threshold.temperature}") Integer thresholdTemperature,
                          @Value("${iot.threshold.humidity}") Integer thresholdHumidity) {
        this.thresholdTemperature = thresholdTemperature;
        this.thresholdHumidity = thresholdHumidity;
    }

    @RabbitListener(queues = "${iot.rabbitmq.queue}")
    public void receiveMessage(String message) {
        Mono.just(message)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(this::processSensorEvent)
                .subscribe();
    }

    private void processSensorEvent(String message) {

        SensorEventDto event = null;
        try {
            event = objectMapper.readValue(message, SensorEventDto.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse sensor event message: {}", message, e);
            return;
        }

        var map = mapPkg(event.message());

        switch (event.sensorType()) {
            case TEMPERATURE -> {
                if (Integer.parseInt(map.get(Constants.SENSOR_VALUE)) > thresholdTemperature) {
                    log.warn("Temperature threshold exceeded for sensor {}: {} > {}",
                            map.get(Constants.SENSOR_ID),
                            map.get(Constants.SENSOR_VALUE),
                            thresholdTemperature);
                }
            }
            case HUMIDITY -> {
                if (Integer.parseInt(map.get(Constants.SENSOR_VALUE)) > thresholdTemperature) {
                    log.warn("Humidity threshold exceeded for sensor {}: {} > {}",
                            map.get(Constants.SENSOR_ID),
                            map.get(Constants.SENSOR_VALUE),
                            thresholdHumidity);
                }
            }
            default -> log.error("Unknown sensor type: {}", event.sensorType());
        }

    }

    Map<String, String> mapPkg(String pkg) {

        return Stream.of(pkg.split(";"))
                .map(String::trim)
                .filter(s -> s.contains("="))
                .map(s -> s.split("=", 2))
                .collect(Collectors.toMap(
                        arr -> arr[0].trim(),
                        arr -> arr[1].trim(),
                        (a, b) -> b
                ));

    }

}
