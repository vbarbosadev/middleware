package dist.middleware.remoting;

import dist.middleware.annotations.HttpMethod;
import dist.middleware.identifications.AbsoluteObjectReference;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Route {
    private final HttpMethod httpMethod;
    private final AbsoluteObjectReference aor;
    private final Pattern pattern;
    private final String[] variableNames;

    public Route(HttpMethod httpMethod, String pathPattern, AbsoluteObjectReference aor) {
        this.httpMethod = httpMethod;
        this.aor = aor;

        Pattern varPattern = Pattern.compile("\\{(\\w+)\\}");
        Matcher varMatcher = varPattern.matcher(pathPattern);
        this.variableNames = varMatcher.results().map(mr -> mr.group(1)).toArray(String[]::new);

        String regex = pathPattern.replaceAll("\\{\\w+\\}", "(\\\\w+)");
        this.pattern = Pattern.compile("^" + regex + "$");
    }

    /**
     * Tenta fazer a correspondência desta rota com a URL da requisição.
     * @param requestMethod O método HTTP da requisição.
     * @param requestPath O caminho da URL da requisição.
     * @return Um RouteMatchResult se a correspondência for bem-sucedida, caso contrário, null.
     */
    public RouteMatchResult match(HttpMethod requestMethod, String requestPath) {
        if (this.httpMethod != requestMethod) {
            return null;
        }

        Matcher pathMatcher = pattern.matcher(requestPath);
        if (pathMatcher.matches()) {
            Map<String, String> pathVariables = new HashMap<>();
            for (int i = 0; i < variableNames.length; i++) {
                String varName = variableNames[i];
                String varValue = pathMatcher.group(i + 1);
                pathVariables.put(varName, varValue);
            }
            return new RouteMatchResult(this.aor, pathVariables);
        }
        return null;
    }
}