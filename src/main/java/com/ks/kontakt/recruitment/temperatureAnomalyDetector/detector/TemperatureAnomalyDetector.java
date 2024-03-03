package com.ks.kontakt.recruitment.temperatureAnomalyDetector.detector;

import com.ks.kontakt.model.TemperatureMeasurement;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.detector.algorithms.AnomalyDetectionMeasurementsWindow;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.detector.algorithms.AnomalyDetectionTimeBasedWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TemperatureAnomalyDetector {
    private static Logger LOGGER = LoggerFactory.getLogger(TemperatureAnomalyDetector.class);
    private final AnomalyDetectionTimeBasedWindow anomalyDetectionTimeBasedWindow;
    private final AnomalyDetectionMeasurementsWindow anomalyDetectionTenMeasurementsWindow;

    @Autowired
    public TemperatureAnomalyDetector(AnomalyDetectionTimeBasedWindow anomalyDetectionTimeBasedWindow,
                                      AnomalyDetectionMeasurementsWindow anomalyDetectionTenMeasurementsWindow) {
        this.anomalyDetectionTimeBasedWindow = anomalyDetectionTimeBasedWindow;
        this.anomalyDetectionTenMeasurementsWindow = anomalyDetectionTenMeasurementsWindow;
    }

    @KafkaListener(topics = "temperatureMeasurements", groupId = "temperature-anomaly-detector")
    public void listenMeasurements(TemperatureMeasurement temperatureMeasurement) {
        LOGGER.debug("Processing temperature {} from room {} and sensor {}",
                temperatureMeasurement.getTemperature(),
                temperatureMeasurement.getRoomId(),
                temperatureMeasurement.getThermometerId()
        );
        anomalyDetectionTimeBasedWindow.addMeasurement(temperatureMeasurement);
        anomalyDetectionTenMeasurementsWindow.addMeasurement(temperatureMeasurement);
    }
}
