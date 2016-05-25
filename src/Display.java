import java.awt.*;
import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Display extends JFrame implements MouseListener {
    Window window;
    int width, height;
    int PIXEL_SIZE;
    double scaleFactor = 10;
    ArrayList<Complex> roots;
    Polynomial p;
    Rational r;
    Trig t;
    boolean hasInit;
    boolean isRational = false;
    boolean isTrig = false;

    static  Color[] color_lookup = new Color[16];

    static {
        color_lookup[0] = new Color(0, 240, 0);
        color_lookup[1] = new Color(200, 0, 0);
        color_lookup[2] = new Color(57, 125, 209);
        color_lookup[3] = new Color(0, 0, 200);
        color_lookup[4] = new Color(0, 0, 100);
        color_lookup[5] = new Color(12, 44, 138);
        color_lookup[6] = new Color(24, 82, 177);
        color_lookup[7] = new Color(57, 125, 209);
        color_lookup[8] = new Color(134, 181, 229);
        color_lookup[9] = new Color(211, 236, 248);
        color_lookup[10] = new Color(241, 233, 191);
        color_lookup[11] = new Color(248, 201, 95);
        color_lookup[12] = new Color(255, 170, 0);
        color_lookup[13] = new Color(204, 128, 0);
        color_lookup[14] = new Color(153, 87, 0);
        color_lookup[15] = new Color(106, 52, 3);
    }

    private Color getColorForRoot(Complex z)
    {
        double n = z.c;
        double smooth = n + 1 - Math.log(Math.log(z.magnitude())) / Math.log(2.0);

        return new Color(Color.HSBtoRGB(0.95f + (float)(10 * smooth), 0.6f, 1.0f));
    }

    private Color getColorForRot(int esc, int root)
    {
        Color c = color_lookup[root%16];
        System.out.println(esc);
        return new Color((int)(c.getRed()*esc/100.0), (int)(c.getGreen()*esc/100.0), (int) (c.getBlue()*esc/100.0));
    }

    private Color getColorForot(int esc, int root)
    {
        Color c = color_lookup[root%16];
        int[] rootColor = {c.getRed(), c.getGreen(), c.getBlue()};
        int[] components = {0,0,0};

        //components[root%3] = (int) (rootColor[i] / (1 + Math.exp(- (esc+100)/100)))
        for (int i = 0; i<3; i++) {
            components[i] = (int) (rootColor[i] / (1 + Math.exp(- (esc+100)/100)));
            System.out.print(components[i] + " ");
        }
        System.out.println("");
        return new Color(components[0], components[1], components[2]);
    }

    private Color getColorForRoot(int esc, int root) //newton's method iterations for root, root index
    {
        return shadeColor(color_lookup[root % 16], esc);
    }

    private Color shadeColor(Color c, int esc) {
        //based off users/992484/madprogrammer

        int shadeMax = 200; // max newton's method iterations
        int shaderValue = (int) 255.0*(esc*5)/shadeMax;

        int r = Math.max(0, c.getRed() - shaderValue);
        int g = Math.max(0, c.getGreen() - shaderValue);
        int b = Math.max(0, c.getBlue() - shaderValue);

        return new Color(r,g,b,c.getAlpha());
    }

    public Display () {
        init();
    }

    public void init() {
        hasInit = false;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension newD = new Dimension(d.width/2, d.height/2);
        setVisible(true);
        setSize(newD);

        width = getWidth();
        height = getHeight();
        PIXEL_SIZE = 1;

        this.window = new Window();
        window.standardZoom();

        initRootsTrig();

        addMouseListener(this);
        this.addKeyListener(k);
        setFocusable(true);
        //repaint();
        hasInit = true;
        repaint();
    }

    public void initRoots() {
        Complex[] co1 = {
                new Complex(1, 0),
                new Complex(0, 0),
                new Complex(0, 0),
                new Complex(1, 0)
        };

        p = new Polynomial(co1.length-1, co1);

        roots = new ArrayList<Complex>(p.order+1);

        //generates roots
        for (int i = 0; i < width; i += PIXEL_SIZE) {
                for (int j = 0; j < height; j += PIXEL_SIZE) {
                    Complex c = window.complexForPoint(i, j);
                    Complex root = c.iterateForPolynomial(p);
                    if (root != null) {
                        Complex actualRoot = containsOrIsCloseTo(root);
                        if (!roots.contains(actualRoot)) roots.add(actualRoot);
                    }
                }
            }
        System.out.println("Did Init");
    }

    public void initRootsRat(){
        Complex[] co1 = {
                new Complex(1, 0),
                new Complex(0, 0),
                new Complex(0, 0),
                new Complex(0, 0),
                new Complex(0, 0),
                new Complex(1, 0)
        };

        Complex[] co2 = {
                new Complex(1,0),
                new Complex(0, 0),
                new Complex(0, 0),
                new Complex(1, 0)
        };

        Polynomial p1 = new Polynomial(co1.length-1, co1);
        Polynomial p2 = new Polynomial(co2.length-1, co2);

        r = new Rational(p1, p2);

        roots = new ArrayList<Complex>(p1.order-p2.order);
        isRational = true;
        //generates roots
        for (int i = 0; i < width; i += PIXEL_SIZE) {
            for (int j = 0; j < height; j += PIXEL_SIZE) {
                Complex c = window.complexForPoint(i, j);
                Complex root = c.iterateForRational(r);
                if (root != null) {
                    Complex actualRoot = containsOrIsCloseTo(root);
                    if (!roots.contains(actualRoot)) roots.add(actualRoot);
                }
            }
        }
        System.out.println("Did Init");

    }

    public void initRootsTrig(){
        Complex one = new Complex(1,0);
        Complex num = new Complex(1,0);
        t = new Trig(false, one, num);
        roots = new ArrayList<Complex>(16);
        isTrig = true;
        //generates roots
        for (int i = 0; i < width; i += PIXEL_SIZE) {
            for (int j = 0; j < height; j += PIXEL_SIZE) {
                Complex c = window.complexForPoint(i, j);
                Complex root = c.iterateForTrig(t);
                if (root != null) {
                    Complex actualRoot = containsOrIsCloseTo(root);
                    if (!roots.contains(actualRoot)) roots.add(actualRoot);
                }
            }
        }
        System.out.println("Did Init");
    }

    public KeyListener k = new KeyListener() {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        public void keyPressed(KeyEvent e) {
            int location = e.getKeyCode();

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

    };

    public Complex containsOrIsCloseTo(Complex root) {
        double tolerance = .000000001; //ten times tolerance of the root finder
        for (Complex c : roots) {
                if (root==null) {
                    break;
                }
                Complex dif = c.subtract(root);
                if (dif.magnitude() <= tolerance) return c;
        }
        return root;
    }

    public void paint(Graphics g) {
        if (hasInit!=true) {
            System.out.println("Doesn't paint");
            return;
        }
        System.out.println("Roots: " + roots);
            for (int i = 0; i < width; i += PIXEL_SIZE) {
                System.out.println(i);
                for (int j = 0; j < height; j += PIXEL_SIZE) {

                    Complex c = window.complexForPoint(i, j);

                    Complex root;
                    if (isRational){
                        root = c.iterateForFractalRat(r);
                    }
                    else if(isTrig) {
                        root = c.iterateForTrig(t);
                    }
                    else{
                            root = c.iterateForFractal(p);
                    }

                    Complex actualRoot = containsOrIsCloseTo(root);
                    if (root == null || roots.indexOf(actualRoot) == -1) g.setColor(Color.black);
                    else {
                        g.setColor(getColorForRoot((int) root.c, roots.indexOf(actualRoot)));
                        g.fillRect(i, j,PIXEL_SIZE,PIXEL_SIZE);
                    }
                }
            }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        window.zoomToPoint(e.getPoint(), scaleFactor);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private class Window {
        double minX, maxX, minY, maxY;

        public Window() {
            standardZoom();
        }

        public void standardZoom() {
            minX = -1;
            maxX = 1;
            minY = -1;
            maxY = 1;
        }

        public double xRange() { return Math.abs(minX - maxX); }
        public double yRange() { return Math.abs(minY - maxY); }

        public Complex complexForPoint(int x, int y) {
            double xPercent = (double)x / width;
            double yPercent = (double)y / height;
            return new Complex(minX+(xRange()*xPercent), minY+(yRange()*yPercent));
        }

        public void zoomToPoint(java.awt.Point p, double scaleFactor)
        {
            int x = p.x;
            int y = p.y;

            Complex c = this.complexForPoint(x,y);

            minX /= scaleFactor;
            maxX /= scaleFactor;
            minY /= scaleFactor;
            maxY /= scaleFactor;

            double width = xRange() / 2;
            double height = yRange() / 2;

            minX = c.real - width;
            maxX = c.real + width;

            minY = c.imaginary - height;
            maxY = c.imaginary + height;
            repaint();
        }
    }

}