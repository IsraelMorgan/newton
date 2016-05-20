/**
 * Created by ryanmitra on 5/19/16.
 */
//Rational function of the from P(x)/Q(x)
public class Rational {

    Polynomial p;
    Polynomial q;

    public Rational(){
        Complex[] co = {
                new Complex(1, 0),
                new Complex(0, 0),
                new Complex(1, 0)};
        Complex[] co1 = {
                new Complex(1, 0),
                new Complex(0, 0),
                new Complex(0, 0),
                new Complex(1, 0)};
        p = new Polynomial(co.length-1, co);
        q = new Polynomial(co1.length-1, co1);
    }

    public Rational(Polynomial Px, Polynomial Qx){
        this.p = Px;
        this.q = Qx;
    }

    public Rational getDerivative() {

        Polynomial retP = (((this.p).getDerivative()).multiply(this.q)).subtract(((this.q).getDerivative()).multiply(this.p));
        Polynomial retQ = (this.q).multiply(this.q);

        Rational retval = new Rational(retP, retQ);
        return retval;
    }

    public Complex evaluate(Complex c) {

        Complex evalPx = p.evaluate(c);
        Complex evalQx = q.evaluate(c);
        Complex retval = evalPx.divide(evalQx);

        return retval;
    }

    public String toString(){

        return "P(x): " + (this.p) + "\n" + "Q(x): " + (this.q);
    }
}
