package com.ks.kontakt.recruitment.temperatureAnomalyDetector.service;

import com.ks.kontakt.model.TemperatureMeasurement;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.AnomalyDetectionStrategy;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.TemperatureAnomaly;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.repository.TemperatureAnomalyRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AnomalyStoreServiceTest {

    private final TemperatureAnomalyRepository temperatureAnomalyRepository = mock(TemperatureAnomalyRepository.class);
    private final AnomalyStoreService anomalyStoreService = new AnomalyStoreService(temperatureAnomalyRepository);

    @Test
    void storeMeasurementAsAnomalyFromNumberOfMeasurementTest() {
        // given
        Instant time = Instant.now();
        TemperatureMeasurement measurement = new TemperatureMeasurement(20, time, 2, 1);
        measurement.getIsAnomaly().set(true);
        when(temperatureAnomalyRepository.save(any(TemperatureAnomaly.class))).thenAnswer(anomaly -> Mono.just(anomaly.getArguments()[0]));
        // when
        TemperatureAnomaly result = anomalyStoreService.storeMeasurementAsAnomaly(measurement, AnomalyDetectionStrategy.NUMBER_OF_MEASUREMENTS);

        // then
        verify(temperatureAnomalyRepository).save(any(TemperatureAnomaly.class));
        assertThat(result.getTimestamp()).isEqualTo(time);
        assertThat(result.getTemperature()).isEqualTo(20);
        assertThat(result.getSensorId()).isEqualTo(1);
        assertThat(result.getRoomId()).isEqualTo(2);
        assertThat(result.getAnomalyDetectionStrategy()).isEqualTo(AnomalyDetectionStrategy.NUMBER_OF_MEASUREMENTS);
    }

    @Test
    void storeMeasurementAsAnomalyFromTimeFrameTest() {
        // given
        Instant time = Instant.now();
        TemperatureMeasurement measurement = new TemperatureMeasurement(20, time, 2, 1);
        measurement.getIsAnomaly().set(true);
        when(temperatureAnomalyRepository.save(any(TemperatureAnomaly.class))).thenAnswer(anomaly -> Mono.just(anomaly.getArguments()[0]));
        // when
        TemperatureAnomaly result = anomalyStoreService.storeMeasurementAsAnomaly(measurement, AnomalyDetectionStrategy.TIME_FRAME_BASED);

        // then
        verify(temperatureAnomalyRepository).save(any(TemperatureAnomaly.class));
        assertThat(result.getTimestamp()).isEqualTo(time);
        assertThat(result.getTemperature()).isEqualTo(20);
        assertThat(result.getSensorId()).isEqualTo(1);
        assertThat(result.getRoomId()).isEqualTo(2);
        assertThat(result.getAnomalyDetectionStrategy()).isEqualTo(AnomalyDetectionStrategy.TIME_FRAME_BASED);
    }
}
