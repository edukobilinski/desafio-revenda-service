# Desafio Revenda Service 🚛🍻

API responsável por simular o fluxo de pedidos entre revendas e a Ambev, com integração resiliente e persistência de pedidos pendentes.

## 🧱 Tecnologias
- Java 21
- Spring Boot 3
- PostgreSQL + JPA
- Resilience4j (retry + fallback)
- Swagger (springdoc-openapi)
- Testcontainers + JUnit 5
- Docker + Docker Compose
- GitHub Actions (CI/CD)

## 📦 Como executar

```bash
docker-compose up --build
```

A aplicação estará disponível em: `http://localhost:8080`

Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 📬 Endpoints principais

- `POST /revendas` - Cadastrar revenda
- `POST /revendas/{id}/pedidos` - Receber pedido do cliente
- `GET /actuator/health` - Health check

## 📄 Exemplo de payload (pedido)

```json
{
  "clienteId": "cliente123",
  "itens": [
    { "produto": "Cerveja", "quantidade": 1000 }
  ]
}
```

## 🧪 Executar testes

```bash
./mvnw test
```

## 🚀 CI/CD com GitHub Actions

- Build Maven
- Execução de testes
- Validação de Docker build

---
Feito com 💙 para o desafio técnico.