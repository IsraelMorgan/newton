import java.awt.*;

/**
 * Created by matthew on 2/17/15.
 */


public class Complex {
    double real, imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Complex() {
        this.real = 0;
        this.imaginary = 0;
    }

    public Complex multiply(Complex other)
    {
        double real = (this.real * other.real) - (this.imaginary * other.imaginary);
        double imag = (this.real * other.imaginary) + (this.imaginary * other.real);
        return new Complex(real, imag);
    }

    public Complex divide(Complex other) {
        double retReal = ((this.real*other.real)+(this.imaginary*other.imaginary))/((other.real*other.real)+(other.imaginary*other.imaginary));
        double retImaginary = ((this.imaginary*other.real)-(this.real*other.imaginary))/((other.real*other.real)+(other.imaginary*other.imaginary));
        return new Complex(retReal,retImaginary);
    }

    public Complex pow(int pow) {
        if (pow == 0) return new Complex(1, 0);
        Complex c = this;
        for (int i = 1; i < pow; i++) c = c.multiply(this);
        return c;
    }

    public Complex pow(Complex other, int pow) {
        for (int i = 1; i < pow; i++) other = other.multiply(other);
        return other;
    }

    public Complex iterateForPolynomial(Polynomial p){
        return p.findZeroWithGuess(this);
    }

    public Complex add(Complex c)
    {
        double real = this.real + c.real;
        double imag = this.imaginary + c.imaginary;
        return new Complex(real, imag);
    }

    public Complex subtract(Complex c)
    {
        double real = this.real - c.real;
        double imag = this.imaginary - c.imaginary;
        return new Complex(real, imag);
    }

    public Complex sin() {
        double real = Math.sin(this.real) * Math.cosh(this.imaginary);
        double imaginary = Math.cos(this.real) * Math.sinh(this.imaginary);

        this.real = real;
        this.imaginary = imaginary;

        return this;
    }

    public String toString() {
        return "(" + real + " + " + imaginary + "i)";
    }

    public double magnitude() {
        return Math.sqrt(real*real + imaginary*imaginary);
    }
}
