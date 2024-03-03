package com.ks.kontakt.recruitment.temperatureAnomalyDetector.detector.algorithms;

import com.ks.kontakt.model.TemperatureMeasurement;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.AnomalyDetectionStrategy;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.service.AnomalyStoreService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AnomalyDetectionMeasurementsWindowTest {

    private final AnomalyStoreService anomalyStoreService = mock(AnomalyStoreService.class);
    private final AnomalyDetectionMeasurementsWindow anomalyDetection = new AnomalyDetectionMeasurementsWindow(anomalyStoreService, 10, 5);

    @Test
    void whenAnomalyDetected_thenStoreMeasurementAsAnomaly() {
        Instant givenTime = Instant.now();
        addMeasurements(givenTime, 20.0, 20.1, 20.2, 20.3, 20.2, 30.3, 20.0, 20.1, 20.2, 20.3, 20.2);
        ArgumentCaptor<TemperatureMeasurement> captor = ArgumentCaptor.forClass(TemperatureMeasurement.class);
        verify(anomalyStoreService, times(1)).storeMeasurementAsAnomaly(captor.capture(), eq(AnomalyDetectionStrategy.NUMBER_OF_MEASUREMENTS));
        assertThat(captor.getValue().getTemperature()).isEqualTo(30.3);
    }

    @Test
    void whenAnomalyNotDetected_thenNoAnomalyReported() {
        Instant givenTime = Instant.now();
        addMeasurements(givenTime, 20.0, 20.1, 20.2, 20.3, 20.2, 20.3, 20.0, 20.1, 20.2, 20.3, 20.2, 20.3);
        verify(anomalyStoreService, never()).storeMeasurementAsAnomaly(any(TemperatureMeasurement.class), eq(AnomalyDetectionStrategy.NUMBER_OF_MEASUREMENTS));
    }

    @Test
    void whenAnomalyNotDetectedNotEnoughMeasurements_thenNoAnomalyReported() {
        Instant givenTime = Instant.now();
        addMeasurements(givenTime, 20.0, 20.1, 20.2, 20.3, 30.0);
        verify(anomalyStoreService, never()).storeMeasurementAsAnomaly(any(TemperatureMeasurement.class), eq(AnomalyDetectionStrategy.NUMBER_OF_MEASUREMENTS));
    }

    private void addMeasurements(Instant startTime, double... temps) {
        for (double temp : temps) {
            anomalyDetection.addMeasurement(createMeasurement(temp, startTime.plusMillis(100)));
        }
    }

    private TemperatureMeasurement createMeasurement(double temp, Instant time) {
        return new TemperatureMeasurement(temp, time, 1, 1);
    }
}
