import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class FractionableInvHadler implements InvocationHandler {
    private Object obj;
    private static Object cache;
    private static boolean cached = false;
    FractionableInvHadler(Object obj){this.obj = obj;}

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method m = obj.getClass().getMethod(method.getName(), method.getParameterTypes());

        Annotation[] anns = m.getDeclaredAnnotations();
        if (Arrays.stream(anns).filter(x->((Annotation)x).annotationType().equals(Cache.class)).count()>0) {
            if (!cached) {
                cache = method.invoke(obj, args);
                cached = true;
            }
            return cache;
        }
        if (Arrays.stream(anns).filter(x->((Annotation)x).annotationType().equals(Mutator.class)).count()>0) {
            cached = false;
        }
        return method.invoke(obj, args);
    }

}
