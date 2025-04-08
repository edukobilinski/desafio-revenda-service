package com.desafio.revendaservice.integration;

import com.desafio.revendaservice.application.dto.*;
import com.desafio.revendaservice.domain.model.PedidoRevenda;
import com.desafio.revendaservice.domain.model.Revenda;
import com.desafio.revendaservice.infrastructure.repository.PedidoRevendaRepository;
import org.junit.jupiter.api.BeforeEach;
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
class PedidoRevendaIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
        .withDatabaseName("pedido_revenda_test")
        .withUsername("test")
        .withPassword("test");
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PedidoRevendaRepository pedidoRevendaRepository;
    private String baseUrl;

    @DynamicPropertySource
    static void setupProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/revendas";
        pedidoRevendaRepository.deleteAll();
    }

    @Test
    void deveCadastrarRevendaEEmitirPedidoComSucesso() {
        // Cadastrar revenda
        RevendaDto revendaDto = new RevendaDto(
            "77.777.777/0001-77",
            "Revenda Pedido",
            "Fantasia Pedido",
            "pedido@revenda.com",
            List.of("11987654321"),
            List.of(new ContatoDto("Contato Pedido", true)),
            List.of(new EnderecoDto("Av. Teste", "500", "São Paulo", "SP", "04567-000"))
        );

        ResponseEntity<Revenda> postRevenda = restTemplate.postForEntity(baseUrl, revendaDto, Revenda.class);
        assertThat(postRevenda.getStatusCode()).isEqualTo(HttpStatus.OK);
        Revenda revenda = postRevenda.getBody();
        assertThat(revenda).isNotNull();
        assertThat(revenda.getId()).isNotNull();

        // Emitir pedido
        PedidoClienteDto pedidoDto = new PedidoClienteDto(
            987L,
            List.of(new ItemPedidoDto("Guaraná", 1000))
        );

        String pedidoUrl = baseUrl + "/" + revenda.getId() + "/pedidos";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PedidoClienteDto> pedidoRequest = new HttpEntity<>(pedidoDto, headers);
        ResponseEntity<PedidoRevenda> pedidoResponse = restTemplate.postForEntity(pedidoUrl, pedidoRequest, PedidoRevenda.class);

        assertThat(pedidoResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        PedidoRevenda pedido = pedidoResponse.getBody();
        assertThat(pedido).isNotNull();
        assertThat(pedido.getClienteId()).isEqualTo("987");

        // Verificar se foi salvo no banco
        List<PedidoRevenda> pedidosSalvos = pedidoRevendaRepository.findByRevendaId(revenda.getId());
        assertThat(pedidosSalvos).hasSize(1);
    }
}
