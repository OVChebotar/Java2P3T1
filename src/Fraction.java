import java.lang.reflect.Proxy;

interface Fractionable{
    double doubleValue();
    void setNum(int num) ;
    void setDenum(int denum) ;
}

public class Fraction implements Fractionable{
    public  Object getProxy()
    {
        Class cls = this.getClass();
        return  Proxy.newProxyInstance(cls.getClassLoader(),
                new Class[]{Fractionable.class},
                new FractionableInvHadler(this));
    }
    private int num;
    private int denum;

    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(time = 1000)
    public double doubleValue() {
        System.out.println("invoke double value");
        return (double) num/denum;
    }
}
