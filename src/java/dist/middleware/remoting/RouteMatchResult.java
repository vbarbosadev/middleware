package dist.middleware.remoting;


import dist.middleware.identifications.AbsoluteObjectReference;
import java.util.Map;

/**
 * DTO que representa o resultado de uma busca de rota bem-sucedida.
 * Contém a referência do método e os parâmetros extraídos da URL.
 */
public record RouteMatchResult(
        AbsoluteObjectReference aor,
        Map<String, String> pathVariables
) {}