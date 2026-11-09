# 🔥 FogoData — Monitoramento e Análise de Focos de Incêndio

Projeto acadêmico de **Atividades Práticas Supervisionadas (APS)** do curso de **Ciência da Computação da Universidade Paulista (UNIP)**, desenvolvido para a disciplina de **Estrutura de Dados**.

O **FogoData** é uma aplicação web que permite carregar, visualizar e ordenar registros de focos de incêndio do **Espírito Santo**, a partir de arquivos CSV referentes aos anos de **2023 e 2024**. O projeto também compara o desempenho de algoritmos de ordenação, considerando operações realizadas e tempo de execução.

## 🎯 Objetivos

- Ler e validar registros de focos de incêndio provenientes de arquivos CSV.
- Exibir indicadores sobre o carregamento e a qualidade dos dados.
- Permitir a consulta dos registros por meio de uma interface web.
- Aplicar diferentes algoritmos de ordenação aos dados.
- Comparar algoritmos por número de operações e tempo de execução.
- Integrar conceitos de Estrutura de Dados, Java e desenvolvimento web.

## 🛠️ Tecnologias utilizadas

- **Java 21**
- **Spring Boot 4.1.1**
- **Spring Web MVC**
- **Thymeleaf**
- **HTML5, CSS3 e JavaScript**
- **Maven / Maven Wrapper**
- **Arquivos CSV** como fonte de dados

## 🧮 Algoritmos implementados

O módulo de ordenação inclui:

1. **Bubble Sort**
2. **Selection Sort**
3. **Insertion Sort**
4. **Merge Sort**
5. **Quick Sort**

As análises apresentam métricas como **comparações**, **substituições/escritas** e **tempo de execução**, conforme a instrumentação de cada algoritmo no projeto.

> **Observação:** os resultados de desempenho variam conforme os dados, o tamanho da amostra e o ambiente de execução.

## 🖥️ Funcionalidades

### Visão geral (`/`)

- Indicadores de focos válidos, linhas lidas e linhas inválidas.
- Tempo de leitura dos arquivos.
- Informações sobre os arquivos processados.
- Opção para recarregar os dados.

### Dados carregados (`/dados`)

- Tabela com informações dos focos de incêndio.
- Consulta de município, data e hora, estado, bioma, coordenadas e satélite.
- Filtros de ano, bioma e busca por município na interface.


### Ordenação e resultados (`/resultados`)

- Escolha do algoritmo de ordenação.
- Seleção do critério de ordenação.
- Definição de limite de registros para testes.
- Exibição de métricas de desempenho.
- Comparação entre os cinco algoritmos.
- Visualização de uma amostra dos dados ordenados.

## 📁 Estrutura do projeto

```text
aps/
├── dados/
│   ├── ES 2023.csv
│   └── ES 2024.csv
├── src/
│   ├── main/
│   │   ├── java/com/unip/aps/
│   │   │   ├── controller/
│   │   │   ├── exception/
│   │   │   ├── model/
│   │   │   ├── ordenacao/
│   │   │   ├── service/
│   │   │   └── ApsApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   └── Style.css
│   │       ├── templates/
│   │       │   ├── index.html
│   │       │   ├── dados.html
│   │       │   └── resultados.html
│   │       └── application.properties
│   └── test/
├── mvnw
├── mvnw.cmd
└── pom.xml
```

## 🚀 Como executar

### Pré-requisitos

- **JDK 21** instalado e configurado.
- Conexão à internet na primeira execução, caso o Maven precise baixar dependências.

### Passo a passo

1. Clone o repositório ou extraia os arquivos do projeto.
2. Abra a pasta `aps` no **IntelliJ IDEA** ou **VS Code**.
3. Confirme que os arquivos CSV estão na pasta `dados/`.
4. No terminal, na raiz do projeto, execute:

**Windows (PowerShell ou Prompt de Comando):**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux/macOS:**

```bash
./mvnw spring-boot:run
```

5. Acesse no navegador:

**http://localhost:8080**

Também é possível executar a classe `ApsApplication.java` diretamente pela IDE.

## 🧭 Rotas da aplicação

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/` | Painel inicial e indicadores de carga |
| `GET` | `/dados` | Consulta dos registros carregados |
| `GET` | `/resultados` | Ordenação e comparação de algoritmos |

## 📊 Fonte dos dados

Os arquivos `ES 2023.csv` e `ES 2024.csv` contêm registros de focos de incêndio no Espírito Santo. A interface do projeto identifica a base como relacionada ao **INPE**.

Os arquivos são processados pela camada de serviços da aplicação. Registros inválidos podem ser contabilizados separadamente durante a leitura.

## 🧱 Organização da aplicação

O projeto utiliza a arquitetura **MVC (Model–View–Controller)**:

- **Model:** representa focos de incêndio e resultados de carga e ordenação.
- **View:** páginas Thymeleaf estilizadas com CSS e comportamentos em JavaScript.
- **Controller:** recebe requisições, aciona serviços e disponibiliza dados para as páginas.
- **Service:** realiza leitura dos CSVs, carregamento e execução das ordenações.
- **Ordenação:** concentra as implementações dos algoritmos.

## 🧪 Testes e observações

Para validar o sistema, recomenda-se:

- Conferir o carregamento dos arquivos de 2023 e 2024.
- Verificar os indicadores de registros válidos e inválidos.
- Testar os filtros e a navegação entre páginas.
- Executar todos os algoritmos com a mesma quantidade de registros.
- Comparar métricas de desempenho sem assumir que tempos serão idênticos em diferentes máquinas.

**Limitação conhecida:** renderizar uma quantidade muito grande de registros na página `/dados` pode afetar o desempenho do navegador. Uma evolução possível é utilizar filtros e paginação no backend.

## 🎓 Informações acadêmicas

- **Instituição:** Universidade Paulista (UNIP)
- **Curso:** Ciência da Computação
- **Atividade:** Atividades Práticas Supervisionadas (APS)
- **Disciplina:** Estrutura de Dados
- **Projeto:** FogoData — Monitoramento e Análise de Focos de Incêndio

### Integrantes

- Kauã da Silva Andrade | H403DG8
- Ricardo De Sousa Matos Junior | R871CJ1
- Eduardo Leite Salema| R660CC3
Henrique Lucca Costa | H77JCB3
---

**Desenvolvido para fins acadêmicos — UNIP.**
