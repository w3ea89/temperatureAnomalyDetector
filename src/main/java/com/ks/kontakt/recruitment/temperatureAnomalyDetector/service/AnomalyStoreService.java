package com.ks.kontakt.recruitment.temperatureAnomalyDetector.service;

import com.ks.kontakt.model.TemperatureMeasurement;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.AnomalyDetectionStrategy;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.TemperatureAnomaly;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.repository.TemperatureAnomalyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnomalyStoreService {
    private final TemperatureAnomalyRepository temperatureAnomalyRepository;

    @Autowired
    public AnomalyStoreService(TemperatureAnomalyRepository temperatureAnomalyRepository) {
        this.temperatureAnomalyRepository = temperatureAnomalyRepository;
    }

    public TemperatureAnomaly storeMeasurementAsAnomaly(TemperatureMeasurement measurement,
                                                        AnomalyDetectionStrategy detectionStrategy) {
        TemperatureAnomaly anomaly = new TemperatureAnomaly();
        anomaly.setAnomalyDetectionStrategy(detectionStrategy);
        anomaly.setSensorId(measurement.getThermometerId());
        anomaly.setTimestamp(measurement.getTimestamp());
        anomaly.setRoomId(measurement.getRoomId());
        anomaly.setTemperature(measurement.getTemperature());
        return temperatureAnomalyRepository.save(anomaly).block();
    }
}
