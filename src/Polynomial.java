import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Created by matthew on 6/17/15.
 */
public class Polynomial {
    int order;
    Complex[] coefficients;

    public Polynomial(){
        order = 0;
        coefficients = new Complex[0];
    }

    public Polynomial(int order, Complex[] coefficients){
        this.order = order;
        this.coefficients = coefficients;
        if (order!=coefficients.length-1) {
            throw new IllegalArgumentException("Bad Polynomial");
        }
    }

    public Polynomial getDerivative() {
        Complex[] newCoefficients = new Complex[coefficients.length-1];
        for (int i = 0; i<order; i++) {
            newCoefficients[i] = coefficients[i].multiply(new Complex((order-i), 0));
        }
        Polynomial retval = new Polynomial(order-1, newCoefficients);
        return retval;
    }

    public Complex evaluate(Complex c) {
        Complex retval = new Complex();
        for (int i = 0; i<=order; i++) {
            Complex add = coefficients[i].multiply(c.pow(order-i));
            // System.out.println(add);
            retval = retval.add(add);
        }
        return retval;
    }

    public String toString() {
        String arr = "";
        for (Complex a : coefficients) {
            arr+=a;
            arr+=" ";
        }
        return "Degree: " + order + " Coefficients: " + arr;
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
            return x;
        }
        return null;

    }

}
