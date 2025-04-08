package com.desafio.revendaservice.integration;

import com.desafio.revendaservice.application.dto.ContatoDto;
import com.desafio.revendaservice.application.dto.EnderecoDto;
import com.desafio.revendaservice.application.dto.RevendaDto;
import com.desafio.revendaservice.domain.model.Revenda;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
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
class RevendaIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
        .withDatabaseName("revenda_full")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void config(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void deveCadastrarEListarRevendas() {
        String baseUrl = "http://localhost:" + port + "/revendas";

        RevendaDto dto = new RevendaDto(
            "88.888.888/0001-88",
            "Revenda de Integração",
            "Fantasia Test",
            "test@integra.com",
            List.of("11988887777"),
            List.of(new ContatoDto("Contato 1", true)),
            List.of(new EnderecoDto("Rua Teste", "456", "Cidade", "SP", "00000-000"))
        );

        // POST /revendas
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RevendaDto> request = new HttpEntity<>(dto, headers);

        ResponseEntity<Revenda> postResponse = restTemplate.postForEntity(baseUrl, request, Revenda.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Revenda created = postResponse.getBody();

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();

        // GET /revendas/{id}
        ResponseEntity<Revenda> getResponse = restTemplate.getForEntity(baseUrl + "/" + created.getId(), Revenda.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Revenda fetched = getResponse.getBody();

        assertThat(fetched).isNotNull();
        assertThat(fetched.getEmail()).isEqualTo("test@integra.com");
    }
}
