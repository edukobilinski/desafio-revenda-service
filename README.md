# 🛒 Revenda Service - Desafio Técnico

Este projeto simula o fluxo de pedidos de bebidas realizados por revendas da Ambev. Foi desenvolvido como parte de um desafio técnico, utilizando **Java 21**, **Spring Boot 3.2.5**, **PostgreSQL**, **Swagger/OpenAPI**, **Resilience4j** e **Testcontainers**.

---

## 🚀 Funcionalidades

- Cadastro completo de revendas (CNPJ, contatos, endereços, etc.)
- Recebimento de pedidos feitos por clientes da revenda
- Emissão de pedido para a Ambev (com retry e fallback persistente)
- Persistência de pedidos pendentes para reprocessamento posterior
- Reprocessamento manual de pedidos pendentes
- Listagem de pedidos por revenda
- Swagger UI para documentação interativa
- Testes automatizados com JUnit e Testcontainers
- Pipeline CI/CD com GitHub Actions

---

## 📦 Tecnologias

- Java 21
- Spring Boot 3.2.5
- Spring Data JPA
- PostgreSQL
- Resilience4j (Retry + Fallback)
- Jackson (JSR-310)
- Swagger (springdoc-openapi)
- Docker / Docker Compose
- Testcontainers
- Maven

---

## 📄 Documentação da API

📍 Acesse:

```
http://localhost:8080/swagger-ui.html
```

Documentação gerada automaticamente com **Springdoc OpenAPI**.

---

## 🔧 Como executar localmente

### ▶️ Com Maven

```bash
./mvnw clean install
./mvnw spring-boot:run
```

### 🐳 Com Docker Compose

```bash
docker-compose up --build
```

---

## 🔁 Endpoints principais

| Método | Endpoint                         | Descrição                                 |
|--------|----------------------------------|-------------------------------------------|
| POST   | `/revendas`                      | Cadastra nova revenda                     |
| GET    | `/revendas`                      | Lista todas as revendas                   |
| GET    | `/revendas/{id}`                 | Consulta revenda por ID                   |
| POST   | `/revendas/{id}/pedidos`         | Recebe pedido feito por cliente           |
| GET    | `/revendas/{id}/pedidos`         | Lista pedidos da revenda                  |
| POST   | `/pedidos-pendentes/reprocessar` | Reprocessa pedidos pendentes              |

---

## 🧪 Exemplos de Payloads

### ✅ Cadastro de Revenda (POST `/revendas`)

```json
{
  "cnpj": "12.345.678/0001-90",
  "razaoSocial": "Distribuidora Brasil Ltda",
  "nomeFantasia": "Brasil Bebidas",
  "email": "contato@brasilbebidas.com",
  "telefones": ["(11) 98765-4321"],
  "contatos": [
    { "nome": "João Silva", "principal": true }
  ],
  "enderecos": [
    {
      "rua": "Rua das Bebidas",
      "numero": "100",
      "cidade": "São Paulo",
      "estado": "SP",
      "cep": "01000-000"
    }
  ]
}
```

### ✅ Pedido do Cliente para Revenda (POST `/revendas/{id}/pedidos`)

```json
{
  "clienteId": 456,
  "itens": [
    { "produto": "Cerveja", "quantidade": 800 },
    { "produto": "Refrigerante", "quantidade": 300 }
  ]
}
```

### 🔁 Reprocessamento de pedidos pendentes

```http
POST /pedidos-pendentes/reprocessar
```

---

## 📂 Estrutura do Projeto (DDD)

```
com.desafio.revendaservice
├── api
│   ├── controller         # Camada de entrada (REST)
│   └── exception          # Tratamento global de erros
├── application
│   ├── service            # Regras de negócio
│   ├── client             # Integração externa (Ambev)
│   └── dto                # Objetos de transporte
├── domain
│   └── model              # Entidades de domínio
├── infrastructure
│   └── repository         # Repositórios JPA
└── RevendaServiceApplication.java
```

---

## ⚙️ CI/CD com GitHub Actions

A aplicação conta com um pipeline configurado para:

- Build
- Execução de testes
- Validação de Docker build

---
Feito com 💙 para o desafio técnico.