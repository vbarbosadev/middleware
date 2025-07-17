# Guia de Testes da API de Produtos

### 1. Buscar Todos os Produtos (GET)
```bash
curl "http://localhost:8080/produtos"
```

### 2. Buscar um Produto por ID (GET com @Path)
Substitua `{id_do_produto}` por um ID retornado no comando anterior.
```bash
curl "http://localhost:8080/produtos/{id_do_produto}"
```

### 3. Filtrar Produtos por Preço Máximo (GET com @Param)
```bash
curl "http://localhost:8080/produtos/filtrar?precoMax=1000"
```

### 4. Criar um Novo Produto (POST com @RequestBody)
```bash
curl -X POST "http://localhost:8080/produtos" \
-H "Content-Type: application/json" \
-d '{"nome": "Teclado Mecanico", "preco": 350.0, "estoque": 50}'
```

### 5. Atualizar um Produto (PUT com @Path e @RequestBody)
Substitua `{id_do_produto}` por um ID existente.
```bash
curl -X PUT "http://localhost:8080/produtos/{id_do_produto}" \
-H "Content-Type: application/json" \
-d '{"nome": "Teclado Mecanico RGB", "preco": 420.0, "estoque": 45}'
```

### 6. Deletar um Produto (DELETE com @Path)
Substitua `{id_do_produto}` por um ID existente.
```bash
curl -X DELETE "http://localhost:8080/produtos/{id_do_produto}"
