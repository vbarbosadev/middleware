package dist.example;

import dist.middleware.MiddlewareRunner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando API de Gestao de Produtos...");
        MiddlewareRunner.start(8080, "dist.example");
    }
}