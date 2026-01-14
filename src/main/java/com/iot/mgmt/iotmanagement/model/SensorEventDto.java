package com.iot.mgmt.iotmanagement.model;

import java.sql.Timestamp;

public record SensorEventDto(
        SensorType sensorType,
        String message,
        Timestamp msgTimestamp
) {
}
