# Middleware Java para Sistemas Distribuidos

Middleware academico desenvolvido em Java com foco em comunicacao distribuida, roteamento HTTP e invocacao dinamica de metodos por reflexao. O projeto funciona como uma camada intermediaria entre clientes HTTP e objetos remotos anotados, permitindo expor operacoes Java como endpoints de forma leve e organizada.

## Visao Geral

Este projeto foi construído para a disciplina de Programacao Distribuida da UFRN com o objetivo de demonstrar, na pratica, conceitos centrais de middleware:

- descoberta automatica de servicos por varredura de pacote
- registro de rotas a partir de anotacoes customizadas
- tratamento manual de requisicoes HTTP sobre sockets TCP
- desserializacao e serializacao JSON
- invocacao remota de metodos via reflection
- separacao de responsabilidades entre lookup, broker, marshalling e invocacao

## O que o projeto faz

Ao iniciar o middleware, a aplicacao:

1. escaneia um pacote base informado na inicializacao
2. encontra classes anotadas com `@RemoteObject`
3. registra metodos anotados com `@MethodMapping`
4. sobe um servidor TCP para receber requisicoes HTTP
5. resolve rota, parametros de query, variaveis de path e corpo JSON
6. invoca o metodo Java correspondente e devolve a resposta em JSON

## Arquitetura

### `MiddlewareRunner`
Ponto de entrada do framework. Inicializa o processo de descoberta, cria o broker e sobe o servidor.

### `Lookup`
Responsavel por escanear o pacote da aplicacao com a biblioteca Reflections e registrar os objetos remotos e suas rotas.

### `Route`
Converte caminhos anotados em padroes de matching, incluindo suporte a variaveis como `/produtos/{id}`.

### `Broker`
Camada central de orquestracao. Valida o metodo HTTP, encontra a rota correta, prepara os argumentos, invoca o metodo alvo e monta a resposta HTTP.

### `Marshaller`
Faz o binding entre requisicao HTTP e parametros Java usando as anotacoes:

- `@Param` para query string
- `@Path` para variaveis da URL
- `@RequestBody` para corpo da requisicao

### `Invoker`
Executa o metodo remoto via reflection sobre a instancia previamente descoberta.

### `ServerRequestHandler` e `ClientHandler`
Implementam o servidor HTTP sobre `ServerSocket`, leitura bruta da requisicao, tratamento de headers e resposta JSON ao cliente.

## Tecnologias e conceitos aplicados

- Java 24
- Maven
- Reflection API
- Sockets TCP
- HTTP parsing manual
- Gson para JSON
- Reflections para descoberta automatica de classes
- Virtual Threads para concorrencia no tratamento de conexoes

## Estrutura do projeto

```text
src/main/java/dist/middleware
|-- MiddlewareRunner.java
|-- annotations/
|-- broker/
|-- identifications/
`-- remoting/
```

## Exemplo de uso

O modelo de extensao do middleware e baseado em anotacoes. Uma aplicacao cliente pode expor um objeto remoto assim:

```java
package com.exemplo.app;

import dist.middleware.annotations.HttpMethod;
import dist.middleware.annotations.MethodMapping;
import dist.middleware.annotations.Param;
import dist.middleware.annotations.Path;
import dist.middleware.annotations.RemoteObject;
import dist.middleware.annotations.RequestBody;

@RemoteObject("/produtos")
public class ProdutoService {

    @MethodMapping(path = "/buscar", method = HttpMethod.GET)
    public String buscarPorNome(@Param(name = "nome") String nome) {
        return "Buscando produto: " + nome;
    }

    @MethodMapping(path = "/{id}", method = HttpMethod.GET)
    public String buscarPorId(@Path(name = "id") int id) {
        return "Produto " + id;
    }

    @MethodMapping(path = "", method = HttpMethod.POST)
    public Produto criar(@RequestBody Produto produto) {
        return produto;
    }
}
```

Inicializacao:

```java
import dist.middleware.MiddlewareRunner;

public class Main {
    public static void main(String[] args) {
        MiddlewareRunner.start(8080, "com.exemplo.app");
    }
}
```

## Diferenciais tecnicos

- framework proprio, sem depender de Spring ou JAX-RS para roteamento
- uso de anotacoes customizadas para tornar a API declarativa
- pipeline enxuto e didatico para estudar sistemas distribuidos
- codigo organizado em camadas pequenas e objetivas
- base adequada para evolucoes como filtros, autenticacao, logs e suporte a mais verbos HTTP

## Contexto academico

Projeto desenvolvido como parte da disciplina de Programacao Distribuida da UFRN, com foco em explorar fundamentos de middleware, comunicacao entre processos e abstracoes para exposicao de servicos remotos.