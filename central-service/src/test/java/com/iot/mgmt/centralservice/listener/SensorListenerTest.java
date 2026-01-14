package com.iot.mgmt.centralservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.mgmt.centralservice.model.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SensorListenerTest {

    @Mock
    ObjectMapper objectMapper;

    private SensorListener sensorListener;

    @BeforeEach
    void setUp() {
        sensorListener = new SensorListener(25, 60);
    }

    @Test
    void shouldParsePackage() {
        var map = sensorListener.mapPkg("sensor_id=t1; value=30");
        assertEquals("t1", map.get(Constants.SENSOR_ID));
        assertEquals("30", map.get(Constants.SENSOR_VALUE));
    }

}
