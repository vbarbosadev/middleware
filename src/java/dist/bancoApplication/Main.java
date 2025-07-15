package dist.bancoApplication;

import dist.middleware.MiddlewareRunner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando a Aplicacao Bancaria...");


        MiddlewareRunner.start(8080, "dist.bancoApplication");
    }
}