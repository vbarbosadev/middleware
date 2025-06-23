package dist.middleware.remoting;

import dist.middleware.broker.Broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Lida com a comunicação de um único cliente em uma thread separada.
 * É responsável por ler e parsear a requisição HTTP, chamar o Broker
 * e escrever a resposta de volta para o cliente.
 */
public class ClientHandler {

    private final Socket clientSocket;
    private final Broker broker;

    public ClientHandler(Socket socket, Broker broker) {
        this.clientSocket = socket;
        this.broker = broker;
        run();
    }


    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
        ) {
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                return;
            }
            System.out.println("Thread " + Thread.currentThread().getId() + ": Recebido -> " + requestLine);

            String[] requestParts = requestLine.split(" ");
            String httpMethod = requestParts[0];
            String fullPath = URLDecoder.decode(requestParts[1], StandardCharsets.UTF_8);

            String path = fullPath;
            String query = "";
            if (fullPath.contains("?")) {
                path = fullPath.substring(0, fullPath.indexOf("?"));
                query = fullPath.substring(fullPath.indexOf("?") + 1);
            }

            HttpResponse httpResponse = broker.process(httpMethod, path, query);

            writer.println("HTTP/1.1 " + httpResponse.statusCode() + " " + httpResponse.statusMessage());
            writer.println("Content-Type: text/plain; charset=utf-8");
            writer.println("Content-Length: " + httpResponse.body().getBytes(StandardCharsets.UTF_8).length);
            writer.println("Connection: close");
            writer.println();
            writer.print(httpResponse.body());
            writer.flush();

        } catch (IOException e) {
            System.err.println("Erro ao manusear o cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar o socket do cliente: " + e.getMessage());
            }
        }
    }
}