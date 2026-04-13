# 💳 Microsserviço de Pagamento




![Java](https://img.shields.io/badge/Java-17-000?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-000?style=for-the-badge&logo=springboot)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-000?style=for-the-badge&logo=rabbitmq)
![Docker](https://img.shields.io/badge/Docker-000?style=for-the-badge&logo=docker)
![Maven](https://img.shields.io/badge/Maven-000?style=for-the-badge&logo=apachemaven)
![MySQL](https://img.shields.io/badge/MySQL-000?style=for-the-badge&logo=mysql)

---
Este projeto é um microsserviço responsável pelo processamento de pagamentos dentro de uma arquitetura de microsserviços.

Ele faz parte de um ecossistema distribuído, sendo responsável por receber, validar e processar pagamentos, além de se comunicar com outros serviços através de mensageria.

---

## 🚀 Tecnologias utilizadas

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- RabbitMQ (mensageria)
- Docker
- Maven

---

## 🧠 Arquitetura

O projeto segue o padrão de **arquitetura de microsserviços**, onde cada serviço possui responsabilidade única.

### 📌 Responsabilidades do serviço de pagamento:

- Processar pagamentos
- Validar dados de pagamento
- Atualizar status de pagamento
- Publicar eventos (ex: pagamento aprovado/rejeitado)
- Consumir eventos de outros serviços (ex: pedido criado)

---

## 🔄 Comunicação entre serviços

A comunicação é feita de forma assíncrona utilizando **RabbitMQ**, permitindo:

- Baixo acoplamento entre serviços
- Maior escalabilidade
- Tolerância a falhas

Exemplo de fluxo:

1. Pedido é criado no serviço de pedidos
2. Evento é enviado para fila
3. Serviço de pagamento consome o evento
4. Processa o pagamento
5. Retorna evento com status (APROVADO / REJEITADO)

---

## 📂 Estrutura do projeto
