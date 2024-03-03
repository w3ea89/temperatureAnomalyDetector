package com.ks.kontakt.recruitment.temperatureAnomalyDetector.configuration;

import com.ks.kontakt.recruitment.temperatureAnomalyDetector.detector.algorithms.AnomalyDetectionMeasurementsWindow;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.detector.algorithms.AnomalyDetectionTimeBasedWindow;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.service.AnomalyStoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Value("${algorithm.timeBased.numberOfSeconds}")
    private int numberOfSeconds;
    @Value("${algorithm.timeBased.degreeDiffThreshold}")
    private int timeBasedTemperatureDiffThreshold;
    @Value("${algorithm.measurementsBased.numberOfMeasurements}")
    private int measurementsNumber;
    @Value("${algorithm.measurementsBased.degreeDiffThreshold}")
    private int measurementBasedTemperatureDiffThreshold;

    @Bean
    public AnomalyDetectionTimeBasedWindow anomalyDetectionTimeBasedWindow(AnomalyStoreService anomalyStoreService) {
        return new AnomalyDetectionTimeBasedWindow(anomalyStoreService, numberOfSeconds, timeBasedTemperatureDiffThreshold);
    }

    @Bean
    public AnomalyDetectionMeasurementsWindow anomalyDetectionMeasurementsWindow(AnomalyStoreService anomalyStoreService) {
        return new AnomalyDetectionMeasurementsWindow(anomalyStoreService, measurementsNumber, measurementBasedTemperatureDiffThreshold);
    }
}
