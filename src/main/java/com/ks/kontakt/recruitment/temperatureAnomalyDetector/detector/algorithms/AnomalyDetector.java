package com.ks.kontakt.recruitment.temperatureAnomalyDetector.detector.algorithms;

import com.ks.kontakt.model.TemperatureMeasurement;

public interface AnomalyDetector {
    void addMeasurement(TemperatureMeasurement temperatureMeasurement);
}
