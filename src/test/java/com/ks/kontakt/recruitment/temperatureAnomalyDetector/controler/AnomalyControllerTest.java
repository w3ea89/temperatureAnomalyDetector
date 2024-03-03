package com.ks.kontakt.recruitment.temperatureAnomalyDetector.controler;


import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.AnomalyDetectionStrategy;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.model.TemperatureAnomaly;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.repository.TemperatureAnomalyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(AnomalyController.class)
public class AnomalyControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TemperatureAnomalyRepository repository;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void listAllAnomaliesTest() {
        when(repository.findAll()).thenReturn(Flux.fromIterable(List.of(
                createAnomaly(AnomalyDetectionStrategy.TIME_FRAME_BASED),
                createAnomaly(AnomalyDetectionStrategy.TIME_FRAME_BASED),
                createAnomaly(AnomalyDetectionStrategy.NUMBER_OF_MEASUREMENTS)
        )));

        webTestClient.get().uri("/anomalies")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TemperatureAnomaly.class)
                .hasSize(3);

    }


    @Test
    public void listAllTimeBasedAnomaliesTest() {
        when(repository.findAll()).thenReturn(Flux.fromIterable(List.of(
                createAnomaly(AnomalyDetectionStrategy.TIME_FRAME_BASED),
                createAnomaly(AnomalyDetectionStrategy.TIME_FRAME_BASED),
                createAnomaly(AnomalyDetectionStrategy.NUMBER_OF_MEASUREMENTS)
        )));

        webTestClient.get().uri("/anomalies/time")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TemperatureAnomaly.class)
                .hasSize(2);
    }

    @Test
    public void listAllMeasurementBasedAnomaliesTest() {
        when(repository.findAll()).thenReturn(Flux.fromIterable(List.of(
                createAnomaly(AnomalyDetectionStrategy.TIME_FRAME_BASED),
                createAnomaly(AnomalyDetectionStrategy.NUMBER_OF_MEASUREMENTS)
        )));

        webTestClient.get().uri("/anomalies/measurement")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TemperatureAnomaly.class)
                .hasSize(1);
    }

    private TemperatureAnomaly createAnomaly(AnomalyDetectionStrategy detectionStrategy) {
        TemperatureAnomaly anomaly = new TemperatureAnomaly();
        anomaly.setAnomalyDetectionStrategy(detectionStrategy);
        return anomaly;
    }
}
