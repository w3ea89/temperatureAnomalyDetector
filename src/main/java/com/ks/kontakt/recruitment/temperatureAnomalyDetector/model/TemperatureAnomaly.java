package com.ks.kontakt.recruitment.temperatureAnomalyDetector.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
public class TemperatureAnomaly {
    @Id
    private String id;
    private Integer sensorId;
    private Integer roomId;
    private AnomalyDetectionStrategy anomalyDetectionStrategy;
    private Instant timestamp;
    private double temperature;

    public TemperatureAnomaly() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public AnomalyDetectionStrategy getAnomalyDetectionStrategy() {
        return anomalyDetectionStrategy;
    }

    public void setAnomalyDetectionStrategy(AnomalyDetectionStrategy anomalyDetectionStrategy) {
        this.anomalyDetectionStrategy = anomalyDetectionStrategy;
    }
}


