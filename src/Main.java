import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) {
        Fraction fr= new Fraction(2,3);
        Fractionable num = (Fractionable)fr.getProxy();
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
        num.doubleValue();// sout молчит
        num.setNum(5);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
        num.setDenum(10);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
    }
}
