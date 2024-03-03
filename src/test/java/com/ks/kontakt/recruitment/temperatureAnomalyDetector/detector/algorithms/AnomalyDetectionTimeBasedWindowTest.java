package com.ks.kontakt.recruitment.temperatureAnomalyDetector.detector.algorithms;

import com.ks.kontakt.model.TemperatureMeasurement;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.AnomalyDetectionStrategy;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.TemperatureAnomaly;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.service.AnomalyStoreService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AnomalyDetectionTimeBasedWindowTest {

    private final AnomalyStoreService anomalyStoreService = mock(AnomalyStoreService.class);
    private final AnomalyDetectionTimeBasedWindow anomalyDetection = new AnomalyDetectionTimeBasedWindow(anomalyStoreService, 10, 5);

    @Test
    void whenAnomalyDetected_thenStoreMeasurementAsAnomaly() {
        // given
        Instant givenTime = Instant.now();
        // when
        when(anomalyStoreService.storeMeasurementAsAnomaly(argThat(temperatureMeasurement -> 29.3 == temperatureMeasurement.getTemperature()), eq(AnomalyDetectionStrategy.TIME_FRAME_BASED))).thenReturn(new TemperatureAnomaly());
        anomalyDetection.addMeasurement(createMeasurement(20.0, givenTime));
        anomalyDetection.addMeasurement(createMeasurement(20.1, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.2, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.3, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.2, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.2, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(29.3, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.2, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.1, givenTime.plusMillis(1)));

        // Then
        ArgumentCaptor<TemperatureMeasurement> temperatureMeasurementArgumentCaptor = ArgumentCaptor.forClass(TemperatureMeasurement.class);
        verify(anomalyStoreService, times(1)).storeMeasurementAsAnomaly(temperatureMeasurementArgumentCaptor.capture(), eq(AnomalyDetectionStrategy.TIME_FRAME_BASED));
        assertThat(temperatureMeasurementArgumentCaptor.getValue().getTemperature()).isEqualTo(29.3);
    }

    @Test
    void whenAnomalyNotDetectedCauseAnomalyIsNotInWindow_thenNoAnomalyIsStored() {
        // given
        Instant givenTime = Instant.now();
        // when
        anomalyDetection.addMeasurement(createMeasurement(29.3, givenTime));
        anomalyDetection.addMeasurement(createMeasurement(20.0, givenTime.plusSeconds(11)));
        anomalyDetection.addMeasurement(createMeasurement(20.1, givenTime.plusSeconds(11).plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.2, givenTime.plusSeconds(11).plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.3, givenTime.plusSeconds(11).plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.2, givenTime.plusSeconds(11).plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.2, givenTime.plusSeconds(11).plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.2, givenTime.plusSeconds(11).plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.1, givenTime.plusSeconds(11).plusMillis(1)));

        // Then
        verify(anomalyStoreService, never()).storeMeasurementAsAnomaly(any(TemperatureMeasurement.class), eq(AnomalyDetectionStrategy.TIME_FRAME_BASED));
    }

    @Test
    void whenAnomalyNotDetectedCauseNotAnomalyTemperatureIsPresent__thenNoAnomalyIsStored() {
        // given
        Instant givenTime = Instant.now();
        // when
        anomalyDetection.addMeasurement(createMeasurement(20.0, givenTime));
        anomalyDetection.addMeasurement(createMeasurement(20.1, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.2, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.3, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.2, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.2, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(23.3, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.2, givenTime.plusMillis(1)));
        anomalyDetection.addMeasurement(createMeasurement(20.1, givenTime.plusMillis(1)));

        // Then
        verify(anomalyStoreService, never()).storeMeasurementAsAnomaly(any(TemperatureMeasurement.class), eq(AnomalyDetectionStrategy.TIME_FRAME_BASED));
    }

    private TemperatureMeasurement createMeasurement(double temp, Instant givenTime) {
        return new TemperatureMeasurement(temp, givenTime, 1, 1);
    }
}
