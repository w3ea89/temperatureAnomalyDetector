package com.ks.kontakt.recruitment.temperatureAnomalyDetector.detector.algorithms;

import com.ks.kontakt.model.TemperatureMeasurement;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.AnomalyDetectionStrategy;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.service.AnomalyStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnomalyDetectionMeasurementsWindow implements AnomalyDetector {
    private static Logger LOGGER = LoggerFactory.getLogger(AnomalyDetectionMeasurementsWindow.class);
    private final Map<Integer, List<TemperatureMeasurement>> measurements = new ConcurrentHashMap<>();
    private final AnomalyStoreService anomalyStoreService;
    private final int numberOfMeasurements;
    private final int temperatureDiffThreshold;

    public AnomalyDetectionMeasurementsWindow(AnomalyStoreService anomalyStoreService, int numberOfMeasurements, int temperatureDiffThreshold) {
        this.anomalyStoreService = anomalyStoreService;
        this.numberOfMeasurements = numberOfMeasurements;
        this.temperatureDiffThreshold = temperatureDiffThreshold;
    }


    public void addMeasurement(TemperatureMeasurement measurement) {
        measurements.putIfAbsent(measurement.getThermometerId(), new LinkedList<>());
        List<TemperatureMeasurement> sensorMeasurements = measurements.get(measurement.getThermometerId());

        if (sensorMeasurements.size() >= numberOfMeasurements) {
            sensorMeasurements.remove(0);
        }
        sensorMeasurements.add(measurement);

        TemperatureMeasurement anomaly = hasAnomaly(sensorMeasurements);
        if (anomaly != null) {
            anomaly.getIsAnomaly().set(true);
            LOGGER.info(
                    "Measurements anomaly detected on sensor {} in room {} at temperature {} on {}",
                    anomaly.getThermometerId(),
                    anomaly.getRoomId(),
                    anomaly.getTemperature(),
                    anomaly.getTimestamp()
            );
            anomalyStoreService.storeMeasurementAsAnomaly(anomaly, AnomalyDetectionStrategy.NUMBER_OF_MEASUREMENTS);
        }
    }

    private TemperatureMeasurement hasAnomaly(List<TemperatureMeasurement> temperatures) {
        if (temperatures.size() < numberOfMeasurements) {
            return null;
        }
        double average = calculateAvgTemp(temperatures);
        for (TemperatureMeasurement temperatureMeasurement : temperatures) {
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

    private double calculateAvgTemp(List<TemperatureMeasurement> temperatures) {
        double sum = temperatures.stream()
                .mapToDouble(TemperatureMeasurement::getTemperature)
                .sum();
        return sum / temperatures.size();
    }
}
