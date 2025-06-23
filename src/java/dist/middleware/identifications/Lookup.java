package dist.middleware.identifications;

import dist.middleware.remoting.Route;
import dist.middleware.remoting.RouteMatchResult;
import org.reflections.Reflections;
import dist.middleware.annotations.MethodMapping;
import dist.middleware.annotations.RemoteObject;
import dist.middleware.annotations.HttpMethod;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Implementação do padrão Lookup.
 *
 * Esta classe é responsável por encontrar e registrar todos os objetos remotos
 * e seus métodos expostos. Ela é o "mapa" que o Broker usará para direcionar
 * as requisições.
 */
public class Lookup {
    private final List<Route> routes = new ArrayList<>();

    public Lookup(String basePackage) {
        scanAndRegister(basePackage);
    }

    private void scanAndRegister(String basePackage) {
        try {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> remoteObjectClasses = reflections.getTypesAnnotatedWith(RemoteObject.class);

            for (Class<?> clazz : remoteObjectClasses) {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                RemoteObject remoteObjectAnnotation = clazz.getAnnotation(RemoteObject.class);
                String basePath = remoteObjectAnnotation.value();

                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(MethodMapping.class)) {
                        MethodMapping methodMapping = method.getAnnotation(MethodMapping.class);
                        String methodPath = methodMapping.path();
                        String fullPath = basePath + methodPath;
                        HttpMethod httpMethod = methodMapping.method();

                        AbsoluteObjectReference aor = new AbsoluteObjectReference(instance, method);
                        Route route = new Route(httpMethod, fullPath, aor);
                        routes.add(route);

                        System.out.println("Rota Registrada: " + httpMethod + " " + fullPath);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Falha ao escanear e registrar objetos remotos.", e);
        }
    }
    /**
     * Procura por uma rota que corresponda à requisição.
     */
    public RouteMatchResult lookup(HttpMethod httpMethod, String requestPath) {
        for (Route route : routes) {
            RouteMatchResult result = route.match(httpMethod, requestPath);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}