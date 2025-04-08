package com.desafio.revendaservice.integration;

import com.desafio.revendaservice.application.client.PedidoClient;
import com.desafio.revendaservice.application.dto.PedidoResponse;
import com.desafio.revendaservice.domain.model.ItemPedido;
import com.desafio.revendaservice.domain.model.PedidoPendente;
import com.desafio.revendaservice.domain.model.PedidoRevenda;
import com.desafio.revendaservice.infrastructure.repository.PedidoPendenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(PedidoPendenteIntegrationTest.FakePedidoClientConfig.class)
class PedidoPendenteIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
        .withDatabaseName("reprocess_test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private PedidoPendenteRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/pedidos-pendentes/reprocessar";
        repository.deleteAll();

        PedidoPendente pendente = PedidoPendente.builder()
            .pedidoOriginalId(42L)
            .itens(List.of(ItemPedido.builder().produto("Cerveja").quantidade(1200).build()))
            .build();

        repository.save(pendente);
    }

    @Test
    void deveReprocessarPedidosPendentes() {
        ResponseEntity<PedidoPendente[]> response = restTemplate.postForEntity(
            baseUrl, null, PedidoPendente[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();

        // Confirma que o pedido pendente foi removido após sucesso
        List<PedidoPendente> pendentesRestantes = repository.findAll();
        assertThat(pendentesRestantes).isEmpty();
    }

    @TestConfiguration
    static class FakePedidoClientConfig {

        @Bean
        @Primary
        public PedidoClient fakePedidoClient() {
            return new PedidoClient() {
                @Override
                public PedidoResponse emitir(PedidoRevenda pedido) {
                    return new PedidoResponse("AMBEV-SUCESSO", null);
                }
            };
        }
    }
}
