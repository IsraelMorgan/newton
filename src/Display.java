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
    boolean hasInit;

    static  Color[] color_lookup = new Color[16];

    static {
        color_lookup[0] = new Color(0, 240, 0);
        color_lookup[1] = new Color(200, 0, 0);
        color_lookup[2] = new Color(57, 125, 209);
        color_lookup[3] = new Color(0, 0, 200);
        color_lookup[4] = new Color(0, 0, 100);
        /*color_lookup[0] = new Color(25, 7, 26);
        color_lookup[1] = new Color(25, 7, 26);
        color_lookup[2] = new Color(9, 1, 47);
        color_lookup[3] = new Color(4, 4, 73);
        color_lookup[4] = new Color(0, 7, 100);
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
        color_lookup[15] = new Color(106, 52, 3);*/
    }

    private Color getColorForRoot(int root)
    {
        return color_lookup[root % 16];
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

        initRoots();

        addMouseListener(this);
        this.addKeyListener(k);
        setFocusable(true);
        //repaint();
        hasInit = true;
        repaint();
    }

    public void initRoots() {
        Complex[] co = {
                new Complex(1, 0),
                new Complex(0, 0),
                new Complex(0, 0),
                new Complex(0, 0),
                new Complex(0, 0),
                new Complex(1, 0)
        };
        p = new Polynomial(5, co);
        roots = new ArrayList<Complex>(p.order+1);

        //generates roots
        for (int i = 0; i < width; i += PIXEL_SIZE) {
            System.out.println(i);
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
                for (int j = 0; j < height; j += PIXEL_SIZE) {

                    Complex c = window.complexForPoint(i, j);
                    Complex root = c.iterateForPolynomial(p);
                    Complex actualRoot = containsOrIsCloseTo(root);
                    if (root == null || roots.indexOf(actualRoot) == -1) g.setColor(Color.black);
                    else {
                        g.setColor(getColorForRoot(roots.indexOf(actualRoot)));
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
            System.out.println("About to repaint lol");
            repaint();
        }
    }

}