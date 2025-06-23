package dist.middleware.remoting;


import dist.middleware.identifications.AbsoluteObjectReference;

import java.lang.reflect.InvocationTargetException;

/**
 * Implementação do padrão Invoker.
 * Responsável por executar (invocar) o método do objeto de negócio
 * usando a API de Reflection do Java.
 */
public class Invoker {

    /**
     * Invoca o método do objeto de destino.
     * @param aor A referência completa para o método a ser invocado.
     * @param params Os parâmetros já convertidos pelo Marshaller.
     * @return O objeto retornado pelo método invocado.
     * @throws Exception Propaga exceções, incluindo as da lógica de negócio.
     */
    public Object invoke(AbsoluteObjectReference aor, Object[] params) throws Exception {
        try {
            return aor.getMethod().invoke(aor.getTargetObject(), params);
        } catch (InvocationTargetException e) {
            throw (Exception) e.getTargetException();
        }
    }
}