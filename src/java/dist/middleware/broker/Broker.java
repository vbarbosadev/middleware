package dist.middleware.broker;

import dist.middleware.annotations.HttpMethod;
import dist.middleware.identifications.AbsoluteObjectReference;
import dist.middleware.remoting.HttpResponse;
import dist.middleware.identifications.Lookup;
import dist.middleware.remoting.Invoker;
import dist.middleware.remoting.Marshaller;
import dist.middleware.remoting.RouteMatchResult;

import java.net.http.HttpRequest;
import java.util.Arrays;

/**
 * Implementação do padrão Broker.
 * É o coordenador central que recebe uma requisição e orquestra os outros
 * componentes (Lookup, Marshaller, Invoker) para processá-la.
 */
public class Broker {
    private final Lookup lookup;
    private final Marshaller marshaller;
    private final Invoker invoker;
    private final ResponseCodes responseCodes = ResponseCodes.getInstance();

    public Broker(Lookup lookup) {
        this.lookup = lookup;
        this.marshaller = new Marshaller();
        this.invoker = new Invoker();
    }

    public HttpResponse process(String httpMethodStr, String path, String query) {
        HttpMethod httpMethod;
        try {
            httpMethod = HttpMethod.valueOf(httpMethodStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new HttpResponse(400, "Bad Request", "Método HTTP inválido.");
        }

        RouteMatchResult routeResult = lookup.lookup(httpMethod, path);

        if (routeResult == null) {
            String body = "Endpoint nao encontrado para " + httpMethodStr + " " + path;
            return new HttpResponse(404, "Not Found", body);
        }

        /*
        * debug
            System.out.println("[unMARSHALLER] arguments " + Arrays.toString(arguments));
            System.out.println("[ROUTE-RESULT] " + routeResult.aor());

            System.out.println("[INVOKER] result: " + result);
            System.out.println("[INVOKER] " + invoker.invoke(routeResult.aor(), arguments));
            System.out.println("[MARSHALLER] " + responseBody);
            System.out.println("[MARSHALLER] marshall" + marshaller.marshall(result));
         */



        try {
            Object[] arguments = marshaller.unmarshall(query, routeResult.pathVariables(), routeResult.aor());
            Object result = invoker.invoke(routeResult.aor(), arguments);
            String responseBody = marshaller.marshall(result);

                try {


                    return responseCodes.getHttpResponse(responseBody);
                } catch (Exception parseException) {
                    return new HttpResponse(500, "Internal Server Error", "Erro ao formatar a resposta de erro customizada.");
                }
        } catch (Exception e) {
            return new HttpResponse(500, "Internal Server Error", "Erro interno: " + e.getMessage());
        }
    }


}