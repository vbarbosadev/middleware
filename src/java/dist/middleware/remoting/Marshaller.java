package dist.middleware.remoting;

import dist.middleware.annotations.Param;
import dist.middleware.identifications.AbsoluteObjectReference;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementação do padrão Marshaller.
 * Responsável por converter os dados da requisição (strings) para tipos Java
 * (unmarshalling) e o resultado da invocação de volta para uma string (marshalling).
 */
public class Marshaller {

    /**
     * Atualizado para receber parâmetros da URL (pathVariables) e da query string.
     */
    public Object[] unmarshall(String query, Map<String, String> pathVariables, AbsoluteObjectReference aor) throws Exception {
        Map<String, String> queryParams = parseQuery(query);

        Map<String, String> allParams = new HashMap<>(queryParams);
        allParams.putAll(pathVariables);

        Method method = aor.getMethod();
        Parameter[] methodParams = method.getParameters();
        Object[] arguments = new Object[methodParams.length];

        for (int i = 0; i < methodParams.length; i++) {
            Param paramAnnotation = methodParams[i].getAnnotation(Param.class);
            if (paramAnnotation == null) {
                throw new IllegalStateException("Todos os parâmetros do método " + method.getName() + " devem ser anotados com @Param.");
            }
            String paramName = paramAnnotation.name();
            String paramValueStr = allParams.get(paramName);

            if (paramValueStr == null) {
                throw new IllegalArgumentException("Parâmetro obrigatório '" + paramName + "' não encontrado na requisição.");
            }

            Class<?> paramType = methodParams[i].getType();
            arguments[i] = convert(paramValueStr, paramType);
        }
        return arguments;
    }

    public String marshall(Object result) { if (result == null) { return ""; } return String.valueOf(result); }
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
            String[] pair = param.split("=");
            if (pair.length > 1) params.put(pair[0], pair[1]); else params.put(pair[0], "");
        }
        return params;
    }
}