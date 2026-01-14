package com.iot.mgmt.iotmanagement.model;

public record Sensor(
        SensorType type,
        Integer port
) {
}
