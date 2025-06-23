package dist.bancoApplication;


import dist.middleware.annotations.*;

@RemoteObject("/contas")
public class BancoController {

    private final BancoService bancoService = BancoService.getInstance();

    /**
     * Busca uma conta pelo número usando Path Variable.
     * Ex: GET /contas/buscar/1001
     */
    @MethodMapping(path = "/buscar/{numero}", method = HttpMethod.GET)
    public String buscarConta(@Param(name = "numero") int numeroConta) {
        Conta conta = bancoService.buscarConta(numeroConta);
        if (conta != null) {
            return "200:"+conta.toString();

        }
        return "404:Conta nao encontrada.";
    }

    /**
     * Deleta uma conta usando Path Variable.
     * Ex: DELETE /contas/deletar/1001
     */
    @MethodMapping(path = "/deletar/{numero}", method = HttpMethod.DELETE)
    public String deletarConta(@Param(name = "numero") int numeroConta) {
        Conta conta = bancoService.deletarConta(numeroConta);
        if (conta != null) {
            return "201:Conta deletada com sucesso: " + conta.toString();
        }
        return "204:Conta " + numeroConta + " nao encontrada.";
    }

    /**
     * Cria uma nova conta usando Path Variable e Query Param.
     * Ex: POST /contas/criar/Joao?saldo=500
     */
    @MethodMapping(path = "/criar/{titular}", method = HttpMethod.POST)
    public String criarConta(
            @Param(name = "titular") String titular,
            @Param(name = "saldo") double saldo,
            @Param(name = "numConta") int numConta
    )
    {
        if(bancoService.buscarConta(numConta) != null) {
            return "409:Conta com o numero " + numConta + " já existe.";
        }
        Conta conta = bancoService.criarConta(numConta, titular, saldo);
        return "201:Conta criada com sucesso: " + conta.toString();

    }
}