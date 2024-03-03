package com.ks.kontakt.recruitment.temperatureAnomalyDetector.e2e;

import com.ks.kontakt.model.TemperatureMeasurement;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.configuration.TestConfig;
import com.ks.kontakt.recruitment.temperatureAnomalyDetector.repository.TemperatureAnomalyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class AnomalyDetectionIT {
    @Autowired
    TemperatureAnomalyRepository temperatureAnomalyRepository;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private KafkaTemplate<String, TemperatureMeasurement> kafkaTemplate;

    @BeforeEach
    void setUp() {
        temperatureAnomalyRepository.deleteAll().block();
    }

    @Test
    public void testTemperatureAnomalyDetectionFlowForMeasurementAlgorithm() throws InterruptedException {
        Instant time = Instant.now();
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time, 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(100), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(100), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(100), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(100), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(100), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(100), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(100), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(100), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(100), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(100), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(30.0, time.plusMillis(100), 1, 1));
        Thread.sleep(1000);
        webTestClient.get().uri("/anomalies")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].temperature").isEqualTo(30.0);
    }

    @Test
    public void testTemperatureAnomalyDetectionFlowForTimeBasedAlgorithm() throws InterruptedException {
        Instant time = Instant.now();
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time, 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(100), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(40.0, time.plusMillis(10), 1, 1));
        Thread.sleep(1000);
        webTestClient.get().uri("/anomalies")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].temperature").isEqualTo(40.0);
    }

    @Test
    public void testTemperatureAnomalyDetectionFlowForNoDetection() throws InterruptedException {
        Instant time = Instant.now();
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time, 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(100), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(21.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(21.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(1), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(22.0, time.plusMillis(10), 1, 1));
        kafkaTemplate.send("temperatureMeasurements", "Sensor1", new TemperatureMeasurement(21.0, time.plusMillis(10), 1, 1));
        Thread.sleep(1000);
        webTestClient.get().uri("/anomalies")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isEmpty();

    }
}
