package dist.middleware.broker;

import dist.middleware.remoting.HttpResponse;

public class ResponseCodes {

    private static final ResponseCodes instance = new ResponseCodes();


    private ResponseCodes() {}

    public static ResponseCodes getInstance() { return instance; }

    /**
     * Inicia o framework de middleware.
     * @param responseBody corpo da resposta da aplicação, para pegar o codigo de resposta e a menssagem
     */
    public HttpResponse getHttpResponse(String responseBody) {
        String[] parts = responseBody.split(":", 2);
        int statusCode = Integer.parseInt(parts[0]);
        String message = parts[1];
        return searchCode(statusCode, message);
    }

    /**
     * Inicia o framework de middleware.
     * @param statusCode codigo de resposta da aplicação
     * @param message mensagem de resposta da aplicação.
     */
    public HttpResponse searchCode(int statusCode, String message) {
        return switch (statusCode) {
            case 400 -> new HttpResponse(400, "Bad Request", message);
            case 404 -> new HttpResponse(404, "Not Found", message);
            case 409 -> new HttpResponse(409, "Conflict", message);
            case 204 -> new HttpResponse(204, "No Content", message);
            default -> new HttpResponse(200, "OK", message);
        };

    }

}
