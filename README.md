# ☕ Sistema de Gestão Comercial (SGC) - Café Arabia

![Java](https://img.shields.io/badge/Java-21+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3+-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

Bem-vindo ao repositório do **Sistema de Gestão Comercial (SGC)**, uma API RESTful desenvolvida para automatizar e otimizar a operação comercial da empresa de venda e distribuição de cafés **Café Arabia** (incluindo as linhas Continental e Camel).

Projeto acadêmico desenvolvido no **UNICEUB - Centro Universitário de Brasília** (Brasília - DF, 2026).

---

## 📖 Sobre o Projeto

Com o alto volume de entrada e saída de produtos (grãos especiais, cápsulas, pacotes moídos), métodos manuais de controle geram inconsistências entre o registro de vendas e a disponibilidade real nas prateleiras. 

Este projeto visa resolver essa problemática implementando um SGC robusto que automatiza integralmente a operação comercial, focando no controle de estoque automático, prevenção de falhas operacionais e fornecimento de ferramentas de análise de negócio para a tomada de decisão.

![##Diagrama de Caso de Uso](https://github.com/DanielCScience/Sistema-de-Gest-o-Comercial-/blob/main/Captura%20de%20tela%202026-04-11%20192419.png?raw=true)
![##Diagrama de Classe](https://github.com/DanielCScience/Sistema-de-Gest-o-Comercial-/blob/main/Captura%20de%20tela%202026-04-11%20192811.png?raw=true)
![##Diagrama de Domínio](https://github.com/DanielCScience/Sistema-de-Gest-o-Comercial-/blob/main/Captura%20de%20tela%202026-04-11%20192956.png?raw=true)

### 🎯 Objetivos
* **Controle de Estoque Automático:** Baixa imediata de produtos após a venda e prevenção de pedidos com saldo insuficiente.
* **Cadastro Unificado:** Sistema confiável para gestão de clientes e produtos.
* **Relatórios Gerenciais:** Geração de relatórios de vendas por período e por cliente.
* **Segurança:** Autenticação via token JWT, restringindo acesso a usuários autorizados (funcionários e administradores).

---

## 🚀 Tecnologias e Arquitetura

O sistema foi desenhado sob uma arquitetura de três camadas (Apresentação, Serviço e Persistência) baseada no padrão **API RESTful**, garantindo separação de responsabilidades e escalabilidade.

### Stack Tecnológico
| Componente | Tecnologia | Justificativa |
| :--- | :--- | :--- |
| **Linguagem** | Java 21+ (Virtual Threads) | Alta performance e concorrência para manipulação de requisições. |
| **Framework** | Spring Boot 3+ | Configuração e desenvolvimento rápido de aplicações robustas. |
| **Banco de Dados** | MySQL | Banco relacional maduro, ideal para o modelo transacional. |
| **ORM** | Spring Data JPA / Hibernate | Abstração e padronização da interação com o banco de dados. |
| **Segurança** | Spring Security / JWT / BCrypt | Controle de acesso restrito e armazenamento seguro de senhas. |

### Padrões de Projeto Aplicados
* **Estruturais/Criação:** Dependency Injection (DI) e Data Transfer Object (DTO).
* **Comportamentais/Arquiteturais:** Repository Pattern, Service Layer Pattern e Filter Pattern (Interceptadores JWT).

---

## ⚙️ Funcionalidades (Requisitos Funcionais)

### 👥 Gestão de Clientes
* **`RF01`** Cadastrar Cliente (Nome, CPF, endereço, telefone, e-mail).
* **`RF02`** Visualizar Cliente (Busca por nome ou CPF).
* **`RF03`** Alterar Cliente (Edição de perfil, exceto CPF).
* **`RF04`** Excluir Cliente (Apenas se não houver histórico de vendas).

### 📦 Gestão de Produtos e Estoque
* **`RF05`** Cadastrar Produto (Nome, descrição, preço unitário e saldo inicial).
* **`RF06`** Visualizar Produto (Catálogo, preços e saldo em estoque).
* **`RF07`** Alterar Produto (Atualização de preço, descrição ou ajuste manual de estoque).
* **`RF08`** Excluir Produto do catálogo.

### 🛒 Gestão de Vendas e Pedidos
* **`RF09`** Registrar Pedido (Vínculo de funcionário, produtos e quantidades).
* **`RF10`** Calcular Valor Total (Cálculo automático no fechamento da venda).
* **`RF11`** Registrar Data e Hora (Geração automática no momento do pedido).
* **`RF12`** Visualizar Pedido (Consulta de itens, funcionário e valor).
* **`RF13`** Alterar Pedido (Edição de itens antes da finalização).
* **`RF14`** Cancelar Pedido (Estorno e recomposição automática do estoque).

### 📊 Relatórios e Dashboards (Acesso Restrito ADMIN)
* **`RF15`** Gerar Gráfico de Vendas Anuais.
* **`RF16`** Gerar Relatório de Vendas por Período (Volume financeiro e quantidade).
* **`RF17`** Gerar Histórico de Compras por Cliente.
* **`RF18`** Limitar Acesso Restrito (Exclusão de dados e relatórios anuais exclusivos para perfil ADMIN).

---

## 🔒 Requisitos Não Funcionais

* **`RNF01`** Desenvolvimento em Java 21+ com Spring Boot 3+ e Virtual Threads.
* **`RNF02`** Persistência relacional em MySQL com JPA/Hibernate.
* **`RNF03`** Comunicação estritamente via JSON (API RESTful).
* **`RNF04`** Versionamento remoto via GitHub com boas práticas de branch/commits.
* **`RNF05`** Criptografia de senhas utilizando hash BCrypt.
* **`RNF06`** Controle de sessão e autenticação via Tokens JWT (expiração configurável).

---

## 🛠️ Como Executar o Projeto
### Pré-requisitos
- Ter o **Java 17** instalado na máquina.
- Ter o **MySQL** instalado e rodando na porta local padrão `3306`.
### Passo a Passo
1. Clone este repositório para a sua máquina:
   ```bash
   git clone https://github.com/SeuUsuario/NomeDoRepositorio.git
   ```
2. Abra a pasta do projeto e edite as credenciais do banco de dados no arquivo `src/main/resources/application.properties` caso seja diferente da sua máquina:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=root
   ```
3. O Spring Boot com JPA está configurado como `update`, portanto ele irá **criar todas as tabelas do banco automaticamente** no momento do primeiro Run.
4. Abra a sua IDE favorita e execute a classe `VendasApplication.java`.
---
## 📖 Como Testar (Documentação Swagger)
Com a API rodando, você não precisa de plataformas como Postman. O sistema conta com o **Swagger UI**, uma tela limpa e intuitiva para testar todos os endpoints, já adaptada para receber seu Token Bearer.
1. Acesse pelo navegador: `http://localhost:8080/swagger-ui.html`
2. Utilize o endpoint `POST /api/v1/funcionarios` para cadastrar um usuário ADMIN (O cadastro é público e as senhas serão automaticamente criptografadas).
3. Vá até a rota de autenticação (`POST /api/v1/auth/login`) informando o e-mail e senha que você acabou de criar e copie o token gerado.
4. Volte ao topo da página do Swagger, clique no ícone **Authorize 🔒**, e cole o token.
5. Sucesso! Você já pode realizar requisições privadas com segurança!
---

## 👥 Equipe e Créditos

* **Desenvolvedores (Prestadores):** Daniel Lourenço Lustosa | Vinicius Gabriel Couto Machado
* **Cliente:** Aziz Abdalla Jarjour
* **Orientador:** Prof. Felippe Pires Ferreira
* **Instituição:** UNICEUB - Centro Universitário de Brasília
