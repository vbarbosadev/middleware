package dist.middleware.remoting;

import com.google.gson.Gson;
import dist.middleware.annotations.Param;
import dist.middleware.identifications.AbsoluteObjectReference;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;


public class Marshaller {
    private final Gson gson = new Gson();

    public Object[] unmarshall(String query, String requestBody, Map<String, String> pathVariables, AbsoluteObjectReference aor) throws Exception {
        Map<String, String> queryParams = parseQuery(query);
        Map<String, String> allUrlParams = new HashMap<>(queryParams);
        allUrlParams.putAll(pathVariables);

        Method method = aor.getMethod();
        Parameter[] methodParams = method.getParameters();
        Object[] arguments = new Object[methodParams.length];

        for (int i = 0; i < methodParams.length; i++) {
            Parameter parameter = methodParams[i];
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                arguments[i] = gson.fromJson(requestBody, parameter.getType());
            } else if (parameter.isAnnotationPresent(Param.class)) {
                Param paramAnnotation = parameter.getAnnotation(Param.class);
                String paramName = paramAnnotation.name();
                String paramValueStr = allUrlParams.get(paramName);
                if (paramValueStr == null) throw new IllegalArgumentException("Parâmetro obrigatório '" + paramName + "' não encontrado.");
                arguments[i] = convert(paramValueStr, parameter.getType());
            } else {
                throw new IllegalStateException("Parâmetro " + parameter.getName() + " deve ser anotado com @Param ou @RequestBody.");
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
    
    private Object convert(String value, Class<?> targetType) { /* ... */ }
    private Map<String, String> parseQuery(String query) { /* ... */ }
}