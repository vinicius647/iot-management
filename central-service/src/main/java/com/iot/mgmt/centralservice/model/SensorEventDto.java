package com.iot.mgmt.centralservice.model;

import java.sql.Timestamp;

public record SensorEventDto(
        SensorType sensorType,
        String message,
        Timestamp msgTimestamp
) {
}
