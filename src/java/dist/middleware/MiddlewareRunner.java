package dist.middleware;

import dist.middleware.broker.Broker;
import dist.middleware.identifications.Lookup;
import dist.middleware.remoting.ServerRequestHandler;


public class MiddlewareRunner {

    public static void start(int port, String appBasePackage) {
        System.out.println("Iniciando Middleware...");

        Lookup lookup = new Lookup(appBasePackage);

        Broker broker = new Broker(lookup);

        ServerRequestHandler server = new ServerRequestHandler(port, broker);
        server.start();

        System.out.println("Middleware iniciado e pronto para receber requisições.");
    }
}