package com.ks.kontakt.recruitment.temperatureAnomalyDetector.detector;

import com.ks.kontakt.model.TemperatureMeasurement;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.detector.algorithms.AnomalyDetectionMeasurementsWindow;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.detector.algorithms.AnomalyDetectionTimeBasedWindow;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TemperatureAnomalyDetectorTest {

    private final AnomalyDetectionTimeBasedWindow anomalyDetectionTimeBasedWindow =
            mock(AnomalyDetectionTimeBasedWindow.class);
    private final AnomalyDetectionMeasurementsWindow anomalyDetectionMeasurementsWindow =
            mock(AnomalyDetectionMeasurementsWindow.class);
    private final TemperatureAnomalyDetector temperatureAnomalyDetector =
            new TemperatureAnomalyDetector(anomalyDetectionTimeBasedWindow, anomalyDetectionMeasurementsWindow);

    @Test
    void listenMeasurements_passesMeasurementToBothDetectors() {
        TemperatureMeasurement temperatureMeasurement = new TemperatureMeasurement();
        temperatureAnomalyDetector.listenMeasurements(temperatureMeasurement);
        verify(anomalyDetectionTimeBasedWindow, times(1)).addMeasurement(temperatureMeasurement);
        verify(anomalyDetectionMeasurementsWindow, times(1)).addMeasurement(temperatureMeasurement);
    }
}
