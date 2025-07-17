package dist.middleware.remoting;

import com.google.gson.Gson;
import dist.middleware.annotations.Param;
import dist.middleware.annotations.Path;
import dist.middleware.annotations.RequestBody;
import dist.middleware.identifications.AbsoluteObjectReference;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class Marshaller {
    private final Gson gson = new Gson();

    public Object[] unmarshall(String query, String requestBody, Map<String, String> pathVariables, AbsoluteObjectReference aor) throws Exception {
        Map<String, String> queryParams = parseQuery(query);

        Method method = aor.getMethod();
        Parameter[] methodParams = method.getParameters();
        Object[] arguments = new Object[methodParams.length];

        for (int i = 0; i < methodParams.length; i++) {
            Parameter parameter = methodParams[i];

            if (parameter.isAnnotationPresent(RequestBody.class)) {
                //  @RequestBody
                arguments[i] = gson.fromJson(requestBody, parameter.getType());

            } else if (parameter.isAnnotationPresent(Path.class)) {
                // @Path
                Path pathAnnotation = parameter.getAnnotation(Path.class);
                String paramName = pathAnnotation.name();
                String paramValueStr = pathVariables.get(paramName);
                if (paramValueStr == null) {
                    throw new IllegalArgumentException("Variável de caminho (path) '" + paramName + "' não encontrada na URL.");
                }
                arguments[i] = convert(paramValueStr, parameter.getType());

            } else if (parameter.isAnnotationPresent(Param.class)) {
                // @Param
                Param paramAnnotation = parameter.getAnnotation(Param.class);
                String paramName = paramAnnotation.name();
                String paramValueStr = queryParams.get(paramName);
                if (paramValueStr == null) {
                    throw new IllegalArgumentException("Parâmetro de query '" + paramName + "' não encontrado.");
                }
                arguments[i] = convert(paramValueStr, parameter.getType());

            } else {
                throw new IllegalStateException("Parâmetro " + parameter.getName() + " deve ser anotado com @Path, @Param, ou @RequestBody.");
            }
        }
        return arguments;
    }

    public String marshall(Object result) {
        if (result instanceof String) {
            if (((String) result).startsWith("ERROR:")) return (String) result;
        }
        return gson.toJson(result);
    }

    private Object convert(String value, Class<?> targetType) {
        if (targetType == String.class) return value;
        if (targetType == int.class || targetType == Integer.class) return Integer.parseInt(value);
        if (targetType == double.class || targetType == Double.class) return Double.parseDouble(value);
        if (targetType == long.class || targetType == Long.class) return Long.parseLong(value);
        if (targetType == boolean.class || targetType == Boolean.class) return Boolean.parseBoolean(value);
        throw new IllegalArgumentException("Tipo de parâmetro não suportado para conversão: " + targetType.getName());
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) return params;
        for (String param : query.split("&")) {
            String[] pair = param.split("=", 2);
            if (pair.length > 1) params.put(pair[0], pair[1]); else params.put(pair[0], "");
        }
        return params;
    }
}