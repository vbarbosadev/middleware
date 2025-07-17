package dist.middleware.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Path {
    /**
     * O nome da variável no caminho da URL.
     * Deve corresponder ao nome entre chaves no @MethodMapping.
     */
    String name();
}