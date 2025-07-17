package dist.middleware.remoting;


import dist.middleware.identifications.AbsoluteObjectReference;
import java.util.Map;


public record RouteMatchResult(
        AbsoluteObjectReference aor,
        Map<String, String> pathVariables
) {}