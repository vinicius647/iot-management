package com.iot.mgmt.iotmanagement.service;

import com.iot.mgmt.iotmanagement.model.Sensor;
import com.iot.mgmt.iotmanagement.model.SensorEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.udp.UdpServer;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

@RequiredArgsConstructor
@Slf4j
public class ServerListener {

    private final Sensor sensor;
    private final Producer producer;

    public void startListening() {
        UdpServer.create()
                .port(sensor.port())
                .handle((in, out) -> in.receive()
                        .asByteArray()
                        .doOnNext(this::processPacket)
                        .then())
                .bind()
                .doOnSuccess(c ->
                        log.info("Listening for UDP packets on port {}", sensor.port()))
                .doOnError(error ->
                        log.error("Failed to bind UDP server on port {}: {}", sensor.port(), error.getMessage()))
                .subscribe();
    }

    private void processPacket(byte[] packet) {
        log.info("{} packet received on port {}: {}", sensor.type(), sensor.port(), new String(packet));

        var message = new SensorEventDto(sensor.type(),
                new String(packet, StandardCharsets.UTF_8),
                new Timestamp(System.currentTimeMillis())
        );

        producer.sendMessage(message);
    }
}
