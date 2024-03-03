package com.ks.kontakt.recruitment.temperatureAnomalyDetector.helper;

import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.AnomalyDetectionStrategy;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.TemperatureAnomaly;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MeasurementBasedPredicateTest {

    private final MeasurementBasedPredicate predicate = new MeasurementBasedPredicate();

    @Test
    void testWithTimeFrameBasedAnomaly_returnsFalse() {
        TemperatureAnomaly timeFrameBasedAnomaly = new TemperatureAnomaly();
        timeFrameBasedAnomaly.setAnomalyDetectionStrategy(AnomalyDetectionStrategy.TIME_FRAME_BASED);
        assertThat(predicate.test(timeFrameBasedAnomaly)).isFalse();
    }

    @Test
    void testWithNonTimeFrameBasedAnomaly_returnsTrue() {
        TemperatureAnomaly timeFrameBasedAnomaly = new TemperatureAnomaly();
        timeFrameBasedAnomaly.setAnomalyDetectionStrategy(AnomalyDetectionStrategy.NUMBER_OF_MEASUREMENTS);
        assertThat(predicate.test(timeFrameBasedAnomaly)).isTrue();
    }
}