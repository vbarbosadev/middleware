package dist.middleware.remoting;

import dist.middleware.broker.Broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Broker broker;

    public ClientHandler(Socket socket, Broker broker) {
        this.clientSocket = socket;
        this.broker = broker;
    }

    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
        ) {
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

            Map<String, String> headers = new HashMap<>();
            String headerLine;
            while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
                String[] headerParts = headerLine.split(": ", 2);
                if (headerParts.length == 2) {
                    headers.put(headerParts[0].toLowerCase(), headerParts[1]);
                }
            }

            String requestBody = "";
            if (headers.containsKey("content-length")) {
                int contentLength = Integer.parseInt(headers.get("content-length"));
                char[] bodyChars = new char[contentLength];
                reader.read(bodyChars, 0, contentLength);
                requestBody = new String(bodyChars);
            }

            String[] requestParts = requestLine.split(" ");
            String httpMethod = requestParts[0];
            String fullPath = URLDecoder.decode(requestParts[1], StandardCharsets.UTF_8);

            String path = fullPath.contains("?") ? fullPath.substring(0, fullPath.indexOf("?")) : fullPath;
            String query = fullPath.contains("?") ? fullPath.substring(fullPath.indexOf("?") + 1) : "";

            HttpResponse httpResponse = broker.process(httpMethod, path, query, requestBody);

            writer.println("HTTP/1.1 " + httpResponse.statusCode() + " " + httpResponse.statusMessage());
            writer.println("Content-Type: application/json; charset=utf-8"); // Resposta agora é JSON
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