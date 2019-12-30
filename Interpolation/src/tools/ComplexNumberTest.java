package tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexNumberTest {


    @Test
    public void multiplyingTest() {
        ComplexNumber c1 = new ComplexNumber(2, 3);
        ComplexNumber c2 = new ComplexNumber(3, -1);
        ComplexNumber result = ComplexNumber.multiply(c1, c2);
        System.out.println(c1 + " * " + c2 + " = " + result);
        assertEquals( new ComplexNumber(9, 7), result);
    }


    @Test
    public void powerTest() {
        ComplexNumber c1 = new ComplexNumber(1, -2);
        ComplexNumber c2 = new ComplexNumber(1, -3);
        ComplexNumber pow1 = ComplexNumber.pow(c1, 2);
        ComplexNumber pow2 = ComplexNumber.pow(c2, 3);

        ComplexNumber result = ComplexNumber.add(pow1, pow2);
        System.out.println(pow1 + " + " + pow2 + " = " + result);
        assertEquals(new ComplexNumber(-29, 14), result);
    }
    @Test
    public void divideTest() {
        ComplexNumber c1 = new ComplexNumber(2, -1);
        ComplexNumber c2 = new ComplexNumber(1, 1);

        ComplexNumber result = ComplexNumber.divide(c1, c2);
        System.out.println(c1 + " / " + c2 + " = " + result);
        assertEquals(new ComplexNumber(.5, 3 / (double) 2), result);
    }

}