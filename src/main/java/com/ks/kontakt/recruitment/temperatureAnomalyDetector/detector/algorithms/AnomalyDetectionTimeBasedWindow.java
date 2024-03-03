package com.ks.kontakt.recruitment.temperatureAnomalyDetector.detector.algorithms;

import com.ks.kontakt.model.TemperatureMeasurement;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.AnomalyDetectionStrategy;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.service.AnomalyStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnomalyDetectionTimeBasedWindow implements AnomalyDetector {

    private static Logger LOGGER = LoggerFactory.getLogger(AnomalyDetectionTimeBasedWindow.class);
    private final Map<Integer, List<TemperatureMeasurement>> measurements = new ConcurrentHashMap<>();
    private final AnomalyStoreService anomalyStoreService;
    private final int numberOfSeconds;
    private final int temperatureDiffThreshold;

    @Autowired
    public AnomalyDetectionTimeBasedWindow(AnomalyStoreService anomalyStoreService, int numberOfSeconds, int temperatureDiffThreshold) {
        this.anomalyStoreService = anomalyStoreService;
        this.numberOfSeconds = numberOfSeconds;
        this.temperatureDiffThreshold = temperatureDiffThreshold;
    }

    @Override
    public void addMeasurement(TemperatureMeasurement temperatureMeasurement) {
        measurements.putIfAbsent(temperatureMeasurement.getThermometerId(), new LinkedList<>());
        List<TemperatureMeasurement> sensorMeasurements = measurements.get(temperatureMeasurement.getThermometerId());
        sensorMeasurements.removeIf(m -> isOlderThanThreshold(temperatureMeasurement, m));
        sensorMeasurements.add(temperatureMeasurement);
        TemperatureMeasurement anomaly = detectAnomaly(sensorMeasurements);
        if (anomaly != null) {
            anomaly.getIsAnomaly().set(true);
            LOGGER.info(
                    "Time based anomaly detected on sensor {} in room {} at temperature {} on {}",
                    anomaly.getThermometerId(),
                    anomaly.getRoomId(),
                    anomaly.getTemperature(),
                    anomaly.getTimestamp()
            );
            anomalyStoreService.storeMeasurementAsAnomaly(anomaly, AnomalyDetectionStrategy.TIME_FRAME_BASED);
        }
    }

    private boolean isOlderThanThreshold(TemperatureMeasurement temperatureMeasurement, TemperatureMeasurement m) {
        return ChronoUnit.SECONDS.between(m.getTimestamp(), temperatureMeasurement.getTimestamp()) > numberOfSeconds;
    }

    private TemperatureMeasurement detectAnomaly(List<TemperatureMeasurement> sensorMeasurements) {
        if (sensorMeasurements.size() == 1) {
            return null;
        }
        double average = calculateAvg(sensorMeasurements);
        for (TemperatureMeasurement temperatureMeasurement : sensorMeasurements) {
            if (isAnomaly(average, temperatureMeasurement)) {
                return temperatureMeasurement;
            }
        }
        return null;
    }
    private boolean isAnomaly(double average, TemperatureMeasurement temperatureMeasurement) {
        return !temperatureMeasurement.getIsAnomaly().get()
                && Math.abs(temperatureMeasurement.getTemperature() - average) > temperatureDiffThreshold;
    }
    private double calculateAvg(List<TemperatureMeasurement> sensorMeasurements) {
        return sensorMeasurements.stream()
                .mapToDouble(TemperatureMeasurement::getTemperature)
                .sum() / sensorMeasurements.size();
    }
}
