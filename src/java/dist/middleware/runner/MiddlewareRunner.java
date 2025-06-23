package dist.middleware.runner;

import dist.middleware.broker.Broker;
import dist.middleware.identifications.Lookup;
import dist.middleware.remoting.ServerRequestHandler;

/**
 * Ponto de entrada do Middleware.
 * A aplicação do usuário chamará o método start() desta classe
 * para inicializar e rodar o framework.
 */
public class MiddlewareRunner {

    /**
     * Inicia o framework de middleware.
     * @param port A porta em que o servidor HTTP deve rodar.
     * @param appBasePackage O pacote base da aplicação do usuário (para o Lookup escanear).
     */
    public static void start(int port, String appBasePackage) {
        System.out.println("Iniciando Middleware...");

        Lookup lookup = new Lookup(appBasePackage);

        Broker broker = new Broker(lookup);

        ServerRequestHandler server = new ServerRequestHandler(port, broker);
        server.start();

        System.out.println("Middleware iniciado e pronto para receber requisições.");
    }
}