package dist.middleware.identifications;

import dist.middleware.annotations.Param;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class AbsoluteObjectReference {

    private final Object targetObject;

    private final Method method;

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