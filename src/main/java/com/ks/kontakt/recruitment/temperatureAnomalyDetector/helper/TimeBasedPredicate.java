package com.ks.kontakt.recruitment.temperatureAnomalyDetector.helper;

import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.AnomalyDetectionStrategy;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.TemperatureAnomaly;

import java.util.function.Predicate;

public class TimeBasedPredicate implements Predicate<TemperatureAnomaly> {
    @Override
    public boolean test(TemperatureAnomaly temperatureAnomaly) {
        return temperatureAnomaly.getAnomalyDetectionStrategy().equals(AnomalyDetectionStrategy.TIME_FRAME_BASED);
    }
}
