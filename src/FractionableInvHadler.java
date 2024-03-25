import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import static java.lang.Long.min;

public class FractionableInvHadler implements InvocationHandler {
    private Object obj;

    //Ключ - метод и атрибуты, значение - кэш(ключ "Cache") и время умирания(ключ "Time")
    private static ConcurrentHashMap<String, HashMap<String, Object>> map;
    FractionableInvHadler(Object obj) {
        this.obj = obj;
        map = new ConcurrentHashMap();
        (new Thread(()->{
            long curTime = System.currentTimeMillis();
            long minTime, tTime;        //minTime - ближайшее время умирания кэша
            while (true) {
                minTime = 0;
                for (String key : map.keySet()) {   //пробегаемся, удаляем старые и заодно определяем minTime
                    try {
                        tTime = (Long)map.get(key).get("Time");
                        if (tTime < curTime) {
                            map.remove(key);
                        }
                        else {
                            minTime = minTime == 0 ? tTime : min(minTime, tTime);
                        }
                    } catch (NullPointerException e) {}
                }
                if (curTime < minTime) {
                    try {
                        Thread.sleep((int)((minTime - curTime)*1.2));   //Засыпаем на 20% дольше чем до первого умирания кэша
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        })).start();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method m = obj.getClass().getMethod(method.getName(), method.getParameterTypes());

        Annotation[] anns = m.getDeclaredAnnotations();
        if (m.isAnnotationPresent(Cache.class)) {
            Object res;
            String State;
            Long curTime = System.currentTimeMillis();
            Long elTime = 0L;
            long lifeTime = m.getAnnotation(Cache.class).time();
            State = "" + m + getState(obj);     //состояние = метод + значения атрибутов объекта
            try {
                res = map.get(State).get("Cache");
                elTime = (Long)map.get(State).get("Time");
            } catch (NullPointerException e) {
                res = null;
            }
            if (res == null || elTime < curTime) {
                res = method.invoke(obj, args);
            }
            HashMap hm = new HashMap();
            hm.put("Cache", res);
            hm.put("Time", System.currentTimeMillis() + lifeTime);      //время умирания кэша
            map.put(State, hm);
            return res;
        }
        return method.invoke(obj, args);
    }

    public String getState(Object obj) throws IllegalAccessException {
        String res = "";
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            res += ";" + String.valueOf(field.get(obj));
        }
        return res;
    }

}
