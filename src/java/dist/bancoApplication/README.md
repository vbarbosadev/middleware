<!-- README.md -->

# Guia de Testes da Aplicação Bancária

Execute a classe `Main.java` e use as seguintes URLs no seu navegador ou JMeter para testar.

## Endpoints

### 1. Criar Conta
Cria uma nova conta com um titular e saldo inicial. O número da conta é gerado automaticamente.

- **URL:** `http://localhost:8080/contas/criar/{nomeUser}`
- **Método:** `POST`
- **Parâmetros:**
    - `titular` (String): Nome do titular da conta.
    - `saldo` (double): Saldo inicial.
    - `numConta` (int): Número da conta
- **Exemplo:** `http://localhost:8080/contas/criar/Ana?saldo=250&numConta=1010`

### 2. Buscar Conta
Busca os detalhes de uma conta existente pelo número.

- **URL:** `http://localhost:8080/contas/buscar/{numConta}`
- **Método:** `GET`
- **Parâmetros:**
    - `numero` (int): O número da conta a ser buscada.
- **Exemplo:** `http://localhost:8080/contas/buscar/1001`

### 3. Depositar
Adiciona um valor ao saldo de uma conta existente.

- **URL:** `http://localhost:8080/contas/depositar`
- **Método:** `GET`
- **Parâmetros:**
    - `numero` (int): Número da conta.
    - `valor` (double): Valor a ser depositado.
- **Exemplo:** `http://localhost:8080/contas/depositar????`

### 4. Sacar
Retira um valor do saldo de uma conta existente.

- **URL:** `http://localhost:8080/contas/sacar`
- **Método:** `GET`
- **Parâmetros:**
    - `numero` (int): Número da conta.
    - `valor` (double): Valor a ser sacado.
- **Exemplo:** `http://localhost:8080/contas/sacar???`

### 5. Deletar Conta
Remove uma conta do sistema.

- **URL:** `http://localhost:8080/contas/deletar/{numConta}`
- **Método:** `DELETE`
- **Parâmetros:**
    - `numero` (int): Número da conta a ser deletada.
- **Exemplo:** `http://localhost:8080/contas/deletar/1001`
