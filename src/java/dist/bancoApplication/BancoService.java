package dist.bancoApplication;

// minha-aplicacao/src/main/java/com/exemplo/banco/service/BancoService.java

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class BancoService {
    private static final BancoService instance = new BancoService();
    private final Map<Integer, Conta> contas = new ConcurrentHashMap<>();
    private final AtomicInteger proximoNumeroConta = new AtomicInteger(1001);

    private BancoService() {}

    public static BancoService getInstance() {
        return instance;
    }

    public Conta criarConta(int numConta, String titular, double saldoInicial) {

        Conta novaConta = new Conta(numConta, titular, saldoInicial);
        contas.put(numConta, novaConta);
        return novaConta;
    }

    public Conta buscarConta(int numero) {
        return contas.get(numero);
    }

    public Conta deletarConta(int numero) {
        return contas.remove(numero);
    }

    public boolean depositar(int numero, double valor) {
        Conta conta = buscarConta(numero);
        if (conta != null) {
            conta.depositar(valor);
            return true;
        }
        return false;
    }

    public boolean sacar(int numero, double valor) {
        Conta conta = buscarConta(numero);
        if (conta != null) {
            return conta.sacar(valor);
        }
        return false;
    }
}