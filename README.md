# API de Produtos com Middleware Java

Aplicacao de exemplo desenvolvida em Java para demonstrar o uso de um middleware proprio baseado em anotacoes, reflection e comunicacao HTTP sobre sockets. Esta branch apresenta uma API de gestao de produtos com operacoes de consulta, criacao, atualizacao, remocao e filtro por preco.

## Visao Geral

O projeto foi construído como uma aplicacao cliente do middleware desenvolvido no repositório principal. Em vez de usar frameworks como Spring Boot, a API expõe seus endpoints a partir de anotacoes customizadas do middleware, o que destaca de forma didatica como uma camada de distribuicao pode registrar rotas, interpretar parametros e invocar metodos Java dinamicamente.

## O que esta branch entrega

- listagem de produtos
- busca de produto por ID
- filtro por preco maximo via query param
- criacao de novos produtos com payload JSON
- atualizacao de produtos existentes
- exclusao de produtos por ID
- inicializacao simples da API pela classe `Main`

## Arquitetura da aplicacao

### `Main`
Responsavel por iniciar a API e subir o middleware na porta `8080`, apontando para o pacote base `dist.example`.

### `ProdutoController`
Camada de exposicao HTTP. Define os endpoints com anotacoes como `@RemoteObject`, `@MethodMapping`, `@Path`, `@Param` e `@RequestBody`.

### `ProdutoService`
Camada de regra de negocio e armazenamento em memoria. Usa `ConcurrentHashMap` para manter os produtos e inicializa alguns dados de exemplo para facilitar testes.

### `Produto`
Entidade principal da aplicacao, contendo `id`, `nome`, `preco` e `estoque`.

### `ProdutoDTO`
Objeto de transferencia usado para receber os dados enviados nas operacoes de criacao e atualizacao.

## Tecnologias e conceitos aplicados

- Java 17
- Maven
- Middleware Java customizado
- HTTP + JSON
- Anotacoes customizadas
- Reflection
- CRUD em memoria
- Concorrencia com `ConcurrentHashMap`

## Endpoints disponiveis

### Buscar todos os produtos

```http
GET /produtos
```

### Buscar produto por ID

```http
GET /produtos/{id}
```

### Filtrar por preco maximo

```http
GET /produtos/filtrar?precoMax=1000
```

### Criar produto

```http
POST /produtos
Content-Type: application/json
```

Payload:

```json
{
  "nome": "Teclado Mecanico",
  "preco": 350.0,
  "estoque": 50
}
```

### Atualizar produto

```http
PUT /produtos/{id}
Content-Type: application/json
```

Payload:

```json
{
  "nome": "Teclado Mecanico RGB",
  "preco": 420.0,
  "estoque": 45
}
```

### Deletar produto

```http
DELETE /produtos/{id}
```

## Como executar

### Pre-requisitos

- Java 17
- Maven
- dependencia `dist.middleware:middleware:0.0.1-BETA` disponivel no ambiente local ou em repositorio acessivel

### Executando com Maven

```bash
mvn clean package
java -jar target/projeto-produtos-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Quando a aplicacao iniciar, a API ficara disponivel em:

```text
http://localhost:8080
```

## Exemplos de teste com curl

```bash
curl "http://localhost:8080/produtos"
```

```bash
curl "http://localhost:8080/produtos/ID_DO_PRODUTO"
```

```bash
curl "http://localhost:8080/produtos/filtrar?precoMax=1000"
```

```bash
curl -X POST "http://localhost:8080/produtos" ^
-H "Content-Type: application/json" ^
-d "{\"nome\":\"Teclado Mecanico\",\"preco\":350.0,\"estoque\":50}"
```

```bash
curl -X PUT "http://localhost:8080/produtos/ID_DO_PRODUTO" ^
-H "Content-Type: application/json" ^
-d "{\"nome\":\"Teclado RGB\",\"preco\":420.0,\"estoque\":45}"
```

```bash
curl -X DELETE "http://localhost:8080/produtos/ID_DO_PRODUTO"
```

## Diferenciais tecnicos

- demonstracao pratica de uma API sem framework web tradicional
- integracao direta com middleware proprio baseado em anotacoes
- separacao clara entre controller, service, model e DTO
- implementacao simples e didatica, boa para estudo e apresentacao de conceitos
- branch ideal para mostrar a aplicacao concreta do middleware em um dominio real

## Contexto do projeto

Esta branch representa a camada de aplicacao do projeto, usando o middleware desenvolvido anteriormente para expor uma API REST-like de produtos. Ela ajuda a demonstrar nao apenas a construcao do middleware em si, mas tambem sua utilizacao em um caso de uso funcional e compreensivel.
