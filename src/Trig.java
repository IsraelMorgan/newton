/**
 * Created by ryanmitra on 5/24/16.
 */
public class Trig {

    boolean isSin; //true = sin, false = cos
    Complex coefficient;
    Complex period;

    public Trig(){
        isSin = true;
        coefficient = new Complex(1,0);
        period = new Complex (1,0);
        //sets Trig function to sinx
    }

    public Trig(boolean trig, Complex coef, Complex per){
        isSin = trig;
        coefficient = coef;
        period = per;
    }

    public Complex evaluate(Complex c){
        Complex retval;
        if(isSin){
            retval = coefficient.multiply((c.multiply(period)).sin());
        }else{
            retval = coefficient.multiply((c.multiply(period)).cos());
        }
        return retval;
    }

    public Trig getDerivative(){
        Boolean newIsSin = true;
        Complex newCoef = this.coefficient;
        Complex newPer = this.period;

        if(this.isSin == true){
            newIsSin = false;
            newCoef = (this.coefficient).multiply(this.period);
        }
        else if(this.isSin== false){
            newIsSin = true;
            Complex negOne = new Complex(-1,0);
            newCoef = ((this.coefficient).multiply(this.period)).multiply(negOne);
        }
        return new Trig(newIsSin, newCoef, newPer);
    }

    public String toString(){
        String retval = "idk";
        if(isSin == true){
            retval = "Trig Function: " + coefficient + "Sin(" + period + "x)";
        }
        else if(isSin == false){
            retval = "Trig Function: " + coefficient + "Cos(" + period + "x)";
        }
        return retval;
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

