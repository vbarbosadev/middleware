package dist.middleware.remoting;

import dist.middleware.broker.Broker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;


public class ServerRequestHandler {

    private final int port;
    private final Broker broker;

    public ServerRequestHandler(int port, Broker broker) {
        this.port = port;
        this.broker = broker;
    }


    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor TCP aguardando conexões na porta " + port);

            var executor = Executors.newVirtualThreadPerTaskExecutor();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                executor.submit(() -> new ClientHandler(clientSocket, broker));
            }

        } catch (IOException e) {
            System.err.println("Erro critico no servidor: " + e.getMessage());
            throw new RuntimeException("Falha ao iniciar o servidor TCP.", e);
        }
    }
}