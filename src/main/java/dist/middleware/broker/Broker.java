package dist.middleware.broker;

import dist.middleware.annotations.HttpMethod;
import dist.middleware.remoting.HttpResponse;
import dist.middleware.identifications.Lookup;
import dist.middleware.remoting.Invoker;
import dist.middleware.remoting.Marshaller;
import dist.middleware.remoting.RouteMatchResult;


public class Broker {
    private final Lookup lookup;
    private final Marshaller marshaller;
    private final Invoker invoker;

    public Broker(Lookup lookup) {
        this.lookup = lookup;
        this.marshaller = new Marshaller();
        this.invoker = new Invoker();
    }

    public HttpResponse process(String httpMethodStr, String path, String query, String requestBody) {
        HttpMethod httpMethod;
        try {
            httpMethod = HttpMethod.valueOf(httpMethodStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new HttpResponse(400, "Bad Request", "{\"error\":\"Método HTTP inválido.\"}");
        }

        RouteMatchResult routeResult = lookup.lookup(httpMethod, path);

        if (routeResult == null) {
            return new HttpResponse(404, "Not Found", "{\"error\":\"Endpoint nao encontrado.\"}");
        }

        try {
            Object[] arguments = marshaller.unmarshall(query, requestBody, routeResult.pathVariables(), routeResult.aor());
            Object result = invoker.invoke(routeResult.aor(), arguments);
            String responseBody = marshaller.marshall(result);
            
            if (responseBody.startsWith("ERROR:409:")) {
                 return new HttpResponse(409, "Conflict", "{\"error\":\"" + responseBody.substring(10) + "\"}");
            }

            return new HttpResponse(200, "OK", responseBody);
        } catch (Exception e) {
            return new HttpResponse(500, "Internal Server Error", "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}   