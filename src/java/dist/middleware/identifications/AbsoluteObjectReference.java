package dist.middleware.identifications;

import dist.middleware.annotations.Param;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementação do padrão Absolute Object Reference.
 *
 * Esta classe contém todas as informações necessárias para encontrar e invocar
 * um metodo de um objeto remoto. Ela é o "valor" no mapa de lookup do nosso Broker.
 */
public class AbsoluteObjectReference {

    private final Object targetObject;

    private final Method method;

    /*
    * Um mapa que armazena informações sobre os parâmetros do metodo para o Marshaller.
    * Chave: nome do parâmetro na anotação @Param (ex: "a").
    * Valor: o tipo do parâmetro (ex: int.class).
    * */

    private final Map<String, Class<?>> methodParameters;

    public AbsoluteObjectReference(Object targetObject, Method method) {
        this.targetObject = targetObject;
        this.method = method;
        this.methodParameters = new HashMap<>();
        initializeParameters();
    }

    private void initializeParameters() {
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(Param.class)) {
                Param paramAnnotation = parameter.getAnnotation(Param.class);
                assert paramAnnotation != null;
                methodParameters.put(paramAnnotation.name(), parameter.getType());
            }
        }
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public Method getMethod() {
        return method;
    }

    public Map<String, Class<?>> getMethodParameters() {
        return methodParameters;
    }
}