package com.iot.mgmt.iotmanagement.service;

import com.iot.mgmt.iotmanagement.model.SensorEventDto;

public interface Producer {

    void sendMessage(SensorEventDto message);
}
