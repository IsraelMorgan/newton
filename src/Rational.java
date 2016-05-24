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

    public Complex findZeroWithGuess(Complex test) {
        Complex x = test;
        double tolerance = .0000000001; // Stop if you're close enough
        int max_count = 200; // Maximum number of Newton's method iterations

         /* x is our current guess. If no command line guess is given,
        we take 0 as our starting point. */
        int count;
        for(count=1;
            //Carry on till we're close, or we've run it 200 times.
            (this.evaluate(x).magnitude() > tolerance) && ( count < max_count);
            count ++)  {

            x = x.subtract(this.evaluate(x).divide(this.getDerivative().evaluate(x)));  //Newtons method.
            // System.out.println("Step: "+count+" x:"+x+" Value:"+p.evaluate(x));
        }
        //OK, done let's report on the outcomes.
        if(this.evaluate(x).magnitude() <= tolerance) {
            x.c = count;
            return x;
        }
        return null;

    }

    public Complex fractalEsc(Complex test) {
        Complex x = test;
        double tolerance = .0000000001; // Stop if you're close enough
        int max_count = 200; // Maximum number of Newton's method iterations

         /* x is our current guess. If no command line guess is given,
        we take 0 as our starting point. */
        int count;
        for(count=1;
            //Carry on till we're close, or we've run it 200 times.
            (this.evaluate(x).magnitude() > tolerance) && ( count < max_count);
            count ++)  {

            Complex a = new Complex(1, 0);

            x = x.subtract(a.multiply(this.evaluate(x).divide(this.getDerivative().evaluate(x))));  //Newtons method.
            // System.out.println("Step: "+count+" x:"+x+" Value:"+p.evaluate(x));
        }
        //OK, done let's report on the outcomes.
        if(this.evaluate(x).magnitude() <= tolerance) {
            x.c = count;
            return x;
        }
        return null;

    }
}
