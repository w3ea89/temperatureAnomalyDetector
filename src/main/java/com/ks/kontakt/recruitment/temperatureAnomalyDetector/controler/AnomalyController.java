package com.ks.kontakt.recruitment.temperatureAnomalyDetector.controler;

import com.ks.kontakt.recruitment.temperatureAnomalyDetector.helper.MeasurementBasedPredicate;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.helper.TimeBasedPredicate;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.TemperatureAnomaly;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.repository.TemperatureAnomalyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.function.Predicate;

@RestController
public class AnomalyController {
    private static final Predicate<TemperatureAnomaly> TIME_BASED = new TimeBasedPredicate();
    private static final Predicate<TemperatureAnomaly> MEASUREMENT_BASED = new MeasurementBasedPredicate();
    @Autowired
    private TemperatureAnomalyRepository repository;

    @GetMapping(value = "/anomalies")
    public Flux<TemperatureAnomaly> listAllAnomalies() {
        return repository.findAll();
    }


    @GetMapping(value = "/anomalies/time")
    public Flux<TemperatureAnomaly> listAllTimeBasedAnomalies() {
        return repository.findAll()
                .filter(TIME_BASED);
    }

    @GetMapping(value = "/anomalies/measurement")
    public Flux<TemperatureAnomaly> listAllMeasurementBasedAnomalies() {
        return repository.findAll()
                .filter(MEASUREMENT_BASED);
    }
}
