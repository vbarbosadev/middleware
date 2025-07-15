package dist.middleware.remoting;


import dist.middleware.identifications.AbsoluteObjectReference;

import java.lang.reflect.InvocationTargetException;

public class Invoker {


    public Object invoke(AbsoluteObjectReference aor, Object[] params) throws Exception {
        try {
            return aor.getMethod().invoke(aor.getTargetObject(), params);
        } catch (InvocationTargetException e) {
            throw (Exception) e.getTargetException();
        }
    }
}