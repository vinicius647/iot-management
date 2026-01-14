package com.iot.mgmt.iotmanagement.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.mgmt.iotmanagement.model.SensorConfig;
import reactor.core.publisher.Mono;

public class Utility {

    private Utility() {
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Mono<SensorConfig> loadSensorConfigs() {

        return Mono.fromCallable(() -> {
            var classLoader = Thread.currentThread().getContextClassLoader();

            try (var inputStream = classLoader.getResourceAsStream("sensor-ports.json")) {
                if (inputStream == null) {
                    throw new IllegalArgumentException("Resource not found: sensor-ports.json");
                }
                return mapper.readValue(inputStream, SensorConfig.class);
            }
        });

    }
}
