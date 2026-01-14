package com.iot.mgmt.iotmanagement.service;

import com.iot.mgmt.iotmanagement.util.Utility;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class UdpServerMgmt {

    private final Producer producer;

    @PostConstruct
    public void startUdpServer() {
        log.info("UDP Server started...");
        Utility.loadSensorConfigs()
                .doOnNext(config ->
                        config.sensors().forEach(sensor -> {
                            var serverListener = new ServerListener(sensor, producer);
                            serverListener.startListening();
                        })
                ).subscribe();
    }

}
