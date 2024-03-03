package com.ks.kontakt.recruitment.temperatureAnomalyDetector.repository;


import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.TemperatureAnomaly;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TemperatureAnomalyRepository extends ReactiveMongoRepository<TemperatureAnomaly, Long> {
}