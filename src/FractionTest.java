import static org.junit.jupiter.api.Assertions.*;

class FractionTest {

    @org.junit.jupiter.api.Test
    void getProxy() {
    }

    @org.junit.jupiter.api.Test
    void setNum() {
        Fraction fr = new Fraction(1, 10);
        fr.setNum(5);
        assert(fr.doubleValue() == 0.5);
    }

    @org.junit.jupiter.api.Test
    void setDenum() {
        Fraction fr = new Fraction(1, 10);
        fr.setDenum(5);
        assert(fr.doubleValue() == 0.2);
    }

    @org.junit.jupiter.api.Test
    void doubleValue() {
        Fraction fr = new Fraction(1, 10);
        assert(fr.doubleValue() == 0.1);
    }
}