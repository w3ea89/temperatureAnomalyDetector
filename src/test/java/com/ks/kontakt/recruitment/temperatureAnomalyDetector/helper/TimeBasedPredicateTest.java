package com.ks.kontakt.recruitment.temperatureAnomalyDetector.helper;

import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.AnomalyDetectionStrategy;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.TemperatureAnomaly;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TimeBasedPredicateTest {

    private final TimeBasedPredicate predicate = new TimeBasedPredicate();

    @Test
    void testWithTimeFrameBasedAnomaly_returnTrue() {
        TemperatureAnomaly timeFrameBasedAnomaly = new TemperatureAnomaly();
        timeFrameBasedAnomaly.setAnomalyDetectionStrategy(AnomalyDetectionStrategy.TIME_FRAME_BASED);
        assertThat(predicate.test(timeFrameBasedAnomaly)).isTrue();
    }

    @Test
    void testWithNonTimeFrameBasedAnomaly_returnsFalse() {
        TemperatureAnomaly timeFrameBasedAnomaly = new TemperatureAnomaly();
        timeFrameBasedAnomaly.setAnomalyDetectionStrategy(AnomalyDetectionStrategy.NUMBER_OF_MEASUREMENTS);
        assertThat(predicate.test(timeFrameBasedAnomaly)).isFalse();
    }
}
