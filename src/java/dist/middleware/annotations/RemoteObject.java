package dist.middleware.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RemoteObject {

    /**
     * Define um "caminho base" para todos os endpoints dentro desta classe.
     * Ex: se value() for "/calculator", e um @MethodMapping tiver path="/soma",
     * o caminho final será "/calculator/soma".
     * @return O caminho base da URL.
     */
    String value() default "";
}