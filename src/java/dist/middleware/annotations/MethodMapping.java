package dist.middleware.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodMapping {

    /**
     * O caminho da URL para este endpoint (ex: "/soma").
     */
    String path();

    /**
     * O metodo HTTP que este endpoint irá responder (GET, POST, PUT, DELETE).
     */
    HttpMethod method();
}